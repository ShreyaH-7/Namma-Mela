package com.nammamela.app.repository

import com.nammamela.app.data.local.AppDatabase
import com.nammamela.app.data.local.entity.BookingEntity
import com.nammamela.app.data.local.entity.CastEntity
import com.nammamela.app.data.local.entity.CommentEntity
import com.nammamela.app.data.local.entity.PlayEntity
import com.nammamela.app.data.local.entity.UserEntity
import com.nammamela.app.data.model.AuthResult
import com.nammamela.app.data.model.CastMember
import com.nammamela.app.data.model.Comment
import com.nammamela.app.data.model.Play
import com.nammamela.app.data.remote.ApiService
import com.nammamela.app.data.remote.dto.AdminLoginRequest
import com.nammamela.app.data.remote.dto.BookingRequest
import com.nammamela.app.data.remote.dto.CastRequest
import com.nammamela.app.data.remote.dto.CommentRequest
import com.nammamela.app.data.remote.dto.LoginRequest
import com.nammamela.app.data.remote.dto.PlayRequest
import com.nammamela.app.data.remote.dto.RegisterRequest
import com.nammamela.app.util.Resource
import com.nammamela.app.util.SessionManager
import retrofit2.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRepository(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val sessionManager: SessionManager,
) {

    val cachedPlay: Flow<Play?> = appDatabase.playDao().observePlay().map { entity -> entity?.toModel() }
    val cachedComments: Flow<List<Comment>> = appDatabase.commentDao().observeComments().map { list -> list.map { it.toModel() } }
    val cachedCast: Flow<List<CastMember>> = appDatabase.castDao().observeCast().map { list -> list.map { it.toModel() } }
    val cachedBookings = appDatabase.bookingDao().observeBookings()

    suspend fun register(name: String, email: String, password: String): Resource<AuthResult> {
        return safeApiCall {
            val result = apiService.register(RegisterRequest(name, email, password)).toModel()
            persistSession(result)
            result
        }
    }

    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return safeApiCall {
            val result = apiService.login(LoginRequest(email, password)).toModel()
            persistSession(result)
            result
        }
    }

    suspend fun adminLogin(email: String?, password: String?, pin: String?): Resource<AuthResult> {
        return safeApiCall {
            val result = apiService.adminLogin(AdminLoginRequest(email, password, pin)).toModel()
            persistSession(result)
            result
        }
    }

    suspend fun fetchPlay(): Resource<Play> {
        return safeApiCall {
            val play = apiService.getPlay().toModel()
            // The latest server copy is cached locally so the app can show data faster next time.
            appDatabase.playDao().upsert(play.toEntity())
            play
        }
    }

    suspend fun savePlay(playRequest: PlayRequest): Resource<Play> {
        return safeApiCall {
            val play = apiService.savePlay(playRequest).toModel()
            appDatabase.playDao().upsert(play.toEntity())
            play
        }
    }

    suspend fun fetchSeats(): Resource<List<String>> {
        return safeApiCall {
            val response = apiService.getSeats()
            appDatabase.bookingDao().clear()
            response.bookings.forEach { booking ->
                appDatabase.bookingDao().insert(
                    BookingEntity(
                        id = booking.id,
                        userId = sessionManager.getUserId(),
                        customerName = booking.customerName,
                        seatsCsv = booking.seats.joinToString(","),
                        timestamp = booking.timestamp.orEmpty()
                    )
                )
            }
            response.reservedSeats
        }
    }

    suspend fun createBooking(customerName: String, seats: List<String>): Resource<Unit> {
        return safeApiCall {
            val booking = apiService.createBooking(
                BookingRequest(
                    customerName = customerName,
                    seats = seats.sortedBy { it.removePrefix("S").toIntOrNull() ?: Int.MAX_VALUE }
                )
            )
            appDatabase.bookingDao().insert(
                BookingEntity(
                    id = booking.id,
                    userId = sessionManager.getUserId(),
                    customerName = booking.customerName,
                    seatsCsv = booking.seats.joinToString(","),
                    timestamp = booking.timestamp.orEmpty()
                )
            )
        }
    }

    suspend fun fetchComments(): Resource<List<Comment>> {
        return safeApiCall {
            val comments = apiService.getComments().map { it.toModel() }
            appDatabase.commentDao().clear()
            appDatabase.commentDao().upsertAll(comments.map { it.toEntity() })
            comments
        }
    }

    suspend fun addComment(name: String, message: String): Resource<Unit> {
        return safeApiCall {
            val comment = apiService.addComment(CommentRequest(name, message)).toModel()
            appDatabase.commentDao().insert(comment.toEntity())
        }
    }

    suspend fun fetchCast(): Resource<List<CastMember>> {
        return safeApiCall {
            val cast = apiService.getCast().map { it.toModel() }
            appDatabase.castDao().clear()
            appDatabase.castDao().upsertAll(cast.map { it.toEntity() })
            cast
        }
    }

    suspend fun addCast(request: CastRequest): Resource<Unit> {
        return safeApiCall {
            val cast = apiService.addCast(request).toModel()
            appDatabase.castDao().insert(cast.toEntity())
        }
    }

    suspend fun resetSeats(): Resource<String> {
        return safeApiCall {
            appDatabase.bookingDao().clear()
            apiService.resetSeats().message
        }
    }

    suspend fun logout() {
        sessionManager.clearSession()
        appDatabase.userDao().clear()
        appDatabase.bookingDao().clear()
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
    fun isAdmin(): Boolean = sessionManager.isAdmin()
    fun currentName(): String = sessionManager.getName()

    private suspend fun persistSession(result: AuthResult) {
        // SharedPreferences keeps the session alive, while Room stores lightweight user details offline.
        sessionManager.saveSession(
            token = result.token,
            userId = result.user.id,
            name = result.user.name,
            email = result.user.email,
            role = result.user.role
        )
        appDatabase.userDao().upsert(
            UserEntity(
                id = result.user.id,
                name = result.user.name,
                email = result.user.email,
                role = result.user.role
            )
        )
    }

    private suspend fun <T> safeApiCall(block: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(block())
        } catch (exception: HttpException) {
            Resource.Error(exception.message ?: "Request failed")
        } catch (exception: Exception) {
            Resource.Error(exception.localizedMessage ?: "Something went wrong")
        }
    }
}

private fun PlayEntity.toModel() = Play(id, title, genre, duration, description, poster)
private fun Play.toEntity() = PlayEntity(id, title, genre, duration, description, poster)

private fun CommentEntity.toModel() = Comment(id, name, message, time)
private fun Comment.toEntity() = CommentEntity(id, name, message, time)

private fun CastEntity.toModel() = CastMember(id, name, role, bio, image)
private fun CastMember.toEntity() = CastEntity(id, name, role, bio, image)

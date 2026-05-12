package com.nammamela.app.data.remote

import com.nammamela.app.data.remote.dto.AdminLoginRequest
import com.nammamela.app.data.remote.dto.AuthResponse
import com.nammamela.app.data.remote.dto.BookingRequest
import com.nammamela.app.data.remote.dto.BookingResponse
import com.nammamela.app.data.remote.dto.CastRequest
import com.nammamela.app.data.remote.dto.CastResponse
import com.nammamela.app.data.remote.dto.CommentRequest
import com.nammamela.app.data.remote.dto.CommentResponse
import com.nammamela.app.data.remote.dto.LoginRequest
import com.nammamela.app.data.remote.dto.MessageResponse
import com.nammamela.app.data.remote.dto.PlayRequest
import com.nammamela.app.data.remote.dto.PlayResponse
import com.nammamela.app.data.remote.dto.RegisterRequest
import com.nammamela.app.data.remote.dto.SeatsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("admin/login")
    suspend fun adminLogin(@Body request: AdminLoginRequest): AuthResponse

    @GET("play")
    suspend fun getPlay(): PlayResponse

    @POST("play")
    suspend fun savePlay(@Body request: PlayRequest): PlayResponse

    @GET("seats")
    suspend fun getSeats(): SeatsResponse

    @POST("booking")
    suspend fun createBooking(@Body request: BookingRequest): BookingResponse

    @GET("comments")
    suspend fun getComments(): List<CommentResponse>

    @POST("comments")
    suspend fun addComment(@Body request: CommentRequest): CommentResponse

    @GET("cast")
    suspend fun getCast(): List<CastResponse>

    @POST("cast")
    suspend fun addCast(@Body request: CastRequest): CastResponse

    @DELETE("reset-seats")
    suspend fun resetSeats(): MessageResponse
}

package com.nammamela.app.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nammamela.app.data.model.AuthResult
import com.nammamela.app.data.model.Booking
import com.nammamela.app.data.model.CastMember
import com.nammamela.app.data.model.Comment
import com.nammamela.app.data.model.Play

data class AuthResponse(
    val token: String,
    val user: UserResponse,
) {
    fun toModel(): AuthResult = AuthResult(token, user.toModel())
}

data class UserResponse(
    @SerializedName("id") val id: String,
    val name: String,
    val email: String,
    val role: String,
) {
    fun toModel() = com.nammamela.app.data.model.User(id, name, email, role)
}

data class PlayResponse(
    @SerializedName("_id") val id: String,
    val title: String,
    val genre: String,
    val duration: String,
    val description: String,
    val poster: String,
) {
    fun toModel() = Play(id, title, genre, duration, description, poster)
}

data class BookingResponse(
    @SerializedName("_id") val id: String,
    val customerName: String,
    val seats: List<String>,
    val timestamp: String?,
) {
    fun toModel(userId: String) = Booking(id, userId, customerName, seats, timestamp.orEmpty())
}

data class SeatsResponse(
    val reservedSeats: List<String>,
    val bookings: List<BookingResponse>,
)

data class CommentResponse(
    @SerializedName("_id") val id: String,
    val name: String,
    val message: String,
    val time: String?,
) {
    fun toModel() = Comment(id, name, message, time.orEmpty())
}

data class CastResponse(
    @SerializedName("_id") val id: String,
    val name: String,
    val role: String,
    val bio: String,
    val image: String,
) {
    fun toModel() = CastMember(id, name, role, bio, image)
}

data class MessageResponse(
    val message: String,
)

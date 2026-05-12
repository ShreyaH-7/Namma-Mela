package com.nammamela.app.data.remote.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class AdminLoginRequest(
    val email: String? = null,
    val password: String? = null,
    val pin: String? = null,
)

data class PlayRequest(
    val title: String,
    val genre: String,
    val duration: String,
    val description: String,
    val poster: String,
)

data class BookingRequest(
    val customerName: String,
    val seats: List<String>,
)

data class CommentRequest(
    val name: String,
    val message: String,
)

data class CastRequest(
    val name: String,
    val role: String,
    val bio: String,
    val image: String,
)

package com.nammamela.app.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
)

data class Play(
    val id: String = "",
    val title: String,
    val genre: String,
    val duration: String,
    val description: String,
    val poster: String,
)

data class Booking(
    val id: String = "",
    val userId: String,
    val customerName: String,
    val seats: List<String>,
    val timestamp: String,
)

data class Comment(
    val id: String = "",
    val name: String,
    val message: String,
    val time: String,
)

data class CastMember(
    val id: String = "",
    val name: String,
    val role: String,
    val bio: String,
    val image: String,
)

data class AuthResult(
    val token: String,
    val user: User,
)

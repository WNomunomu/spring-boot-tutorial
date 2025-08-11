package com.example.taskmanager.dto

import com.example.taskmanager.entity.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                username = user.username,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}
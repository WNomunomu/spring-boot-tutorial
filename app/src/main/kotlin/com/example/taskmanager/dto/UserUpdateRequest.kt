package com.example.taskmanager.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserUpdateRequest(
    @field:Size(max = 50, message = "Username must be at most 50 characters")
    val username: String?,

    @field:Email(message = "Email must be valid")
    @field:Size(max = 255, message = "Email must be at most 255 characters")
    val email: String?
)
package com.example.taskmanager.service

import com.example.taskmanager.dto.UserCreateRequest
import com.example.taskmanager.dto.UserResponse
import com.example.taskmanager.dto.UserUpdateRequest
import com.example.taskmanager.entity.User
import com.example.taskmanager.exception.DuplicateUserException
import com.example.taskmanager.exception.UserNotFoundException
import com.example.taskmanager.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository) {

    fun createUser(request: UserCreateRequest): UserResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw DuplicateUserException("Username '${request.username}' already exists")
        }
        
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateUserException("Email '${request.email}' already exists")
        }

        val user = User(
            username = request.username,
            email = request.email
        )

        val savedUser = userRepository.save(user)
        return UserResponse.from(savedUser)
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { UserResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }
        return UserResponse.from(user)
    }

    fun updateUser(id: Long, request: UserUpdateRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException("User not found with id: $id") }

        request.username?.let { newUsername ->
            if (newUsername != user.username && userRepository.existsByUsername(newUsername)) {
                throw DuplicateUserException("Username '$newUsername' already exists")
            }
            user.username = newUsername
        }

        request.email?.let { newEmail ->
            if (newEmail != user.email && userRepository.existsByEmail(newEmail)) {
                throw DuplicateUserException("Email '$newEmail' already exists")
            }
            user.email = newEmail
        }

        val savedUser = userRepository.save(user)
        return UserResponse.from(savedUser)
    }

    fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw UserNotFoundException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }
}
package com.example.taskmanager.controller

import com.example.taskmanager.dto.UserCreateRequest
import com.example.taskmanager.dto.UserResponse
import com.example.taskmanager.dto.UserUpdateRequest
import com.example.taskmanager.exception.DuplicateUserException
import com.example.taskmanager.exception.UserNotFoundException
import com.example.taskmanager.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return try {
            val user = userService.getUserById(id)
            ResponseEntity.ok(user)
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createUser(@Valid @RequestBody request: UserCreateRequest): ResponseEntity<Any> {
        return try {
            val user = userService.createUser(request)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (e: DuplicateUserException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<Any> {
        return try {
            val user = userService.updateUser(id, request)
            ResponseEntity.ok(user)
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: DuplicateUserException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            userService.deleteUser(id)
            ResponseEntity.noContent().build()
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}
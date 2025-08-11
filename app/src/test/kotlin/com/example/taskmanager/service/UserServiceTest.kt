package com.example.taskmanager.service

import com.example.taskmanager.dto.UserCreateRequest
import com.example.taskmanager.dto.UserUpdateRequest
import com.example.taskmanager.entity.User
import com.example.taskmanager.exception.DuplicateUserException
import com.example.taskmanager.exception.UserNotFoundException
import com.example.taskmanager.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    private lateinit var testUser: User

    @BeforeEach
    fun setUp() {
        testUser = User(
            id = 1L,
            username = "testuser",
            email = "test@example.com"
        )
    }

    @Test
    fun `createUser should create user when no duplicates exist`() {
        val request = UserCreateRequest("newuser", "new@example.com")
        
        `when`(userRepository.existsByUsername("newuser")).thenReturn(false)
        `when`(userRepository.existsByEmail("new@example.com")).thenReturn(false)
        `when`(userRepository.save(any(User::class.java))).thenReturn(testUser)

        val result = userService.createUser(request)

        assertNotNull(result)
        verify(userRepository).existsByUsername("newuser")
        verify(userRepository).existsByEmail("new@example.com")
        verify(userRepository).save(any(User::class.java))
    }

    @Test
    fun `createUser should throw exception when username exists`() {
        val request = UserCreateRequest("existinguser", "new@example.com")
        
        `when`(userRepository.existsByUsername("existinguser")).thenReturn(true)

        assertThrows(DuplicateUserException::class.java) {
            userService.createUser(request)
        }
    }

    @Test
    fun `getUserById should return user when exists`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.of(testUser))

        val result = userService.getUserById(1L)

        assertEquals(testUser.id, result.id)
        assertEquals(testUser.username, result.username)
        assertEquals(testUser.email, result.email)
    }

    @Test
    fun `getUserById should throw exception when user not found`() {
        `when`(userRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(UserNotFoundException::class.java) {
            userService.getUserById(1L)
        }
    }

    @Test
    fun `deleteUser should delete when user exists`() {
        `when`(userRepository.existsById(1L)).thenReturn(true)

        userService.deleteUser(1L)

        verify(userRepository).deleteById(1L)
    }

    @Test
    fun `deleteUser should throw exception when user not found`() {
        `when`(userRepository.existsById(1L)).thenReturn(false)

        assertThrows(UserNotFoundException::class.java) {
            userService.deleteUser(1L)
        }
    }
}
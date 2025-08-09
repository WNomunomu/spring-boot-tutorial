package com.example.taskmanager.repository

import com.example.taskmanager.entity.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository
) {

    @Test
    fun `should save and find user by username`() {
        // Given
        val user = User(username = "testuser", email = "test@example.com")
        entityManager.persist(user)
        entityManager.flush()

        // When
        val found = userRepository.findByUsername("testuser")

        // Then
        assertTrue(found.isPresent)
        assertEquals("testuser", found.get().username)
        assertEquals("test@example.com", found.get().email)
    }

    @Test
    fun `should find user by email`() {
        // Given
        val user = User(username = "testuser", email = "test@example.com")
        entityManager.persist(user)
        entityManager.flush()

        // When
        val found = userRepository.findByEmail("test@example.com")

        // Then
        assertTrue(found.isPresent)
        assertEquals("testuser", found.get().username)
    }

    @Test
    fun `should check if username exists`() {
        // Given
        val user = User(username = "testuser", email = "test@example.com")
        entityManager.persist(user)
        entityManager.flush()

        // When & Then
        assertTrue(userRepository.existsByUsername("testuser"))
        assertFalse(userRepository.existsByUsername("nonexistent"))
    }

    @Test
    fun `should check if email exists`() {
        // Given
        val user = User(username = "testuser", email = "test@example.com")
        entityManager.persist(user)
        entityManager.flush()

        // When & Then
        assertTrue(userRepository.existsByEmail("test@example.com"))
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"))
    }
}
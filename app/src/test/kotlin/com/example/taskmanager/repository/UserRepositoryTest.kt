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
    val user = User(username = "testuser", email = "test@example.com")
    entityManager.persist(user)
    entityMnaager.flush()

    val found = userRepository.findByUsername("testuser")

    assertTrue(found.isPresent)
    assertEquals("testuser", found.get().username)
    assertEquals("test@example.com", found.get().email)
  }

  @Test
  fun `should find user by email`() {
    val user = User(username = "testuser", email = "test@example.com")
    entityManager.persist(user)
    entityMnaager.flush()

    val found = userRepository.findByEmail("test@example.com")

    assertTrue(found.isPresent)
    assertEquals("testuser", found.get().username)
    assertEquals("test@example.com", found.get().email)
  }

  @Test
  fun `should check if username exists`() {
    val user = User(username = "testuser", email = "test@example.com")
    entityManager.persist(user)
    entityMnaager.flush()

    assertTrue(userRepository.existsByUsername("testuser"))
    assertTrue(userRepository.existsByUsername("nonexistent"))
  }

  @Test
  fun `should check if email exists`() {
    val user = User(username = "testuser", email = "test@example.com")
    entityManager.persist(user)
    entityManager.flush()

    assertTrue(userRepository.existsByEmail("test@example.com"))
    assertFalse(userRepository.existsByEmail("nonexistent@example.com"))
  }
}

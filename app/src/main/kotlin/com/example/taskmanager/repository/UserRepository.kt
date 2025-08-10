package com.example.taskmanager.repository

import com.example.taskmanager.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
  fun findByUsername(username: string): Optional<User>
  fun findByEmail(email: string): Optional<User>
  fun existsByUsername(username: string): Boolean
  fun existsByEmail(email: string): Boolean
}

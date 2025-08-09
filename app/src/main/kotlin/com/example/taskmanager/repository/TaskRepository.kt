package com.example.taskmanager.repository

import com.example.taskmanager.entity.Task
import com.example.taskmanager.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUser(user: User): List<Task>
    fun findByUserId(userId: Long): List<Task>
    fun findByUserIdOrderByIdDesc(userId: Long): List<Task>
    fun findByUserAndCompleted(user: User, completed: Boolean): List<Task>
    fun countByUserId(userId: Long): Long
}

package com.example.taskmanager.repository

import com.example.taskmanager.entity.Task
import com.example.taskmanager.entity.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DataJpaTest
@ActiveProfiles("test")
class TaskUserRelationshipTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val taskRepository: TaskRepository
) {

    @Test
    fun `should create task with user relationship`() {
        // Given
        val user = User(username = "testuser", email = "test@example.com")
        val savedUser = entityManager.persist(user)
        entityManager.flush()

        val task = Task(
            title = "Test Task", 
            description = "Test Description", 
            user = savedUser
        )
        
        // When
        val savedTask = taskRepository.save(task)
        
        // Then
        assertEquals("testuser", savedTask.user.username)
        assertEquals("Test Task", savedTask.title)
    }

    @Test
    fun `should find tasks by user id`() {
        // Given
        val user1 = entityManager.persist(User(username = "user1", email = "user1@example.com"))
        val user2 = entityManager.persist(User(username = "user2", email = "user2@example.com"))
        
        val task1 = entityManager.persist(Task(title = "Task 1", user = user1))
        val task2 = entityManager.persist(Task(title = "Task 2", user = user1))
        val task3 = entityManager.persist(Task(title = "Task 3", user = user2))
        entityManager.flush()

        // When
        val user1Tasks = taskRepository.findByUserId(user1.id)
        val user2Tasks = taskRepository.findByUserId(user2.id)

        // Then
        assertEquals(2, user1Tasks.size)
        assertEquals(1, user2Tasks.size)
        assertTrue(user1Tasks.any { it.title == "Task 1" })
        assertTrue(user1Tasks.any { it.title == "Task 2" })
        assertTrue(user2Tasks.any { it.title == "Task 3" })
    }

    @Test
    fun `should find tasks by user object`() {
        // Given
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        val task1 = entityManager.persist(Task(title = "Task 1", user = user))
        val task2 = entityManager.persist(Task(title = "Task 2", user = user))
        entityManager.flush()

        // When
        val tasks = taskRepository.findByUser(user)

        // Then
        assertEquals(2, tasks.size)
        assertTrue(tasks.all { it.user.id == user.id })
    }

    @Test
    fun `should find tasks by user and completion status`() {
        // Given
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        val completedTask = entityManager.persist(Task(title = "Completed", user = user, completed = true))
        val incompleteTask = entityManager.persist(Task(title = "Incomplete", user = user, completed = false))
        entityManager.flush()

        // When
        val completedTasks = taskRepository.findByUserAndCompleted(user, true)
        val incompleteTasks = taskRepository.findByUserAndCompleted(user, false)

        // Then
        assertEquals(1, completedTasks.size)
        assertEquals(1, incompleteTasks.size)
        assertEquals("Completed", completedTasks[0].title)
        assertEquals("Incomplete", incompleteTasks[0].title)
    }

    @Test
    fun `should count tasks by user id`() {
        // Given
        val user1 = entityManager.persist(User(username = "user1", email = "user1@example.com"))
        val user2 = entityManager.persist(User(username = "user2", email = "user2@example.com"))
        
        entityManager.persist(Task(title = "Task 1", user = user1))
        entityManager.persist(Task(title = "Task 2", user = user1))
        entityManager.persist(Task(title = "Task 3", user = user1))
        entityManager.persist(Task(title = "Task 4", user = user2))
        entityManager.flush()

        // When
        val user1Count = taskRepository.countByUserId(user1.id)
        val user2Count = taskRepository.countByUserId(user2.id)

        // Then
        assertEquals(3, user1Count)
        assertEquals(1, user2Count)
    }

    @Test
    fun `should order tasks by id desc for user`() {
        // Given
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        val task1 = entityManager.persist(Task(title = "Task 1", user = user))
        val task2 = entityManager.persist(Task(title = "Task 2", user = user))
        val task3 = entityManager.persist(Task(title = "Task 3", user = user))
        entityManager.flush()

        // When
        val tasks = taskRepository.findByUserIdOrderByIdDesc(user.id)

        // Then
        assertEquals(3, tasks.size)
        assertTrue(tasks[0].id >= tasks[1].id)
        assertTrue(tasks[1].id >= tasks[2].id)
    }
}
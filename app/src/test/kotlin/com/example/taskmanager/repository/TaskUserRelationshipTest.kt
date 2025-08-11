package com.example.taskmanager.repository

import com.example.taskmanager.entity.Task
import com.example.taskmanager.entity.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Assertions.*

@DataJpaTest
@ActiveProfiles("test")
class TaskUserRelationshipTest @Autowired constructor(
    val entityManager: TestEntityManager,
    val userRepository: UserRepository,
    val taskRepository: TaskRepository
) {

    @Test
    fun `should create task with user relationship`() {
        val user = User(username = "testuser", email = "test@example.com")
        val savedUser = entityManager.persist(user)
        entityManager.flush()

        val task = Task(
            title = "Test Task", 
            description = "Test Description", 
            user = savedUser
        )

        val savedTask = taskRepository.save(task)

        assertEquals("testuser", savedTask.user.username)
        assertEquals("Test Task", savedTask.title)
    }

    @Test
    fun `should find tasks by user id`() {
        val user1 = entityManager.persist(User(username = "user1", email = "user1@example.com"))
        val user2 = entityManager.persist(User(username = "user2", email = "user2@example.com"))

        entityManager.persist(Task(title = "Task 1", description = "Description 1", user = user1))
        entityManager.persist(Task(title = "Task 2", description = "Description 2", user = user1))
        entityManager.persist(Task(title = "Task 3", description = "Description 3", user = user2))
        entityManager.flush()

        val user1Tasks = taskRepository.findByUserId(user1.id)
        val user2Tasks = taskRepository.findByUserId(user2.id)

        assertEquals(2, user1Tasks.size)
        assertEquals(1, user2Tasks.size)
        assertTrue(user1Tasks.any { it.title == "Task 1" })
        assertTrue(user1Tasks.any { it.title == "Task 2" })
        assertTrue(user2Tasks.any { it.title == "Task 3" })
    }

    @Test
    fun `should find tasks by user object`() {
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        entityManager.persist(Task(title = "Task 1", description = "Description 1", user = user))
        entityManager.persist(Task(title = "Task 2", description = "Description 2", user = user))
        entityManager.flush()

        val tasks = taskRepository.findByUser(user)

        assertEquals(2, tasks.size)
        assertTrue(tasks.all { it.user.id == user.id })
    }

    @Test
    fun `should find tasks by user and completion status`() {
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        entityManager.persist(Task(title = "Completed", description = "Completed task", user = user, completed = true))
        entityManager.persist(Task(title = "Incomplete", description = "Incomplete task", user = user, completed = false))
        entityManager.flush()

        val completedTasks = taskRepository.findByUserAndCompleted(user, true)
        val incompleteTasks = taskRepository.findByUserAndCompleted(user, false)

        assertEquals(1, completedTasks.size)
        assertEquals(1, incompleteTasks.size)
        assertEquals("Completed", completedTasks[0].title)
        assertEquals("Incomplete", incompleteTasks[0].title)
    }

    @Test
    fun `should count tasks by user id`() {
        val user1 = entityManager.persist(User(username = "user1", email = "user1@example.com"))
        val user2 = entityManager.persist(User(username = "user2", email = "user2@example.com"))

        entityManager.persist(Task(title = "Task 1", description = "Description 1", user = user1))
        entityManager.persist(Task(title = "Task 2", description = "Description 2", user = user1))
        entityManager.persist(Task(title = "Task 3", description = "Description 3", user = user1))
        entityManager.persist(Task(title = "Task 4", description = "Description 4", user = user2))
        entityManager.flush()

        val user1Count = taskRepository.countByUserId(user1.id)
        val user2Count = taskRepository.countByUserId(user2.id)

        assertEquals(3, user1Count)
        assertEquals(1, user2Count)
    }

    @Test
    fun `should order tasks by id desc for user`() {
        val user = entityManager.persist(User(username = "testuser", email = "test@example.com"))
        entityManager.persist(Task(title = "Task 1", description = "Description 1", user = user))
        entityManager.persist(Task(title = "Task 2", description = "Description 2", user = user))
        entityManager.persist(Task(title = "Task 3", description = "Description 3", user = user))
        entityManager.flush()

        val tasks = taskRepository.findByUserIdOrderByIdDesc(user.id)

        assertEquals(3, tasks.size)
        assertTrue(tasks[0].id >= tasks[1].id)
        assertTrue(tasks[1].id >= tasks[2].id)
    }
}

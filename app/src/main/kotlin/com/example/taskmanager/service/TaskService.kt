package com.example.taskmanager.service

import com.example.taskmanager.model.Task
import com.example.taskmanager.model.TaskCreateRequest
import com.example.taskmanager.model.TaskUpdateRequest
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong
import org.springframework.stereotype.Service

@Service
class TaskService {

  private val tasks = mutableMapOf<Long, Task>()
  private val idGenerator = AtomicLong(0)

  fun createTask(request: TaskCreateRequest): Task {
    val task =
            Task(
                    id = idGenerator.incrementAndGet(),
                    title = request.title,
                    description = request.description
            )
    tasks[task.id] = task

    return task
  }

  fun getAllTasks(): List<Task> {
    return tasks.values.toList()
  }

  fun getTaskById(id: Long): Task? {
    return tasks[id]
  }

  fun udpateTask(id: Long, request: TaskUpdateRequest): Task? {
    val task: Task = tasks[id] ?: return null

    request.title?.let { task.title = it }
    request.description?.let { task.description = it }
    request.completed?.let { task.completed = it }
    task.updatedAt = LocalDateTime.now()

    return task
  }

  fun deleteTask(id: Long): Boolean {
    return tasks.remove(id) != null
  }
}

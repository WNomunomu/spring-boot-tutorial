package com.example.taskmanager.service

import com.example.taskmanager.entity.Task
import com.example.taskmanager.model.TaskCreateRequest
import com.example.taskmanager.model.TaskUpdateRequest
import com.example.taskmanager.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val taskRepository: TaskRepository) {

  fun createTask(request: TaskCreateRequest): Task {
    val task = Task(
      title = request.title,
      description = request.description
    )
    return taskRepository.save(task)
  }

  fun getAllTasks(): List<Task> {
    return taskRepository.findAll()
  }

  fun getTaskById(id: Long): Task? {
    return taskRepository.findById(id).orElse(null)
  }

  fun updateTask(id: Long, request: TaskUpdateRequest): Task? {
    val task = taskRepository.findById(id).orElse(null) ?: return null

    request.title?.let { task.title = it }
    request.description?.let { task.description = it }
    request.completed?.let { task.completed = it }

    return taskRepository.save(task)
  }

  fun deleteTask(id: Long): Boolean {
    return if (taskRepository.existsById(id)) {
      taskRepository.deleteById(id)
      true
    } else {
      false
    }
  }
}

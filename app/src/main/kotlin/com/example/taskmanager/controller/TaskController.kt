package com.example.taskmanager.controller

import com.example.taskmanager.entity.Task
import com.example.taskmanager.model.TaskCreateRequest
import com.example.taskmanager.model.TaskUpdateRequest
import com.example.taskmanager.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {

  @GetMapping
  fun getAllTasks(): List<Task> {
    return taskService.getAllTasks()
  }

  @GetMapping("/{id}")
  fun getTask(@PathVariable id: Long): ResponseEntity<Task> {
    val task = taskService.getTaskById(id)
    return if (task != null) {
      ResponseEntity.ok(task)
    } else {
      ResponseEntity.notFound().build()
    }
  }

  @PostMapping
  fun createTask(@RequestBody request: TaskCreateRequest): ResponseEntity<Task> {
    val task = taskService.createTask(request)
    return ResponseEntity.status(HttpStatus.CREATED).body(task)
  }

  @PutMapping("/{id}")
  fun updateTask(
          @PathVariable id: Long,
          @RequestBody request: TaskUpdateRequest
  ): ResponseEntity<Task> {
    val task = taskService.updateTask(id, request)
    return if (task != null) {
      ResponseEntity.ok(task)
    } else {
      ResponseEntity.notFound().build()
    }
  }

  @DeleteMapping("/{id}")
  fun deleteTask(@PathVariable id: Long): ResponseEntity<Void> {
    val deleted = taskService.deleteTask(id)
    return if (deleted) {
      ResponseEntity.noContent().build()
    } else {
      ResponseEntity.notFound().build()
    }
  }
}

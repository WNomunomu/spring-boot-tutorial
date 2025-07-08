package com.example.taskmanager.model

import java.time.LocalDateTime

data class Task(
  val id: Long,
  var title: String,
  var description: String? = null,
  var completed: Boolean = false,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  var updatedAt: LocalDateTime = LocalDateTime.now()
)

data class TaskCreateRequest(
  val title: String,
  val description: String? = null
)

data class TaskUpdateRequest(
  val title: String? = null,
  val description: String? = null,
  val completed: Boolean? = null
)

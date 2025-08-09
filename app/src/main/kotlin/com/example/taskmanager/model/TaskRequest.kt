package com.example.taskmanager.model

data class TaskCreateRequest(
  val title: String,
  val description: String? = null
)

data class TaskUpdateRequest(
  val title: String? = null,
  val description: String? = null,
  val completed: Boolean? = null
)
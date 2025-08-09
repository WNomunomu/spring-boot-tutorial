package com.example.taskmanager.entity

import jakarta.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(nullable = false)
  var title: String,

  @Column
  var description: String? = null,

  @Column(nullable = false)
  var completed: Boolean = false
)

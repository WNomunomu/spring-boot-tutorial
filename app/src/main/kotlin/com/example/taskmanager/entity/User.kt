package com.example.taskmanager.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(unique = true, nullable = false)
  @NotBlank(message = "ユーザー名は必須です")
  @Size(min = 3, max = 50, message = "ユーザー名は3文字以上50文字以下である必要があります")
  private val username: String,

  @Column(unique = true, nullable = false)
  @Email(message = "有効なメールアドレスを入力してください")
  @NotBlank(message = "メールアドレスは必須です")
  val email: String,

  @Column(nullable = false)
  @NotBlank(message = "パスワードは必須です")
  private val password: String,

  @Enumerated(EnumType.STRING)
  val role: Role = Role.USER,

  @Column(name = "created_at")
  val createdAt: LocalDateTime = LocalDateTime.now(),

  @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
  val tasks: MutableList<Task> = mutableListOf()
) : UserDetails {

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf(SimpleGrantedAuthority("ROLE_${role.name}$"))
  }

  override fun getPassword(): String = password

  override fun getUsername(): String = username

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = true
  
}

enum class Role {
  USER, ADMIN
}

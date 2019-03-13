package com.karumi.springbootkotlin.developers.storage

import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.EncodedPassword
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class DeveloperEntity(
  val username: String,
  val email: String?,
  val password: String,
  @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: UUID?
)

fun Developer.toEntity(): DeveloperEntity = DeveloperEntity(username, email, password.password, id)

fun DeveloperEntity.toDomain(): Developer = Developer(
  username, email, EncodedPassword(password), id
)
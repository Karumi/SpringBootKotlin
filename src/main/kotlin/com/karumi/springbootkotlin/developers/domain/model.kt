package com.karumi.springbootkotlin.developers.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

typealias
  PasswordEncoder = (String) -> String

data class Developer(
  val username: String,
  val email: String?,
  val password: String,
  val id: UUID? = null
)

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
object StorageError : RuntimeException("Internal storage error")

@ResponseStatus(HttpStatus.NOT_FOUND)
object NotFound : RuntimeException("Developer not found")

@ResponseStatus(HttpStatus.BAD_REQUEST)
object NotKarumier : RuntimeException("Developer isn't karumier")

@ResponseStatus(HttpStatus.CONFLICT)
object AlreadyRegistered : RuntimeException("Developer already registered")
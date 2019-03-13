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

sealed class DeveloperError(message: String) : RuntimeException(message) {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  object StorageError : DeveloperError("Internal storage error")

  @ResponseStatus(HttpStatus.NOT_FOUND)
  object NotFound : DeveloperError("Developer not found")

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  object NotKarumier : DeveloperError("Developer isn't karumier")

  @ResponseStatus(HttpStatus.CONFLICT)
  object AlreadyRegistered : DeveloperError("Developer already registered")
}
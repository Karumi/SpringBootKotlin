package com.karumi.springbootkotlin.developers.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Developer(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),
    val username: String,
    val email: String?
)

sealed class DeveloperError(message: String) : RuntimeException(message) {
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  object StorageError : DeveloperError("Internal storage error")
  @ResponseStatus(HttpStatus.NOT_FOUND)
  object NotFound : DeveloperError("Developer not found")
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  object NotKarumier : DeveloperError("Developer isn't karumier")
}
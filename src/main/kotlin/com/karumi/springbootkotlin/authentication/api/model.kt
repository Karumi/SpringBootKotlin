package com.karumi.springbootkotlin.authentication.api

import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.PasswordEncoder

data class NewDeveloperRequest(
  val username: String,
  val email: String?,
  val password: String
)

data class LoginRequest(
  val username: String,
  val password: String
)

fun NewDeveloperRequest.toDomain(encoder: PasswordEncoder): Developer = Developer(
  username = username,
  email = email,
  password = encoder(password)
)
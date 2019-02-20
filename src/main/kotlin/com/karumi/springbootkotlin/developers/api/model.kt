package com.karumi.springbootkotlin.developers.api

import com.karumi.springbootkotlin.developers.domain.Developer

data class DeveloperBody(
  val id: String,
  val username: String,
  val email: String?
)

fun Developer.toBody(): DeveloperBody = DeveloperBody(id.toString(), username, email)
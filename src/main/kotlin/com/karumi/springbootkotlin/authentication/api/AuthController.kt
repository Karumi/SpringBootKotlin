package com.karumi.springbootkotlin.authentication.api

import com.karumi.springbootkotlin.authentication.domain.usecase.LoginDeveloper
import com.karumi.springbootkotlin.authentication.domain.usecase.RegisterDeveloper
import com.karumi.springbootkotlin.common.orThrow
import com.karumi.springbootkotlin.developers.api.DeveloperBody
import com.karumi.springbootkotlin.developers.api.toBody
import com.karumi.springbootkotlin.developers.domain.PasswordEncoder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
  private val registerDeveloper: RegisterDeveloper,
  private val loginDeveloper: LoginDeveloper,
  private val passwordEncoder: PasswordEncoder
) {

  @PostMapping("/register")
  fun register(@RequestBody request: NewDeveloperRequest): ResponseEntity<DeveloperBody> {
    val user = request.toDomain(passwordEncoder)
    return registerDeveloper(user)
      .map { ResponseEntity(it.toBody(), HttpStatus.CREATED) }
      .orThrow()
  }

  @PostMapping("/login")
  fun login(@RequestBody request: LoginRequest): String {
    val userPass = UsernamePasswordAuthenticationToken(request.username, request.password)
    return loginDeveloper(userPass).orThrow()
  }
}
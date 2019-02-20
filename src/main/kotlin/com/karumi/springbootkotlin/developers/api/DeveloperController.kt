package com.karumi.springbootkotlin.developers.api

import com.karumi.springbootkotlin.authentication.api.NewDeveloperRequest
import com.karumi.springbootkotlin.authentication.api.toDomain
import com.karumi.springbootkotlin.common.orThrow
import com.karumi.springbootkotlin.developers.domain.PasswordEncoder
import com.karumi.springbootkotlin.developers.domain.usecase.CreateKarumiDeveloper
import com.karumi.springbootkotlin.developers.domain.usecase.GetDeveloper
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class DeveloperController(
  private val createKarumiDeveloper: CreateKarumiDeveloper,
  private val getKarumiDeveloper: GetDeveloper,
  private val passwordEncoder: PasswordEncoder
) {

  @GetMapping("/")
  fun index(): String = "Karumi"

  @PostMapping("/developer")
  fun createDeveloper(
    @RequestBody developer: NewDeveloperRequest,
    authentication: Authentication
  ): ResponseEntity<DeveloperBody> =
    createKarumiDeveloper(developer.toDomain(passwordEncoder))
      .map { ResponseEntity(it.toBody(), CREATED) }
      .orThrow()

  @GetMapping("/developer/{developerId}")
  fun getDeveloper(@PathVariable developerId: UUID, authentication: Authentication): DeveloperBody =
    getKarumiDeveloper(developerId)
      .map { it.toBody() }
      .orThrow()
}
package com.karumi.springbootkotlin.controllers

import com.karumi.springbootkotlin.common.orThrow
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.usecase.CreateKarumiDeveloper
import com.karumi.springbootkotlin.developers.domain.usecase.GetDeveloper
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class DeveloperController(
  private val createKarumiDeveloper: CreateKarumiDeveloper,
  private val getKarumiDeveloper: GetDeveloper
) {

  @GetMapping("/")
  fun index(): String = "Karumi"

  @PostMapping("/developer")
  fun createDeveloper(@RequestBody developer: Developer): ResponseEntity<Developer> =
    createKarumiDeveloper(developer)
      .orThrow()
      .let { ResponseEntity(it, CREATED) }

  @GetMapping("/developer/{developerId}")
  fun getDeveloper(@PathVariable developerId: UUID): Developer =
    getKarumiDeveloper(developerId).orThrow()
}
package com.karumi.springbootkotlin.controllers

import arrow.core.getOrElse
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.given.givenDeveloper
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeveloperControllerTest : StringSpec(), GivenDeveloper by givenDeveloper {

  @LocalServerPort
  var PORT: Int = 0

  val URL
    get() = "http://localhost:$PORT"

  @Autowired
  lateinit var dao: DeveloperDao

  @Autowired
  lateinit var restTemplate: TestRestTemplate

  override fun listeners() = listOf(SpringListener)

  init {
    "developer POST should create a developer if it's a karumi developer" {
      val newDeveloper = givenNewKarumiDeveloper()

      val result = postDeveloper(newDeveloper)
      val createdDeveloper = result.body!!
      val obtainedDeveloper = getById(createdDeveloper.id)

      createdDeveloper.username shouldBe newDeveloper.username
      createdDeveloper.email shouldBe newDeveloper.email
      obtainedDeveloper shouldBe createdDeveloper
      result.statusCode shouldBe CREATED
    }

    "developer POST shouldn't create a developer if it isn't a karumi developer" {
      val newDeveloper = givenNewDeveloper()

      val result = post(newDeveloper)

      result.statusCode shouldBe BAD_REQUEST
    }

    "developer POST should returns 400 if the json body isn't expected" {
      val result = post(InvalidModel())

      result.statusCode shouldBe BAD_REQUEST
    }

    "developer GET should retrieve by id" {
      val developer = givenDeveloper().let { create(it) }

      val result = getDeveloper(developer)

      result.body shouldBe developer
      result.statusCode shouldBe OK
    }

    "developer GET should returns 404 code if there isn't the developer in the database" {
      val developer = givenDeveloper()

      val result = get(developer)

      result.statusCode shouldBe NOT_FOUND
    }
  }

  private fun postDeveloper(developer: Developer): ResponseEntity<Developer> =
    restTemplate.postForEntity("$URL/developer", developer, Developer::class.java)

  private inline fun <reified A> post(value: A): ResponseEntity<*> =
    restTemplate.postForEntity("$URL/developer", value, Any::class.java)

  private fun getDeveloper(developer: Developer): ResponseEntity<Developer> =
    restTemplate.getForEntity("$URL/developer/${developer.id}", Developer::class.java)

  private fun get(developer: Developer): ResponseEntity<*> =
    restTemplate.getForEntity("$URL/developer/${developer.id}", Any::class.java)

  private data class InvalidModel(val invalid: String = "")

  private fun getById(id: UUID) = dao.getById(id).getOrElse { null }?.getOrElse { null }!!
  private fun create(developer: Developer) = dao.create(developer).getOrElse { null }!!
}
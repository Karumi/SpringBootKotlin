package com.karumi.springbootkotlin.controllers

import arrow.core.getOrElse
import com.karumi.springbootkotlin.authentication.api.NewDeveloperRequest
import com.karumi.springbootkotlin.developers.api.DeveloperBody
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.developers.storage.DeveloperRepository
import com.karumi.springbootkotlin.getAuthorized
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.postAuthorized
import com.karumi.springbootkotlin.uuid
import io.kotlintest.provided.CleanDatabaseListener
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeveloperControllerTest @Autowired constructor(
  private val dao: DeveloperDao,
  private val restTemplate: TestRestTemplate,
  private val repository: DeveloperRepository
) : StringSpec(), GivenDeveloper {

  @LocalServerPort
  var PORT: Int = 0

  val URL
    get() = "http://localhost:$PORT"

  override fun listeners() = listOf(SpringListener, CleanDatabaseListener(repository))

  init {

    "developer POST should create a developer if it's a karumi developer" {
      val newDeveloper = NEW_KARUMI_DEVELOPER
      val result = withSession().postDeveloper(newDeveloper)
      val createdDeveloper = result.body!!
      val obtainedDeveloper = getById(createdDeveloper.id.uuid())

      result.statusCode shouldBe CREATED
      createdDeveloper.username shouldBe newDeveloper.username
      createdDeveloper.email shouldBe newDeveloper.email
      obtainedDeveloper.username shouldBe newDeveloper.username
      obtainedDeveloper.email shouldBe newDeveloper.email
    }

    "developer POST should returns 400 if it isn't a karumi developer" {
      val result = withSession().post(ANY_NEW_DEVELOPER)

      result.statusCode shouldBe BAD_REQUEST
    }

    "developer POST should returns 400 if the json body isn't expected" {
      val result = withSession().post(InvalidModel())

      result.statusCode shouldBe BAD_REQUEST
    }

    "developer POST should returns 401 if doesn't have authentication token" {
      val result = restTemplate.post(ANY_NEW_DEVELOPER)

      result.statusCode shouldBe UNAUTHORIZED
    }

    "developer GET should retrieve by id" {
      val developer = create(KARUMI_DEVELOPER)

      val result = withSession().getDeveloper(developer)

      result.statusCode shouldBe OK
      result.body?.username shouldBe developer.username
      result.body?.email shouldBe developer.email
    }

    "developer GET should returns 404 if there isn't the developer in the database" {
      val result = withSession().get(DEVELOPER_ID)

      result.statusCode shouldBe NOT_FOUND
    }

    "developer GET should returns 401 if doesn't have authentication token" {
      val result = restTemplate.get(DEVELOPER_ID)

      result.statusCode shouldBe UNAUTHORIZED
    }
  }

  private fun TestRestTemplate.postDeveloper(
    developer: NewDeveloperRequest
  ): ResponseEntity<DeveloperBody> = postAuthorized("$URL/developer", developer)

  private fun <A> TestRestTemplate.post(value: A): ResponseEntity<String> =
    postAuthorized("$URL/developer", value)

  private fun TestRestTemplate.getDeveloper(developer: Developer): ResponseEntity<DeveloperBody> =
    getAuthorized("$URL/developer/${developer.id}")

  private fun TestRestTemplate.get(id: UUID): ResponseEntity<String> =
    getAuthorized("$URL/developer/$id")

  private data class InvalidModel(val invalid: String = "")

  private fun withSession(): TestRestTemplate {
    create(SESSION_DEVELOPER)
    return restTemplate
  }

  private fun getById(id: UUID) = dao.getById(id).getOrElse { null }?.getOrElse { null }!!
  private fun create(developer: Developer) = dao.create(developer).getOrElse { null }!!
}
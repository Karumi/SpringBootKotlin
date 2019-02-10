package com.karumi.springbootkotlin.developers.storage

import arrow.core.getOrElse
import arrow.core.success
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.given.givenDeveloper
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeveloperDaoTest : StringSpec(), GivenDeveloper by givenDeveloper {

  override fun listeners() = listOf(SpringListener)

  @Autowired
  lateinit var dao: DeveloperDao

  init {
    "developer should be updated" {
      val developer = save(givenDeveloper())

      val developerUpdate = developer.copy(username = "Pedro")
      val updatedDeveloper = dao.update(developerUpdate)

      updatedDeveloper shouldBe developerUpdate.success()
      find(developer) shouldBe developerUpdate.success()
    }
  }

  private fun save(developer: Developer) =
      dao.create(developer).getOrElse { null }!!

  private fun find(developer: Developer) =
      dao.getById(developer.id).map { it.getOrElse { null } }
}
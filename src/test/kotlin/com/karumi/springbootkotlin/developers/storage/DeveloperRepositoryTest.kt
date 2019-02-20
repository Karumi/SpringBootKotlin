package com.karumi.springbootkotlin.developers.storage

import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.given.GivenDeveloper.Companion.KARUMI_DEVELOPER
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.UUID

@DataJpaTest
class DeveloperRepositoryTest(
  val repository: DeveloperRepository
) : StringSpec(), GivenDeveloper {

  override fun listeners() = listOf(SpringListener)

  init {
    "developer should be updated" {
      val developer = save(KARUMI_DEVELOPER)

      val developerUpdate = developer.copy(username = "Pedro")
      val updatedDeveloper = repository.save(developerUpdate)

      updatedDeveloper shouldBe developerUpdate
      find(developer.id) shouldBe developerUpdate
    }
  }

  private fun save(developer: Developer) =
    repository.save(developer.toEntity())

  private fun find(id: UUID?) =
    repository.findById(id!!).get()
}
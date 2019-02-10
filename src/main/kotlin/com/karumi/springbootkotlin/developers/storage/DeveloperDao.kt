package com.karumi.springbootkotlin.developers.storage

import arrow.core.Option
import arrow.core.Try
import arrow.core.toOption
import com.karumi.springbootkotlin.common.TryLogger
import com.karumi.springbootkotlin.developers.domain.Developer
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
interface DeveloperRepository: PagingAndSortingRepository<Developer, UUID>

@Component
class DeveloperDao(private val developerRepository: DeveloperRepository) {

  fun create(developer: Developer): Try<Developer> = TryLogger(this::class) {
    developerRepository.save(developer)
  }

  fun update(developer: Developer): Try<Developer> = TryLogger(this::class) {
    developerRepository.save(developer)
  }

  fun getById(developerId: UUID): Try<Option<Developer>> = TryLogger(this::class) {
    developerRepository.findById(developerId)
        .orElse(null)
        .toOption()
  }

}

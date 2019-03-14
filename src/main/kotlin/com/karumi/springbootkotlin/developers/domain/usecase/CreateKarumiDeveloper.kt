package com.karumi.springbootkotlin.developers.domain.usecase

import arrow.core.Try
import arrow.core.failure
import arrow.core.success
import com.karumi.springbootkotlin.common.mapException
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.DeveloperValidator
import com.karumi.springbootkotlin.developers.domain.NotKarumier
import com.karumi.springbootkotlin.developers.domain.StorageError
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import org.springframework.stereotype.Component

@Component
class CreateKarumiDeveloper(
  private val developerDao: DeveloperDao
) {

  operator fun invoke(developer: Developer): Try<Developer> =
    validKarumiDeveloper(developer)
      .flatMap {
        developerDao.create(it)
          .mapException { StorageError }
      }

  private fun validKarumiDeveloper(developer: Developer): Try<Developer> =
    if (DeveloperValidator.isKarumiDeveloper(developer)) {
      developer.success()
    } else {
      NotKarumier.failure()
    }
}
package com.karumi.springbootkotlin.authentication.domain.usecase

import arrow.core.Option
import arrow.core.Try
import arrow.core.failure
import com.karumi.springbootkotlin.developers.domain.AlreadyRegistered
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import org.springframework.stereotype.Component

@Component
class RegisterDeveloper(
  private val developerDao: DeveloperDao
) {

  operator fun invoke(developer: Developer): Try<Developer> =
    developerDao.getByUsername(developer.username)
      .flatMap(createIfNotRegistered(developer))

  private fun createIfNotRegistered(developer: Developer): (Option<Developer>) -> Try<Developer> = {
    it.fold({
      developerDao.create(developer)
    }, {
      AlreadyRegistered.failure()
    })
  }
}
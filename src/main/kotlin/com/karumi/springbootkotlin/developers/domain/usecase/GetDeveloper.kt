package com.karumi.springbootkotlin.developers.domain.usecase

import arrow.core.Try
import com.karumi.springbootkotlin.common.mapException
import com.karumi.springbootkotlin.common.toTry
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.NotFound
import com.karumi.springbootkotlin.developers.domain.StorageError
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GetDeveloper(
  private val developerDao: DeveloperDao
) {

  operator fun invoke(developerId: UUID): Try<Developer> =
    developerDao.getById(developerId)
      .mapException { StorageError }
      .flatMap { devOption -> devOption.toTry { NotFound } }
}
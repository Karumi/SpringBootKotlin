package io.kotlintest.provided

import com.karumi.springbootkotlin.developers.storage.DeveloperRepository
import io.kotlintest.AbstractProjectConfig
import io.kotlintest.TestCase
import io.kotlintest.extensions.ProjectLevelExtension
import io.kotlintest.extensions.TestListener
import io.kotlintest.spring.SpringAutowireConstructorExtension

class ProjectConfig : AbstractProjectConfig() {
  override fun extensions(): List<ProjectLevelExtension> =
    listOf(SpringAutowireConstructorExtension)
}

class CleanDatabaseListener(private val developerRepository: DeveloperRepository) : TestListener {
  override fun beforeTest(testCase: TestCase) {
    developerRepository.deleteAll()
  }
}
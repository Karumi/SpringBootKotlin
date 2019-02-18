package com.karumi.springbootkotlin.given

import com.karumi.springbootkotlin.developers.domain.Developer
import java.util.UUID

interface GivenDeveloper {
  companion object {
    val DEVELOPER_ID = UUID.fromString("c04ecc99-d7ba-460c-be78-6995805c5175")!!
  }

  fun givenNewDeveloper(): Developer = Developer(
    username = "Unknown",
    email = "email@email.com"
  )

  fun givenNewKarumiDeveloper(): Developer = Developer(
    username = "Unknown",
    email = "email@karumi.com"
  )

  fun givenDeveloper(): Developer = Developer(
    username = "Unknown",
    email = null
  )
}
package com.karumi.springbootkotlin.given

import com.karumi.springbootkotlin.developers.domain.Developer

interface GivenDeveloper {
  fun givenDeveloper(): Developer
  fun givenNewDeveloper(): Developer
  fun givenNewKarumiDeveloper(): Developer
}

val givenDeveloper = GivenDummyDevelopers()

class GivenDummyDevelopers : GivenDeveloper {

  override fun givenNewDeveloper(): Developer = Developer(
      username = "Unknown",
      email = "email@email.com"
  )

  override fun givenNewKarumiDeveloper(): Developer = Developer(
      username = "Unknown",
      email = "email@karumi.com"
  )

  override fun givenDeveloper(): Developer = Developer(
      username = "Unknown",
      email = null
  )
}
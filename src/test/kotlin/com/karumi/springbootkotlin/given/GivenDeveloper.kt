package com.karumi.springbootkotlin.given

import com.karumi.springbootkotlin.authentication.api.NewDeveloperRequest
import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.EncodedPassword
import com.karumi.springbootkotlin.uuid

interface GivenDeveloper {
  val DEVELOPER_ID
    get() = "c04ecc99-d7ba-460c-be78-6995805c5175".uuid()
  val SESSION_DEVELOPER
    get() = Developer(
      id = "c04ecc99-d7ba-460c-be78-6995805c5177".uuid(),
      username = "Test",
      email = "test@karumi.com",
      password = EncodedPassword("1234")
    )
  val NEW_KARUMI_DEVELOPER
    get() = NewDeveloperRequest(
      username = "Unknown",
      email = "email@karumi.com",
      password = "1234"
    )
  val KARUMI_DEVELOPER
    get() = Developer(
      id = DEVELOPER_ID,
      username = "Unknown",
      email = "email@karumi.com",
      password = EncodedPassword("1234")
    )

  val ANY_NEW_DEVELOPER
    get() = NewDeveloperRequest(
      username = "Unknown",
      email = "email@email.com",
      password = "1234"
    )
}
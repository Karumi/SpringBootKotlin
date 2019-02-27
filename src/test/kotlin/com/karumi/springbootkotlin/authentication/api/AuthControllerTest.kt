package com.karumi.springbootkotlin.authentication.api

import com.karumi.springbootkotlin.developers.api.DeveloperBody
import com.karumi.springbootkotlin.developers.storage.DeveloperRepository
import com.karumi.springbootkotlin.developers.storage.toEntity
import com.karumi.springbootkotlin.getDeveloper
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.security.TokenHelper
import com.karumi.springbootkotlin.withContent
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.provided.CleanDatabaseListener
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest(
  @Autowired val mockMvc: MockMvc,
  @Autowired val tokenHelper: TokenHelper,
  @Autowired val repository: DeveloperRepository
) : StringSpec(), GivenDeveloper {

  override fun listeners() = listOf(SpringListener, CleanDatabaseListener(repository))

  init {
    "POST /auth/register should register a developer" {
      val developer: DeveloperBody = mockMvc.perform(
        MockMvcRequestBuilders.post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON)
          .withContent("CreateKarumiDeveloper.json")
      ).andExpect(MockMvcResultMatchers.status().isCreated)
        .andDo(print())
        .getDeveloper()

      developer.username shouldBe KARUMI_DEVELOPER.username
      developer.email shouldBe KARUMI_DEVELOPER.email
    }

    "POST /auth/login should returns a valid token" {
      repository.save(KARUMI_DEVELOPER.toEntity())

      val jwtToken = mockMvc.perform(
        MockMvcRequestBuilders.post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .withContent("LoginRequest.json")
      ).andExpect(MockMvcResultMatchers.status().isOk)
        .andDo(print())
        .andReturn().response.contentAsString

      tokenHelper.isValidToken(jwtToken).shouldBeTrue()
    }
  }
}
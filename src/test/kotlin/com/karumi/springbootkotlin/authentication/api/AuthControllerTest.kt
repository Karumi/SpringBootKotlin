package com.karumi.springbootkotlin.authentication.api

import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.matchWithSnapshot
import com.karumi.springbootkotlin.withContent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest(
  @Autowired val mockMvc: MockMvc,
  @Autowired val dao: DeveloperDao
) {

  @Test
  fun `should register a developer`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/register")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("CreateKarumiDeveloper.json")
    ).andExpect(MockMvcResultMatchers.status().isOk)
      .andDo(MockMvcResultHandlers.print())
      .matchWithSnapshot()
  }

  @Test
  fun `should register a new developer`() {
    mockMvc.perform(
      MockMvcRequestBuilders.post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("LoginRequest.json")
    ).andExpect(MockMvcResultMatchers.status().isOk)
      .andDo(MockMvcResultHandlers.print())
      .matchWithSnapshot()
  }
}
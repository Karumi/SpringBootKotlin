package com.karumi.springbootkotlin.controllers

import arrow.core.None
import arrow.core.some
import arrow.core.success
import com.karumi.springbootkotlin.WithMockCustomUser
import com.karumi.springbootkotlin.andExpectContent
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.withContent
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class DeveloperControllerTestMockUser(
  @Autowired private val mockMvc: MockMvc
) : GivenDeveloper {

  @MockkBean
  lateinit var developerDao: DeveloperDao

  @Test
  @WithMockCustomUser
  fun `developer POST should create a developer if it's a karumi developer`() {
    every { developerDao.create(any()) } returns KARUMI_DEVELOPER.success()
    every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

    mockMvc.perform(
      MockMvcRequestBuilders.post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("CreateKarumiDeveloper.json")
    ).andExpect(MockMvcResultMatchers.status().isCreated)
      .andExpectContent("ExpectedNewDeveloper.json")
      .andDo(print())
  }

  @Test
  @WithMockCustomUser
  fun `developer POST should returns 400 if it isn't a karumi developer `() {
    every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

    mockMvc.perform(
      MockMvcRequestBuilders.post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("CreateDeveloper.json")
    ).andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andDo(print())
  }

  @Test
  @WithMockCustomUser
  fun `developer POST should returns 400 if the json body isn't expected`() {
    every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

    mockMvc.perform(
      MockMvcRequestBuilders.post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("BadDeveloperBody.json")
    ).andExpect(MockMvcResultMatchers.status().isBadRequest)
      .andDo(print())
  }

  @Test
  fun `developer POST should returns 401 if doesn't have authentication token`() {
    every { developerDao.getByUsername(any()) } returns None.success()

    mockMvc.perform(
      MockMvcRequestBuilders.post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .withContent("CreateKarumiDeveloper.json")
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
      .andDo(print())
  }

  @Test
  @WithMockCustomUser
  fun `developer GET should retrieve by id`() {
    every { developerDao.getById(DEVELOPER_ID) } returns KARUMI_DEVELOPER.some().success()
    every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

    mockMvc.perform(
      MockMvcRequestBuilders.get("/developer/$DEVELOPER_ID")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isOk)
      .andExpectContent("ExpectedNewDeveloper.json")
      .andDo(print())
  }

  @Test
  @WithMockCustomUser
  fun `developer GET should returns 404 if there isn't the developer in the database`() {
    every { developerDao.getById(DEVELOPER_ID) } returns None.success()
    every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

    mockMvc.perform(
      MockMvcRequestBuilders.get("/developer/$DEVELOPER_ID")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isNotFound)
      .andDo(print())
  }

  @Test
  fun `developer GET should returns 401 if doesn't have authentication token`() {
    mockMvc.perform(
      MockMvcRequestBuilders.get("/developer/$DEVELOPER_ID")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isUnauthorized)
      .andDo(print())
  }
}
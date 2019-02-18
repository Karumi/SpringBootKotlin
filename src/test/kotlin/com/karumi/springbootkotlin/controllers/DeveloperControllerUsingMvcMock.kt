package com.karumi.springbootkotlin.controllers

import arrow.core.None
import arrow.core.some
import arrow.core.success
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.given.GivenDeveloper.Companion.DEVELOPER_ID
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.file.Files

@Configuration
@ComponentScan("com.karumi.springbootkotlin")
class Config

@RunWith(SpringRunner::class)
@WebMvcTest(DeveloperController::class)
@ContextConfiguration(classes = arrayOf(Config::class))
class DeveloperControllerUsingMvcMock : GivenDeveloper {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockkBean
  private lateinit var developerDao: DeveloperDao

  @Test
  fun `developer POST should create a developer if it's a karumi developer`() {
    val createdDeveloper = givenNewKarumiDeveloper().copy(id = GivenDeveloper.DEVELOPER_ID)

    every { developerDao.create(any()) } returns createdDeveloper.success()

    mockMvc.perform(
      post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(getContent("CreateKarumiDeveloper.json"))
    ).andExpect(status().isCreated)
      .andDo(print())
      .andReturn()
      .matchWithSnapshot()
  }

  @Test
  fun `developer POST shouldn't create a developer if it isn't a karumi developer`() {
    mockMvc.perform(
      post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(getContent("CreateDeveloper.json"))
    ).andExpect(status().isBadRequest)
      .andDo(print())
      .andReturn()
      .matchWithSnapshot()
  }

  @Test
  fun `eveloper POST should returns 400 if the json body isn't expected`() {
    mockMvc.perform(
      post("/developer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(getContent("BadDeveloperBody.json"))
    ).andExpect(status().isBadRequest)
      .andDo(print())
      .andReturn()
  }

  @Test
  fun `developer GET should retrieve by id`() {
    val createdDeveloper = givenNewKarumiDeveloper().copy(id = GivenDeveloper.DEVELOPER_ID)
    every { developerDao.getById(DEVELOPER_ID) } returns createdDeveloper.some().success()

    mockMvc.perform(
      get("/developer/$DEVELOPER_ID")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk)
      .andDo(print())
      .andReturn()
      .matchWithSnapshot()
  }

  @Test
  fun `developer GET should returns 404 code if there isn't the developer in the database`() {
    every { developerDao.getById(DEVELOPER_ID) } returns None.success()

    mockMvc.perform(
      get("/developer/${GivenDeveloper.DEVELOPER_ID}")
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isNotFound)
      .andDo(print())
      .andReturn()
  }

  fun getContent(fileName: String): String {
    val resource = ClassPathResource(fileName).file
    return String(Files.readAllBytes(resource.toPath()))
  }

  fun MvcResult.matchWithSnapshot() {
    response.contentAsString.matchWithSnapshot()
  }
}
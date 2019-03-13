package com.karumi.springbootkotlin.controllers

import arrow.core.None
import arrow.core.some
import arrow.core.success
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.matchWithSnapshot
import com.karumi.springbootkotlin.withAuthorization
import com.karumi.springbootkotlin.withContent
import com.ninjasquad.springmockk.MockkBean
import io.kotlintest.extensions.TestListener
import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringListener
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class DeveloperControllerUsingMvcMock(
  private val mockMvc: MockMvc,
  @MockkBean val developerDao: DeveloperDao
) : WordSpec(), GivenDeveloper {

  override fun listeners(): List<TestListener> = listOf(SpringListener)

  init {
    "POST /developer" should {
      "create a developer if it's a karumi developer" {
        every { developerDao.create(any()) } returns KARUMI_DEVELOPER.success()
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .withContent("CreateKarumiDeveloper.json")
            .withAuthorization()
        ).andExpect(status().isCreated)
          .andDo(print())
          .matchWithSnapshot(this)
      }

      "returns 400 if it isn't a karumi developer" {
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .withContent("CreateDeveloper.json")
            .withAuthorization()
        ).andExpect(status().isBadRequest)
          .andDo(print())
      }

      "returns 400 if the json body isn't expected" {
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .withContent("BadDeveloperBody.json")
            .withAuthorization()
        ).andExpect(status().isBadRequest)
          .andDo(print())
      }

      "developer POST should returns 401 if doesn't have authentication token" {
        every { developerDao.getByUsername(any()) } returns None.success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .withContent("CreateKarumiDeveloper.json")
        ).andExpect(status().isUnauthorized)
          .andDo(print())
      }
    }

    "GET /developer/{id}" should {
      "retrieve by id" {
        every { developerDao.getById(DEVELOPER_ID) } returns KARUMI_DEVELOPER.some().success()
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          get("/developer/$DEVELOPER_ID")
            .contentType(MediaType.APPLICATION_JSON)
            .withAuthorization()
        ).andExpect(status().isOk)
          .andDo(print())
          .matchWithSnapshot(this)
      }

      "returns 404 code if there isn't the developer in the database" {
        every { developerDao.getById(DEVELOPER_ID) } returns None.success()
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          get("/developer/$DEVELOPER_ID")
            .contentType(MediaType.APPLICATION_JSON)
            .withAuthorization()
        ).andExpect(status().isNotFound)
          .andDo(print())
      }

      "developer GET should returns 401 if doesn't have authentication token" {
        mockMvc.perform(
          get("/developer/$DEVELOPER_ID")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
          .andDo(print())
      }
    }
  }
}
package com.karumi.springbootkotlin.controllers

import arrow.core.None
import arrow.core.some
import arrow.core.success
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import com.karumi.springbootkotlin.given.GivenDeveloper
import com.karumi.springbootkotlin.given.GivenDeveloper.Companion.DEVELOPER_ID
import com.karumi.springbootkotlin.given.GivenDeveloper.Companion.KARUMI_DEVELOPER
import com.karumi.springbootkotlin.given.GivenDeveloper.Companion.SESSION_DEVELOPER
import com.karumi.springbootkotlin.matchWithSnapshot
import com.karumi.springbootkotlin.withAuthorization
import com.ninjasquad.springmockk.MockkBean
import io.kotlintest.Spec
import io.kotlintest.specs.WordSpec
import io.mockk.every
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.file.Files

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class DeveloperControllerUsingMvcMock : WordSpec(), GivenDeveloper {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockkBean
  private lateinit var developerDao: DeveloperDao

  // Use this until the next release 3.3.0: https://github.com/kotlintest/kotlintest/pull/684
  override fun beforeSpec(spec: Spec) {
    TestContextManager(this.javaClass).prepareTestInstance(spec)
  }

  init {
    "POST /developer" should {
      "create a developer if it's a karumi developer" {
        every { developerDao.create(any()) } returns KARUMI_DEVELOPER.success()
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getContent("CreateKarumiDeveloper.json"))
            .withAuthorization()
        ).andExpect(status().isCreated)
          .andDo(print())
          .andReturn()
          .matchWithSnapshot(this)
      }

      "not create a developer if it isn't a karumi developer" {
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getContent("CreateDeveloper.json"))
            .withAuthorization()
        ).andExpect(status().isBadRequest)
          .andDo(print())
          .andReturn()
      }

      "returns 400 if the json body isn't expected" {
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getContent("BadDeveloperBody.json"))
            .withAuthorization()
        ).andExpect(status().isBadRequest)
          .andDo(print())
          .andReturn()
      }

      "developer POST should returns 401 if doesn't have authentication token" {
        every { developerDao.getByUsername(any()) } returns None.success()

        mockMvc.perform(
          post("/developer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getContent("CreateKarumiDeveloper.json"))
        ).andExpect(status().isUnauthorized)
          .andDo(print())
          .andReturn()
          .matchWithSnapshot(this)
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
          .andReturn()
          .matchWithSnapshot(this)
      }

      "returns 404 code if there isn't the developer in the database" {
        every { developerDao.getById(DEVELOPER_ID) } returns None.success()
        every { developerDao.getByUsername(any()) } returns SESSION_DEVELOPER.some().success()

        mockMvc.perform(
          get("/developer/${GivenDeveloper.DEVELOPER_ID}")
            .contentType(MediaType.APPLICATION_JSON)
            .withAuthorization()
        ).andExpect(status().isNotFound)
          .andDo(print())
          .andReturn()
      }

      "developer GET should returns 401 if doesn't have authentication token" {
        mockMvc.perform(
          get("/developer/$DEVELOPER_ID")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized)
          .andDo(print())
          .andReturn()
          .matchWithSnapshot(this)
      }
    }
  }

  fun getContent(fileName: String): String {
    val resource = ClassPathResource(fileName).file
    return String(Files.readAllBytes(resource.toPath()))
  }
}
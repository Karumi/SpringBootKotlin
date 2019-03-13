package com.karumi.springbootkotlin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.karumi.springbootkotlin.developers.api.DeveloperBody
import com.karumi.springbootkotlin.security.TokenHelper
import io.kotlintest.TestContext
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import java.nio.file.Files
import java.util.UUID

inline fun <reified A, B> TestRestTemplate.postAuthorized(url: String, body: B): ResponseEntity<A> =
  withAuthorization(url, POST, body)

inline fun <reified A> TestRestTemplate.getAuthorized(url: String): ResponseEntity<A> =
  withAuthorization<A, Any>(url, GET)

inline fun <reified A, B> TestRestTemplate.withAuthorization(
  url: String,
  method: HttpMethod,
  body: B? = null
): ResponseEntity<A> {
  val headers = HttpHeaders()
  headers.set(AUTHORIZATION, "Bearer ${getTokenForTestUser()}")
  val entity = HttpEntity(body, headers)
  return this.exchange(url, method, entity, A::class.java)
}

fun MockHttpServletRequestBuilder.withAuthorization(): MockHttpServletRequestBuilder =
  header(AUTHORIZATION, "Bearer ${getTokenForTestUser()}")

fun ResultActions.andExpectContent(fileName: String): ResultActions =
  andExpect(content().json(getContent(fileName)))

fun MockHttpServletRequestBuilder.withContent(fileName: String): MockHttpServletRequestBuilder {
  val content = getContent(fileName)
  content(content)
  return this
}

private fun getContent(fileName: String): String {
  val resource = ClassPathResource(fileName).file
  return String(Files.readAllBytes(resource.toPath()))
}

fun getTokenForTestUser(): String =
  TokenHelper().generateToken("Test")

fun ResultActions.getDeveloper(): DeveloperBody {
  val mapper = jacksonObjectMapper()
  return mapper.readValue(andReturn().response.contentAsString, DeveloperBody::class.java)
}

fun ResultActions.matchWithSnapshot(context: TestContext? = null) {
  andReturn().response.contentAsString.matchWithSnapshot(context?.description()?.name)
}

fun ResponseEntity<*>.matchWithSnapshot(context: TestContext) {
  this.body!!.matchWithSnapshot(context.description().name)
}

fun String.uuid(): UUID = UUID.fromString(this)
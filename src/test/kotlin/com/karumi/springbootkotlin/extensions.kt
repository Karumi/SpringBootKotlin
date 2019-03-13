package com.karumi.springbootkotlin

import com.karumi.kotlinsnapshot.matchWithSnapshot
import com.karumi.springbootkotlin.security.Token
import com.karumi.springbootkotlin.security.TokenHelper
import io.kotlintest.TestContext
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
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

fun getTokenForTestUser(): Token =
  TokenHelper().generateToken("Test")

fun MvcResult.matchWithSnapshot(context: TestContext) {
  response.contentAsString.matchWithSnapshot(context.description().name)
}

fun ResponseEntity<*>.matchWithSnapshot(context: TestContext) {
  this.body!!.matchWithSnapshot(context.description().name)
}

fun String.uuid(): UUID = UUID.fromString(this)
package com.karumi.springbootkotlin.security

import com.karumi.springbootkotlin.common.TryLogger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.Date
import java.util.concurrent.TimeUnit

inline class Token(val token: String) {
  override fun toString(): String = token
}

@Component
class
TokenHelper {

  companion object {
    private const val SECRET_TOKEN = "*top*secret*key"
    private val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512
    private val EXPIRES_IN_TEN_MINUTES = TimeUnit.MINUTES.toMillis(10)
  }

  fun generateToken(username: String): Token =
    Jwts.builder()
      .setSubject(username)
      .setIssuedAt(now())
      .setExpiration(getExpirationDate())
      .signWith(SIGNATURE_ALGORITHM, SECRET_TOKEN)
      .compact()
      .let { Token(it) }

  fun isValidToken(token: String): Boolean = TryLogger {
    Jwts.parser()
      .setSigningKey(SECRET_TOKEN)
      .parseClaimsJws(token)
  }.isSuccess()

  fun getUsernameFromToken(token: String): String =
    Jwts.parser()
      .setSigningKey(SECRET_TOKEN)
      .parseClaimsJws(token)
      .body
      .subject

  private fun now() = Date(System.currentTimeMillis())
  private fun getExpirationDate() = Date(now().time + EXPIRES_IN_TEN_MINUTES)
}
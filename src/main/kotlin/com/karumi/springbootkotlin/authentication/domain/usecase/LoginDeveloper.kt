package com.karumi.springbootkotlin.authentication.domain.usecase

import arrow.core.Try
import com.karumi.springbootkotlin.common.TryLogger
import com.karumi.springbootkotlin.security.SetAuthentication
import com.karumi.springbootkotlin.security.Token
import com.karumi.springbootkotlin.security.TokenHelper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class LoginDeveloper(
  private val authenticationManager: AuthenticationManager,
  private val tokenHelper: TokenHelper,
  private val setAuthentication: SetAuthentication
) {

  operator fun invoke(userPass: UsernamePasswordAuthenticationToken): Try<Token> = TryLogger {
    val authentication = authenticationManager.authenticate(userPass)
    setAuthentication(authentication)
    tokenHelper.generateToken(userPass.principal.toString())
  }
}
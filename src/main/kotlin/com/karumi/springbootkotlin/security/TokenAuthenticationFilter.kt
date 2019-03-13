package com.karumi.springbootkotlin.security

import com.karumi.springbootkotlin.common.TryLogger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(
  private val tokenHelper: TokenHelper,
  private val userDetailsService: CustomUserDetailsService,
  private val setAuthentication: SetAuthentication
) : OncePerRequestFilter() {

  companion object {
    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val BEARER = "Bearer "
  }

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain
  ) {
    TryLogger {
      val token = getJwtToken(request)

      if (token != null && tokenHelper.isValidToken(token)) {
        val username = tokenHelper.getUsernameFromToken(token)
        val userDetails = userDetailsService.loadUserByUsername(username)
        val authentication =
          UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        setAuthentication(authentication)
      }
    }

    filterChain.doFilter(request, response)
  }

  private fun getJwtToken(request: HttpServletRequest): String? =
    request.getHeader(AUTHORIZATION_HEADER)?.let { token ->
      if (token.startsWith(BEARER)) {
        token.substring(7)
      } else {
        null
      }
    }
}
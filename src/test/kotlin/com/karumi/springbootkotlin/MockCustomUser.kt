package com.karumi.springbootkotlin

import com.karumi.springbootkotlin.security.SecurityUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {
  override fun createSecurityContext(customUser: WithMockCustomUser): SecurityContext {
    val context = SecurityContextHolder.createEmptyContext()

    val principal = SecurityUser("1234", customUser.name, customUser.username)
    val auth = UsernamePasswordAuthenticationToken(principal, "1234", principal.authorities)
    context.authentication = auth
    return context
  }
}

@Retention(value = AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(val username: String = "Test", val name: String = "Sr. Test")
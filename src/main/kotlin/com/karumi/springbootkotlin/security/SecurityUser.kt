package com.karumi.springbootkotlin.security

import com.karumi.springbootkotlin.developers.domain.Developer
import com.karumi.springbootkotlin.developers.domain.DeveloperValidator
import com.karumi.springbootkotlin.developers.domain.EncodedPassword
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SecurityUser(
  val id: String,
  private val username: String,
  private val password: EncodedPassword,
  private val authorities: List<GrantedAuthority> = emptyList()
) : UserDetails {
  override fun getAuthorities(): Collection<GrantedAuthority> = authorities

  override fun isEnabled(): Boolean = true

  override fun getUsername(): String = username

  override fun isCredentialsNonExpired(): Boolean = true

  override fun getPassword(): String = password.password

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true
}

fun Developer.toSecurityUser(): SecurityUser = SecurityUser(
  id.toString(), username, password, getRole()
)

private fun Developer.getRole() = listOf(
  if (DeveloperValidator.isKarumiDeveloper(this)) {
    GrantedAuthority { "ROLE_KARUMIER" }
  } else {
    GrantedAuthority { "ROLE_USER" }
  }
)
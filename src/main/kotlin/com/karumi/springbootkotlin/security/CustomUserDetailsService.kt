package com.karumi.springbootkotlin.security

import com.karumi.springbootkotlin.common.orThrow
import com.karumi.springbootkotlin.common.toTry
import com.karumi.springbootkotlin.developers.storage.DeveloperDao
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
  private val developerDao: DeveloperDao
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails =
    developerDao.getByUsername(username)
      .flatMap { it.toTry { UsernameNotFoundException("User $username not found") } }
      .orThrow()
      .toSecurityUser()
}
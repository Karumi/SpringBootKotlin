package com.karumi.springbootkotlin.security

import com.karumi.springbootkotlin.developers.domain.EncodedPassword
import com.karumi.springbootkotlin.developers.domain.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

typealias SetAuthentication = (Authentication) -> Unit

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
  private val userDetailsService: CustomUserDetailsService,
  private val tokenHelper: TokenHelper
) : WebSecurityConfigurerAdapter() {

  companion object {
    private val CRYPT_ENCODER = BCryptPasswordEncoder()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder = { password ->
    EncodedPassword(CRYPT_ENCODER.encode(password))
  }

  @Bean
  fun authenticationContext(): SetAuthentication = {
    SecurityContextHolder.getContext().authentication = it
  }

  @Bean(name = [(BeanIds.AUTHENTICATION_MANAGER)])
  override fun authenticationManagerBean(): AuthenticationManager {
    return super.authenticationManagerBean()
  }

  @Autowired
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(CRYPT_ENCODER)
  }

  override fun configure(http: HttpSecurity) {
    http
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/auth/**").permitAll()
      .antMatchers("/v2/api-docs").permitAll()
      .antMatchers("/swagger-resources/**").permitAll()
      .antMatchers("/swagger-ui.html**").permitAll()
      .antMatchers("/webjars/**").permitAll()
      .antMatchers("/h2-console/**").permitAll()
      .anyRequest().authenticated()
      .and()
      .headers().frameOptions().sameOrigin()
      .and()
      .addFilterBefore(
        TokenAuthenticationFilter(tokenHelper, userDetailsService, authenticationContext()),
        UsernamePasswordAuthenticationFilter::class.java
      )
      .csrf().disable()
      .formLogin().disable()
      .httpBasic().disable()
      .exceptionHandling()
      .authenticationEntryPoint(JwtAuthenticationEntryPoint())
  }
}
package com.karumi.admin.server

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@SpringBootApplication
@EnableAdminServer
class AdminServerApplication

fun main(args: Array<String>) {
  runApplication<AdminServerApplication>(*args)
}

@Configuration
class ActuatorSecurityConfig : WebSecurityConfigurerAdapter() {

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
    successHandler.setTargetUrlParameter("redirectTo")
    successHandler.setDefaultTargetUrl("/")

    http.authorizeRequests()
        .antMatchers("/assets/**").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login").successHandler(successHandler).and()
        .logout().logoutUrl("/logout").and()
        .httpBasic().and()
        .csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .ignoringAntMatchers(
            "/instances",
            "/actuator/**"
        )
  }
}
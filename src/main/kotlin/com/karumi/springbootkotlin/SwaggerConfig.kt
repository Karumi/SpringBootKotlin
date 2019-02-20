package com.karumi.springbootkotlin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.Authentication
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle.HEADER
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

  companion object {
    private const val API_KEY_NAME = "Token Access"
  }

  @Bean
  fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
    .ignoredParameterTypes(Authentication::class.java)
    .select()
    .apis(RequestHandlerSelectors.basePackage("com.karumi.springbootkotlin"))
    .paths(PathSelectors.any())
    .build()
    .enable(true)
    .securityContexts(listOf(securityContext()))
    .securitySchemes(listOf(apiKey()))

  private fun apiKey(): ApiKey = ApiKey(API_KEY_NAME, AUTHORIZATION, HEADER.name)

  private fun securityContext(): SecurityContext {
    return SecurityContext.builder()
      .securityReferences(defaultAuth())
      .forPaths(PathSelectors.any())
      .build()
  }

  fun defaultAuth(): List<SecurityReference> {
    val authScope = AuthorizationScope("global", "accessEverything")
    val authorizationScopes = arrayOf(authScope)
    return listOf(SecurityReference(API_KEY_NAME, authorizationScopes))
  }
}
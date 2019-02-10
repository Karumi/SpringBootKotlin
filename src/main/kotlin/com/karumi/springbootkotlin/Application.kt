package com.karumi.springbootkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@SpringBootApplication
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
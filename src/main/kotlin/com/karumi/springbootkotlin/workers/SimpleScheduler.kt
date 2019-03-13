package com.karumi.springbootkotlin.workers

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class SimpleScheduler {

  companion object {
    private val logger = LoggerFactory.getLogger(SimpleScheduler::class.java)
  }

  @Scheduled(fixedRate = 1000)
  fun execute() {
    val startDate = System.currentTimeMillis()

    val task1 = hardWork()
    val task2 = hardWork()
    val task3 = hardWork()

    CompletableFuture.allOf(task1, task2, task3).join()

    val endDate = System.currentTimeMillis()
    logger.info("Total time: ${(endDate - startDate) / 1000}s")
  }

  fun hardWork(): CompletableFuture<Unit> =
    CompletableFuture.supplyAsync { Thread.sleep(1000) }
}
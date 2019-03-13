package com.karumi.springbootkotlin.common

import arrow.core.Try
import java.util.logging.Level
import java.util.logging.Logger

inline fun <reified A : Any, B> A.TryLogger(f: () -> B): Try<B> = Try(f).apply {
  fold(
    { Try { Logger.getLogger(A::class.qualifiedName).log(Level.SEVERE, it.message, it) } },
    { Unit }
  )
}
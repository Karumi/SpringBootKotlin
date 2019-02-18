package com.karumi.springbootkotlin.common

import arrow.core.Option
import arrow.core.Try
import arrow.core.failure
import arrow.core.identity
import arrow.core.success
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KClass

class TryLogger {

  companion object {
    operator fun <A, B : Any> invoke(tag: KClass<B>, f: () -> A): Try<A> = Try(f).apply {
      fold({ Logger.getLogger(tag.qualifiedName).log(Level.SEVERE, it.message, it) }, { Unit })
    }
  }
}

fun <A> Try<A>.mapException(map: (exception: Throwable) -> Throwable): Try<A> =
    fold({ map(it).failure() }, { it.success() })

fun <A> Try<A>.orThrow(): A = fold({ throw it }, ::identity)

fun <A> Option<A>.toTry(ifEmpty: () -> Throwable): Try<A> =
    fold({ ifEmpty().failure() }, { it.success() })
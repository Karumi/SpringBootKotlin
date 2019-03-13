package com.karumi.springbootkotlin.common

import arrow.core.Option
import arrow.core.Try
import arrow.core.failure
import arrow.core.identity
import arrow.core.success

fun <A> Try<A>.mapException(map: (exception: Throwable) -> Throwable): Try<A> =
  fold({ map(it).failure() }, { it.success() })

fun <A> Try<A>.orThrow(): A = fold({ throw it }, ::identity)

fun <A> Option<A>.toTry(ifEmpty: () -> Throwable): Try<A> =
  fold({ ifEmpty().failure() }, { it.success() })
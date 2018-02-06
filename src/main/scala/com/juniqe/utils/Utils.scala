package com.juniqe.utils

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Utils {

  @annotation.tailrec
  def retry[T](n: Int)(fn: => T): Try[T] = {
    Try { fn } match {
      case x: Success[T] => x
      case _ if n > 1 => retry(n - 1)(fn)
      case fn => fn
    }
  }

  type Recovery[T] = PartialFunction[Throwable,T]

  def withNone[T]: Recovery[Option[T]] = { case NonFatal(e) => None }

  def withEmptySeq[T]: Recovery[Seq[T]] = { case NonFatal(e) => Seq() }

}

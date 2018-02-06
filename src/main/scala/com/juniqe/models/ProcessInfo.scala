package com.juniqe.models

case class ProcessInfo[T](success: Boolean, result: Seq[T], size: Int)

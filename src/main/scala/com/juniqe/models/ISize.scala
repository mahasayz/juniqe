package com.juniqe.models

case class ISize(length: Int, width: Int, orientation: Orientations.Value) {
  override def toString: String = s"${length}x$width"
}

object ISize {
  def generateSizes(size: ISize): Seq[ISize] = {
    Seq.empty
  }
}

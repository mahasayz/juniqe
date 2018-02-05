package com.juniqe.models

case class IFile(designerId: Int, designId: Int, productType: ProductTypes.Value, size: ISize, extension: String = "jpg") {
  override def toString: String = s"$designerId-$designId-$productType${size.orientation}-${size.toString}.$extension"
}

object IFile {
  def apply(fileName: String): IFile = {
    val FILENAME_PATTERN = """(\d)-(\d)-(\d+)(\w)-(\d+)x(\d+)\.(\w{2,4})""".r
    fileName match {
      case FILENAME_PATTERN(designerId, designId, productType, orientation, length, width, extension) =>
        IFile(designerId.toInt, designId.toInt, ProductTypes.withName(productType), ISize(length.toInt, width.toInt, Orientations.withName(orientation)), extension)
      case _ => throw InvalidFilenameException("Filename is invalid")
    }
  }
}

case class InvalidFilenameException(message: String = "") extends Exception(message)
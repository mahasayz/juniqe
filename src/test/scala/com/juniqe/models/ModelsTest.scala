package com.juniqe.models

import org.scalatest.{FunSuite, Matchers}
import com.juniqe.models.InvalidFilenameException

import scala.concurrent.Await

class ModelsTest extends FunSuite with Matchers {

  test("exception when filename is invalid") {
    assertThrows[InvalidFilenameException] {
      IFile("something")
    }
  }

  test("file is created when filename is valid") {
    val file = IFile("2-3-300L-20x30.jpg")
    val size = file.size
    file should have (
      'designerId (2),
      'designId (3),
      'productType (ProductTypes.Canvas),
      'extension ("jpg")
    )
    size should have (
      'length (20),
      'width (30),
      'orientation (Orientations.Landscape)
    )
  }

  test("another file is created when filename is valid") {
    val file = IFile("23-2-300X-70x70.jpg")
    val size = file.size
    file should have (
      'designerId (23),
      'designId (2),
      'productType (ProductTypes.Canvas),
      'extension ("jpg")
    )
    size should have (
      'length (70),
      'width (70),
      'orientation (Orientations.Square)
    )
  }

  test("proper size generations") {
    import scala.concurrent.duration._
    import com.juniqe.processor.Pipeline._

    val ifile = IFile("2-3-300L-60x90.jpg")
    val sizes = Await.result(generateSizes(ifile), 10.seconds)

    println(sizes.size)
  }

}

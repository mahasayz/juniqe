package com.juniqe.models

import java.net.URI
import java.nio.file.{Files, Paths}

import com.juniqe.processor.Pipeline
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class PipelineTest extends FunSuite with Matchers {

  test("proper size generations to be rendered") {
    import com.juniqe.processor.Pipeline._

    val ifile = IFile("2-3-300L-60x90.jpg")
    val sizes = Await.result(generateSizes(ifile), 10.seconds)

    val expectedSize = Seq(
      IFile("2-3-300L-20x30.jpg"),
      IFile("2-3-300L-40x60.jpg"),
      IFile("2-3-300L-60x90.jpg")
    )

    sizes should have length (expectedSize.size)
    sizes shouldBe expectedSize
  }

  test("entire pipeline") {
    val files = Seq(
      Files.createFile(Paths.get("./1-2-100P-120x80.jpg")).toFile,
      Files.createFile(Paths.get("./1-3-100P-90x60.jpg")).toFile,
      Files.createFile(Paths.get("./23-2-300X-70x70.jpg")).toFile
    )

    val result = Await.result(Pipeline.run(files), 10.seconds)
    val expectedResult = Seq(
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-30x20.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-60x40.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-90x60.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-120x80.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-3-100P-30x20.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-3-100P-60x40.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-3-100P-90x60.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/23-2-300X-20x20.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/23-2-300X-30x30.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/23-2-300X-50x50.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/23-2-300X-70x70.jpg")
    )

    result shouldBe ProcessInfo[URI](true, expectedResult, expectedResult.size)
  }

  ignore("generate proper urls without duplicates in cases of files sharing subset render sizes") {
    val files = Seq(
      Files.createFile(Paths.get("./1-2-100P-120x80.jpg")).toFile,
      Files.createFile(Paths.get("./1-2-100P-60x40.jpg")).toFile
    )

    val result = Await.result(Pipeline.run(files), 10.seconds)
    val expectedResult = Seq(
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-30x20.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-60x40.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-90x60.jpg"),
      new URI("https://test.s3.amazonaws.com/tmp/1-2-100P-120x80.jpg")
    )

    result shouldBe ProcessInfo[URI](true, expectedResult, expectedResult.size)
  }

}

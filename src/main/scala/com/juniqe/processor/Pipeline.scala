package com.juniqe.processor

import java.io.File
import java.net.URI
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.nio.file.{FileAlreadyExistsException, Files, Path, Paths}
import java.util.UUID

import com.juniqe.models.{IFile, ISize, ProcessInfo}
import com.juniqe.services.amazon.S3Service
import com.juniqe.services.db.MockDBService
import com.juniqe.utils.Utils._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

object Pipeline {
  import scala.concurrent.ExecutionContext.Implicits.global

  object DBDal extends MockDBService
  object S3Service extends S3Service

  def moveFile(files: Seq[File]): Future[Seq[Path]] = {
    Future.sequence(
      for(file <- files) yield {
        val fileName = file.getName
        val uniqueName = s"${UUID.randomUUID}_$fileName"
        val path = Files.move(file.toPath, new File(s"/tmp/$fileName").toPath, REPLACE_EXISTING)
        println(s"Successfully moved $fileName to /tmp/")
        Future.successful(path)
      }
    )
  }

  def generateSizes(ifile: IFile): Future[Seq[IFile]] = {
    val sizes = DBDal.getSizes.recover(withEmptySeq)

    sizes.map(s => {
      s.filter(is => is.orientation == ifile.size.orientation && is.length <= ifile.size.length && is.width <= ifile.size.width)
        .map(x => {
          val res = ifile.copy(size = x)
          println(s"Generated $res for $ifile")
          res
        })
    })
  }

  def f(ifile: Path): Future[Seq[Path]] = {
    val result = for {
      renderableSizes <- generateSizes(IFile(ifile.getFileName.toString)).recover(withEmptySeq[IFile])
      renderFiles = renderableSizes
    } yield {
      renderFiles.map(x => {
        val path = Paths.get(s"/tmp/${x.toString}")
        Try(Files.createFile(path)).getOrElse(path)
      })
    }
    result
  }

  def render(ifiles: Seq[Path]): Future[Seq[Path]] = {
    val result = for (file <- ifiles) yield {
      f(file)
    }
    Future.sequence(result).map(_.flatten)
  }

  def uploadToS3(files: Seq[Path]) = Future.sequence(
    for (file <- files) yield {
//      val bucket = S3Service.getBucketByName("test") getOrElse(S3Service.createBucket("test"))
//      S3Service.createObject(bucket, file.getFileName.toString, file.toFile)
      Future.successful(new URI(s"https://test.s3.amazonaws.com${file.toString}"))
    }
  )

  def deleteFile(files: Seq[Path]): Future[Seq[Boolean]] = Future.sequence(
    for (file <- files) yield Future.successful(Files.deleteIfExists(file))
  )

  def run(files: Seq[File]): Future[ProcessInfo[URI]] = {
    // move original file to /tmp
    // render all possible smaller versions
        // create service IMService with stub
        // def scale(input: File, size: Size): AsynExecution[File]    // this is the rendering job
    // upload rendered files to S3
    // delete all locally created images
    // return seq of URL strings from S3

    val res = for {
      path <- moveFile(files)
      renderedImages <- render(path)
      uploadURI <- uploadToS3(renderedImages)
      r <- deleteFile(renderedImages)
    } yield {
      uploadURI
    }

    res.map(r => ProcessInfo[URI](true, r, r.size))
  }

}

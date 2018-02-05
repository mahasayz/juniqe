package com.juniqe.processor

import java.io.File
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.nio.file.{Files, Path}
import java.util.UUID

import com.juniqe.models.{ISize, ProcessInfo}
import com.juniqe.services.amazon.S3Service
import com.juniqe.services.db.MockDBService
import com.juniqe.utils.Utils._

import scala.concurrent.Future

class Pipeline {
  import scala.concurrent.ExecutionContext.Implicits.global

  object DBDal extends MockDBService
  object S3Service extends S3Service

  def moveFile(file: File): Future[Path] = Future {
    val fileName = file.getName
    val uniqueName = s"${UUID.randomUUID}_$fileName"

    Files.move(file.toPath, new File(s"/tmp/$uniqueName").toPath, REPLACE_EXISTING)
  }

  def generateSizes(size: ISize): Future[Seq[ISize]] = {
    val sizes = DBDal.getSizes.recover(withEmptySeq)

    sizes.map(s => {
      s.filter(is => is.orientation == size.orientation && is.length < size.length && is.width < size.width)
    })
  }

  def uploadToS3(file: File) = Future {
    val bucket = S3Service.getBucketByName("test") getOrElse(S3Service.createBucket("test"))
    S3Service.createObject(bucket, file.toPath.getFileName.toString, file)
  }

  def deleteFile(file: File): Future[Boolean] = Future {
    Files.deleteIfExists(file.toPath)
  }



  def run(files: Seq[File]): Future[ProcessInfo] = {
    // move original file to /tmp
    // render all possible smaller versions
        // create service IMService with stub
        // def scale(input: File, size: Size): AsynExecution[File]    // this is the rendering job
    // upload rendered files to S3
    // delete all locally created images
    // return seq of URL strings from S3

//    for (file <- files) {
//      moveFile(file).flatMap(path => {
//        val name = path.getFileName
//        name.
//      })
//    }



    Future.successful(ProcessInfo(true, 0, 0))
  }

}

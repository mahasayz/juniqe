package com.juniqe.services.amazon

import java.nio.file.{CopyOption, Files}
import java.util.UUID

import awscala.File
import awscala.s3.{Bucket, PutObjectResult, S3Object}

import scala.concurrent.Future

trait S3Service[T] {

  def s3: T

  def getBuckets: Seq[Bucket]

  def getBucketByName: Option[Bucket]

  def createObject(bucket: Bucket, name: String, file: File): PutObjectResult

  def getObject(bucket: Bucket, name: String): Option[S3Object]

}

trait MockS3Service[T] extends S3Service[T] {
  import java.io.File

  def uploadFile(file: File): Future[S3Response] = {
    val fileName = file.getName
    val uniqueName = s"${UUID.randomUUID}_$fileName"

    Files.move(file.toPath, new File(s"/tmp/$uniqueName").toPath, REPLACE_EXISTING)
  }

}

case class S3Response(success: Boolean)
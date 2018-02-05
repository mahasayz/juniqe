package com.juniqe.processor

import java.io.File

import com.juniqe.models.ProcessInfo
import com.juniqe.services.db.MockDBService

import scala.concurrent.Future

class Pipeline {

  object DBDal extends MockDBService

  def run(files: Seq[File]): Future[ProcessInfo] = {
    // move original file to /tmp
    // render all possible smaller versions
        // create service IMService with stub
        // def scale(input: File, size: Size): AsynExecution[File]    // this is the rendering job
    // upload rendered files to S3
    // delete all locally created images
    // return seq of URL strings from S3

    Future.successful(ProcessInfo(true, 0, 0))
  }

}

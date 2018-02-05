package com.juniqe.services.db

import com.juniqe.models.{ISize, Orientations, ProductTypes}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DBService {
  def getSizes: Future[Seq[ISize]]
}

trait MockDBService extends DBService {
  def getSizes: Future[Seq[ISize]] = Future {
    Seq(
      ISize(20,20,Orientations.Square), ISize(30,30,Orientations.Square), ISize(50,50,Orientations.Square), ISize(70,70,Orientations.Square), ISize(100,100,Orientations.Square),
      ISize(20,30,Orientations.Square), ISize(40,60,Orientations.Landscape), ISize(60,90,Orientations.Landscape), ISize(80,120,Orientations.Landscape), ISize(100,150,Orientations.Landscape),
      ISize(30,20,Orientations.Square), ISize(60,40,Orientations.Portrait), ISize(90,60,Orientations.Portrait), ISize(120,80,Orientations.Portrait), ISize(150,100,Orientations.Portrait)
    )
  }
}
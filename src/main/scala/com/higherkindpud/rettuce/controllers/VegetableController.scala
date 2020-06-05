package com.higherkindpud.rettuce.controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VegetableController(_controllerComponents: ControllerComponents) extends BaseController {

  def apple() =
    Action { implicit request: Request[AnyContent] =>
      Ok("")
    }

  override def controllerComponents: ControllerComponents = _controllerComponents
}

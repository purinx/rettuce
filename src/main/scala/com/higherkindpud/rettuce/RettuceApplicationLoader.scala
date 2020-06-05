package com.higherkindpud.rettuce

import com.softwaremill.macwire.wire

import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import router.Routes
import play.api.routing.Router
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.ApplicationLoader.Context
import play.api.http.{HttpErrorHandler, JsonHttpErrorHandler}

class RettuceApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
    new RettuceApplicationBase(context).application
  }
}

class RettuceApplicationBase(context: Context)
    extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with AssetsComponents
    with RettuceComponents {

  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  override lazy val httpErrorHandler: HttpErrorHandler =
    new JsonHttpErrorHandler(environment, devContext.map(_.sourceMapper))

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor

    val prefix: String = "/"
    // unusedでwarnが出るけど消したら動かないので気を付ける
    // (;clean ;compileしたらコンパイル落ちることが確認できる)
    // TODO: build.sbtでいい感じにwarn出ないように

    wire[Routes]
  }

}

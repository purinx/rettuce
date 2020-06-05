package com.higherkindpud.rettuce

import com.softwaremill.macwire.wire

import com.higherkindpud.rettuce.config.AppConfig
import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache

import redis.clients.jedis.JedisPool
import com.softwaremill.macwire.wire
import pureconfig.ConfigSource
// import play.api.inject.Module
import _root_.controllers.AssetsComponents
import com.typesafe.config.ConfigFactory
import play.api.ApplicationLoader.Context
import play.api._
import play.api.http.{HttpErrorHandler, JsonHttpErrorHandler}
import play.api.mvc._
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import redis.clients.jedis.JedisPool
import router.Routes

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
  LoggerConfigurator(context.environment.classLoader)
    .foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }

  override lazy val httpErrorHandler: HttpErrorHandler = new JsonHttpErrorHandler(environment, sourceMapper)

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }
}

import play.api.mvc.ControllerComponents

trait RettuceComponents {
  lazy val config: AppConfig = ConfigSource
    .fromConfig(ConfigFactory.load())
    .load[AppConfig]
    .toOption
    .getOrElse(throw new RuntimeException("can not read application conf of AppConfig Type"))

  //domain
  lazy val vegetableService = wire[VegetableService]

  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  //repository
  lazy val pool: JedisPool   = new JedisPool(config.redis.host, config.redis.port)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}

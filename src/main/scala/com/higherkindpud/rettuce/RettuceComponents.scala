package com.higherkindpud.rettuce

import com.higherkindpud.rettuce.controller.VegetableController
import com.higherkindpud.rettuce.domain.repository.VegetableRepository
import com.higherkindpud.rettuce.domain.service.VegetableService
import com.higherkindpud.rettuce.infra.redis.VegetableRepositoryOnRedis
import com.higherkindpud.rettuce.infra.redis.common.DefaultRedisCache
import com.softwaremill.macwire.wire
// import play.api.inject.Module
import redis.clients.jedis.JedisPool

import play.api.mvc._
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import router.Routes

import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import play.api._

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

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    // val prefix: String = "/"
    wire[Routes]
  }

}

// trait RettuceComponents extends Module with SystemComponents {
trait RettuceComponents {

  //domain
  def configuration: Configuration
  lazy val vegetableService = wire[VegetableService]

  //controller
  def controllerComponents: ControllerComponents
  lazy val vegetableController: VegetableController = wire[VegetableController]

  //repository
  lazy val pool: JedisPool   = new JedisPool("127.0.0.1", 16379)
  lazy val defaultRedisCache = wire[DefaultRedisCache]

  lazy val vegetableRepository: VegetableRepository = wire[VegetableRepositoryOnRedis]
}

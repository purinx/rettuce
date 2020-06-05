// package com.higherkindpud.rettuce

// import java.io.File

// import akka.actor.ActorSystem
// import akka.stream._
// import com.softwaremill.macwire.wire
// import com.typesafe.config.ConfigFactory

// import play.api.Mode.Dev
// import play.api._
// import play.api.http._
// import play.api.i18n._
// import play.api.inject._
// import play.api.libs.Files._
// import play.api.mvc._

// import scala.concurrent.ExecutionContext

// trait SystemComponents {
//   //PlayFramework system

//   implicit val system                     = ActorSystem()
//   implicit val materializer: Materializer = ActorMaterializer() //設定の仕方これで良い？
// //  implicit val defaultDispatcher                  = system.dispatcher
// //  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.fromExecutor(defaultDispatcher)
//   implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

//   lazy val classLoader: ClassLoader = new ClassLoader() {}

//   lazy val rootPath: File = new File("") //これで良い？
//   lazy val mode: Mode     = Dev          //todo: read application.config

//   lazy val enviroment = wire[Environment]

//   lazy val typesafeConfig                                              = ConfigFactory.load()
//   lazy val parserConfig: ParserConfiguration                           = new ParserConfiguration
//   lazy val configuration: Configuration                                = wire[Configuration]
//   lazy val fileMimeTypeConfig: FileMimeTypesConfiguration              = FileMimeTypesConfiguration()
//   lazy val temporaryFileReaperConfig: TemporaryFileReaperConfiguration = new TemporaryFileReaperConfiguration()

//   lazy val applicationLifecycle: ApplicationLifecycle = wire[DefaultApplicationLifecycle]
//   lazy val temporaryFileReaper: TemporaryFileReaper   = wire[DefaultTemporaryFileReaper]
//   lazy val errorHandler: HttpErrorHandler             = new DefaultHttpErrorHandler
//   lazy val temporaryFileCreator: TemporaryFileCreator = wire[DefaultTemporaryFileCreator]

//   lazy val parser: BodyParser[AnyContent] = new BodyParsers.Default
//   lazy val parsers: PlayBodyParsers       = wire[DefaultPlayBodyParsers]
//   lazy val messagesApi: MessagesApi       = new DefaultMessagesApi
//   lazy val langs: Langs                   = new DefaultLangs
//   lazy val fileMimeTypes: FileMimeTypes   = wire[DefaultFileMimeTypes]

//   lazy val actionBuilder: DefaultActionBuilder        = new DefaultActionBuilderImpl(parser)
//   lazy val controllerComponents: ControllerComponents = wire[DefaultControllerComponents]

// }

package com.akka.steps.stream.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor

class MainRoute(system: ActorSystem, materializer: ActorMaterializer) {

  val log = LoggerFactory.getLogger(this.getClass)

  val address = new AddressRoute {
    override implicit val actorSystem: ActorSystem = system
    override implicit val actorMaterializer: ActorMaterializer = materializer
  }

  val myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: Exception =>
      extractUri { uri =>
        log.error(s"Error trying to process the following uri: $uri", e)
        complete((InternalServerError, "Error trying to process the request"))
      }
  }

  val routes = {
    handleExceptions(myExceptionHandler) {
      address.route
    }
  }
}

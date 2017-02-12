package com.akka.steps.stream

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.akka.steps.stream.route.MainRoute
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

/**
  * Created by thiago on 12/02/2017.
  */
object Boot extends App {

  val config = ConfigFactory.load()
  val log = LoggerFactory.getLogger(this.getClass)

  implicit val system = ActorSystem()

  val decider: Supervision.Decider = {
    case e: Exception =>
      log.error(s"We got an error", e.getMessage)
      Supervision.Resume
  }

  implicit val materializer = ActorMaterializer(
    ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  )

  implicit val dispatcher = system.dispatcher

  val mainRoute = new MainRoute(system, materializer)

  Http().bindAndHandle(mainRoute.routes, config.getString("http.interface"), config.getInt("http.port")).onComplete {
    case Success(_) => log.info("Application Started")
    case Failure(f) => log.error("Could not start the server", f)
  }
}

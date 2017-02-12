package com.akka.steps.stream.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.akka.steps.stream.graph.AddressGraph
import com.akka.steps.stream.model.Address
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by thiago on 12/02/2017.
  */
trait AddressRoute {

  import com.akka.steps.stream.MyJsonProtocol._

  implicit def actorSystem: ActorSystem
  implicit def actorMaterializer: ActorMaterializer

  implicit val timeout = Timeout(5.seconds)

  val log = LoggerFactory.getLogger(this.getClass)

  val route = {
    pathPrefix("address") {
      get {
        pathEndOrSingleSlash {
          parameters((
            'state.?,
            'city.?,
            'neighborhood.?,
            'street.?,
            'streetNumber.?,
            'zipcode.?
          )).as(Address) { address =>

            onComplete(AddressGraph.run(address)) {
              case Success(context) =>
                (context.address, context.exception) match {
                  case (Some(validAddress), None) => complete(OK, validAddress)
                  case (_, Some(exception)) => complete(BadRequest, exception.getMessage)
                  case _ => complete(InternalServerError)
                }

              case Failure(f) => complete(InternalServerError, f)
            }
          }
        }
      }
    }
  }
}

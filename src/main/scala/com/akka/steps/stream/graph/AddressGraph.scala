package com.akka.steps.stream.graph

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import com.akka.steps.stream.exception.ValidationException
import com.akka.steps.stream.model.{Address, Context}
import org.slf4j.LoggerFactory

import scala.concurrent.Future

/**
  * Created by thiago on 12/02/2017.
  */
object AddressGraph {

  val log = LoggerFactory.getLogger(this.getClass)

  val validContext = Flow[Context].filter(_.exception.isEmpty)
  val invalidContext = Flow[Context].filter(_.exception.isDefined)

  val validation = Flow[Address].map { address =>
    (address.state, address.city) match {
      case (Some(_), Some(_)) => Context(Some(address), None)
      case _ => Context(None, Some(new ValidationException("State and City must not be null")))
    }
  }

  val sanitize = Flow[Context].map { context =>
    val address = Some(context.address.get.copy(zipCode = Some("01111-000")))
    context.copy(address = address)
  }

  def run(address: Address)(implicit system: ActorSystem, materializer: ActorMaterializer): Future[Context] = {

    val resultSink = Sink.head[Context]

    val graph = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit builder => sink =>
      import GraphDSL.Implicits._

      val bcast = builder.add(Broadcast[Context](2))
      val merge = builder.add(Merge[Context](2))

      Source.single(address) ~> validation ~> bcast ~> validContext ~> sanitize ~> merge ~> sink
                                              bcast ~> invalidContext           ~> merge

      ClosedShape
    })


    graph.run()
  }
}

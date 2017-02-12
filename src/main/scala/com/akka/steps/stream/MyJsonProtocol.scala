package com.akka.steps.stream

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.ext.{JavaTypesSerializers, JodaTimeSerializers}
import org.json4s.{DefaultFormats, jackson}
import spray.json.DefaultJsonProtocol

/**
  * Created by thiago on 12/02/2017.
  */
object MyJsonProtocol extends Json4sSupport with DefaultJsonProtocol with SprayJsonSupport {
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all ++ JavaTypesSerializers.all
  implicit val serialization = jackson.Serialization
}

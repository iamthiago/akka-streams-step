package com.akka.steps.stream.exception

/**
  * Created by thiago on 12/02/2017.
  */
class ValidationException(message: String, cause: Throwable) extends Exception(message, cause) {
  def this(message: String) = this(message, null)
}
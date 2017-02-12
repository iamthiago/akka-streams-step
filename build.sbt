name := "akka-step-streams"

version := "1.0"

scalaVersion := "2.12.1"

val akkaV = "2.4.17"
val akkaHttpV = "10.0.3"
val json4sV = "3.5.0"
val scalaTestV = "3.0.1"

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestV % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

lazy val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-slf4j" % akkaV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV
)

lazy val akkaHttpDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV
)

lazy val akkaComplementsDependencies = Seq(
  "de.heikoseeberger" %% "akka-http-json4s" % "1.12.0"
)

lazy val jsonDependencies = Seq(
  "org.json4s" %% "json4s-jackson" % json4sV,
  "org.json4s" %% "json4s-ext" % json4sV
)

libraryDependencies ++= (
  commonDependencies ++
    akkaDependencies ++
    akkaHttpDependencies ++
    akkaComplementsDependencies ++
    jsonDependencies
  )
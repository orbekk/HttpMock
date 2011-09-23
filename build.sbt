name := "HttpMock"

version := "0.0"

organization := "no.ntnu"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

// Test dependencies
libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.9.1" % "latest.integration" % "test",
    "junit" % "junit" % "latest.integration" % "test"
)

// Dependencies
libraryDependencies ++= Seq(
    "org.eclipse.jetty" % "jetty-server" % "latest.integration",
    "org.eclipse.jetty" % "jetty-servlet" % "latest.integration",
    "javax.servlet" % "servlet-api" % "latest.integration",
    "net.liftweb" % "lift-json" % "latest.integration"
)

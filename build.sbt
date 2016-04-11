name := "Jimmy"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2-core" % "3.7.2" % "test",
	"org.scala-lang" % "scala-parser-combinators" % "2.11.0-M4",
	"org.scalaj" %% "scalaj-http" % "2.2.1",
	"org.scala-lang.modules" %% "scala-xml" % "1.0.3",
	"com.typesafe" % "config" % "1.2.1",
	"org.freemarker" % "freemarker" % "2.3.20",
	"org.jsoup" % "jsoup" % "1.8.3",
	"net.sourceforge.cssparser" % "cssparser" % "0.9.18",
	"com.github.scopt" %% "scopt" % "3.4.0",
	"org.log4s" %% "log4s" % "1.2.1",
	"org.slf4j" % "slf4j-api" % "1.7.10",
	"org.slf4j" % "slf4j-simple" % "1.7.5",
	"org.clapper" %% "grizzled-slf4j" % "1.0.2"
	// "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.1.0",
	// "ch.qos.logback" %  "logback-classic" % "1.1.7",
)

scalacOptions in Test ++= Seq("-Yrangepos")

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

// scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions")

test in assembly := {}

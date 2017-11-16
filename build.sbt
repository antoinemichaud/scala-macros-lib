import scala.languageFeature.experimental.macros

name := "macro-definitions"
 
version := "1.0"
 
scalaVersion := "2.11.11"
 
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
 
libraryDependencies += "org.mockito" % "mockito-core" % "2.11.0" % "test"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

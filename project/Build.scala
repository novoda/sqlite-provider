import sbt._

import Keys._
import AndroidKeys._

object General {

  val settings = Defaults.defaultSettings ++ Seq(
    name := "SQLiteProvider",
    version := "0.0.1",
    scalaVersion := "2.9.0-1",
    platformName in Android := "android-10",
    fullClasspath in Test <<= (fullClasspath in Test, baseDirectory) map { (cp, bd) => cp sortWith ((x,y) => !x.data.getName.contains("android"))},

    libraryDependencies ++= Seq(
      "com.google.android" % "android" % "2.3.3",
      "org.specs2" %% "specs2" % "1.5" % "test",
      "org.eclipse.jetty" % "jetty-websocket" % "7.5.0.RC1" % "test",
      "org.eclipse.jetty" % "jetty-servlet" % "7.5.0.RC1" % "test",
"com.novocode" % "junit-interface" % "0.7" % "test->default",
      "com.github.jbrechtel" %% "robospecs" % "0.0.2-SNAPSHOT" % "test"
    ),

    resolvers ++= Seq(
      "snapshots" at "http://scala-tools.org/repo-snapshots",
      "releases" at "http://scala-tools.org/repo-releases",
      "robospecs" at "http://jbrechtel.github.com/repo/snapshots",
      "pivotal" at "https://oss.sonatype.org/content/repositories/snapshots"
    )
  )

}

object WSBuild extends Build {

  lazy val main = Project(
    "main",
    file("."),
    settings = General.settings  ++ AndroidBase.settings
  )

  lazy val tests = Project(
    "tests",
    file("it"),
    settings = General.settings ++ PlainJavaProject.settings
  ) dependsOn main

}

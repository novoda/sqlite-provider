import saab.Plugin._

import sbt._
import Keys._

organization := "com.novoda"

name := "SQLiteProvider"

version := "0.0.2-SNAPSHOT"

autoScalaLibrary := false

libraryDependencies ++= Seq(
	"com.google.android" % "android" % "2.3.3",
	"org.mockito" % "mockito-core" % "1.8.5" % "test",
	"com.google.android" % "android-test" % "2.3.3" % "test",
        "junit" % "junit" % "4.10" % "test"
)

seq(Robolectric.settings : _*)

seq(sbt.Defaults.itSettings : _*)

seq(androidSettingsIn(IntegrationTest) : _*)


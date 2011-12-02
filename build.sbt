
import de.johoop.jacoco4sbt._
import JacocoPlugin._
import saab.Plugin._

organization := "com.novoda"

name := "SQLiteProvider"

version := "0.0.2-SNAPSHOT"

libraryDependencies ++= Seq(
	"com.google.android" % "android" % "2.3.3",
	"org.mockito" % "mockito-core" % "1.8.5" % "test",
	"com.google.android" % "android-test" % "2.3.3" % "test",
        "junit" % "junit" % "4.10" % "test"
)

seq(Robolectric.settings : _*)

seq(jacoco.settings : _*)

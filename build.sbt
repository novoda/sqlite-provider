
import saab.Plugin._

organization := "com.novoda"

name := "SQLiteProvider"

version := "0.0.2-SNAPSHOT"

libraryDependencies ++= Seq(
	"com.google.android" % "android" % "2.3.3",
	"org.mockito" % "mockito-core" % "1.8.5" % "test",
        "junit" % "junit" % "4.10" % "test"
)

//seq(defaultSettings : _*)


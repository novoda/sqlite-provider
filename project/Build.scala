import sbt._

import Keys._
import AndroidKeys._

object General {

  val settings = Defaults.defaultSettings ++ Seq(
    name := "SQLiteProvider",
    version := "0.0.1",
    scalaVersion := "2.9.1",
    platformName in Android := "android-10",
    fullClasspath in Test <<= (fullClasspath in Test, baseDirectory) map { (cp, bd) => cp sortWith ((x,y) => !x.data.getName.contains("android"))},

    libraryDependencies ++= Seq(
	"com.novocode" % "junit-interface" % "0.7" % "test->default",
	"com.pivotallabs" % "robolectric" % "1.0-RC1" % "test",
	"org.mockito" % "mockito-all" % "1.9.0-rc1" % "test"
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

 lazy val tests = Project("instTest", file("."))
      .configs( IntegrationTest )
      .settings( Defaults.itSettings : _*)
      .settings(General.settings : _*)
      .settings( Defaults.itSettings : _*)
      .settings( AndroidBase.settings : _*)
      .settings(  AndroidInstall.settings : _*)
      .settings( 
	name := "SQLiteProviderTest"
      ) dependsOn main
}

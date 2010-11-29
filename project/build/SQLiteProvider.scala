import sbt._

trait Defaults {
  def androidPlatformName = "android-8"
}
  
class SQLiteProvider(info: ProjectInfo) extends ParentProject(info) {

  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }

  lazy val main  = project(".", "SQLiteProvider", new MainProject(_))
  lazy val tests = project("tests",  "SQLiteProviderTest", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults {
    override def outputDirectoryName = "bin"
    override def dependencyPath = "libs"
    override def managedDependencyPath = "libs"
    override def mainSourcePath = "."
    
    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
    val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.6" % "test"
    val robolectric = "com.xtremelabs" % "robolectric" % "0.9.2" % "test" from "http://pivotal.github.com/robolectric/downloads/robolectric-0.9.2.jar"
    val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
    val junit = "junit" % "junit" % "4.8.2" % "test"
    val wikitext = "org.fusesource.wikitext" % "wikitext-core" % "1.1" % "test"
    val textile = "org.fusesource.wikitext" % "textile-core" % "1.1" % "test"
    
    override def testScalaSourcePath = "specs"
  }

  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults {
    override def outputDirectoryName = "bin"    
    override def managedDependencyPath = "libs"
    override def mainSourcePath = "."
  }
}
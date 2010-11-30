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
  }

  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults {  
        
    val junitInterface = "com.novocode" % "junit-interface" % "0.5" % "test"
    
    val robolectric = "com.xtremelabs" % "robolectric" % "0.9.2" % "test" from "http://pivotal.github.com/robolectric/downloads/robolectric-0.9.2.jar"
    val javaassit = "javassist" % "javassist" %"3.8.0.GA" % "test"
    val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"
    
    val junit = "junit" % "junit" % "4.8.2" % "test"    
    
    val googlemap = Path.fromFile("/opt/android/add-ons/addon_google_apis_google_inc_8/libs/maps.jar")
    
    override def unmanagedClasspath = super.unmanagedClasspath +++ googlemap
    
    override def includeTest(s: String) = { s.endsWith("Spec") || s.endsWith("LocalTest")}
    override def testJavaSourcePath = "src"
    override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
    
  }
}
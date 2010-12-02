import sbt._
import de.element34.sbteclipsify._

trait Defaults extends Eclipsify {
  def androidPlatformName = "android-8"
  //override lazy val eclipseProjectNature = ProjectNature.Java
}
  
class SQLiteProvider(info: ProjectInfo) extends ParentProject(info) {
  
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }
  
  lazy val main  = project(".", "SQLiteProvider", new MainProject(_))
  lazy val tests = project("tests" / "instrumentation",  "SQLiteProviderTest", new TestProject(_), main)
  
  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with Robolectric {        
    def googleMapLocation =  "D:/opt/xandroid/android-sdk-windows/add-ons/addon_google_apis_google_inc_8/libs/maps.jar"
  }
      
  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults {
  }  
}
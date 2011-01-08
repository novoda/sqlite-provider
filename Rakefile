require 'rubygems'
require 'antwrap'
require 'builder'

ANDROID_COMMANDS = ["com.android.ide.eclipse.adt.ResourceManagerBuilder", "com.android.ide.eclipse.adt.PreCompilerBuilder", "org.eclipse.jdt.core.javabuilder", "com.android.ide.eclipse.adt.ApkBuilder"]
ANDROID_NATURES = ["com.android.ide.eclipse.adt.AndroidNature", "org.eclipse.jdt.core.javanature"]
ROBO_COMMANDS = ["org.eclipse.jdt.core.javabuilder"]
ROBO_NATURES = ["org.eclipse.jdt.core.javanature"]
LINKS = ["gen", "src"]
ANDROID_CONTAINER = "com.android.ide.eclipse.adt.ANDROID_FRAMEWORK"


desc "Generating .project"
file "\.project" do
  File.open(".project", 'w') {
    |f| f.write(generate_project :name => "SQLiteProvider", :natures => ANDROID_NATURES, :commands => ANDROID_COMMANDS)
  }
end

desc "generate .project for local test"
file "tests/.project" do
  File.open("tests/.project", 'w') {
    |f| f.write(generate_project :name => "SQLiteProviderRoboTest", :natures => ROBO_NATURES, :commands => ROBO_COMMANDS, :resources => LINKS, :parent => "SQLiteProvider")
  }
end

desc "generate .project for instrumentation"
file "tests/instrumentation/.project" do
  File.open("tests/instrumentation/.project", 'w') {|f|
    f.write(generate_project :name => "SQLiteProviderTest", :natures => ANDROID_NATURES, :commands => ANDROID_COMMANDS, :resources => ["src"], :parent => "SQLiteProvider" )
  }
end



desc "Generating .classpath"
file ".classpath" do
  File.open(".classpath", 'w') {
    |f| f.write(generate_classpath :classpath => [{:kind => 'src', :path=> 'src'},      {:kind => 'src', :path=> 'gen'},      {:kind => 'con', :path=> ANDROID_CONTAINER},      {:kind => 'output', :path=> 'bin'}])    }
  end

  desc "Generating robo .classpath"
  file "tests/.classpath" do
    File.open("tests/.classpath", 'w') {
      |f| f.write(generate_classpath :classpath => [{:kind => 'src', :path=> 'src'},
        {:kind => 'src', :path=> 'gen'},
        {:kind => 'con', :path=> ANDROID_CONTAINER},
        {:kind => 'output', :path=> 'bin'}])
      }
    end


    task :eclipse_project => [".project", "tests/.project", "tests/instrumentation/.project"]
    task :eclipse_classpath => [".classpath", "tests/.classpath", "tests/instrumentation/.classpath"]
    task :eclipse => [:eclipse_project, :eclipse_classpath] 

    def generate_classpath(options)
      project = Builder::XmlMarkup.new
      project.instruct!
      project.classpath {
        options[:classpath].collect {|cp|
          project.classpathentry(:kind => cp[:kind], :path => cp[:path])
        }
      }
    end

    def generate_project(options)
      project = Builder::XmlMarkup.new
      project.instruct!
      project.projectDescription {
        project.name(options[:name])
        project.projects
        project.buildSpec {
          options[:commands].collect {|v|
            project.buildCommand {project.name(v); project.arguments;}
            } if options[:commands]
          }
          project.natures { options[:natures].collect{|n| project.nature(n)} } if options[:natures]
          project.linkedResources {
            options[:resources].collect { |l|
              project.link {
                project.name(options[:parent] + "_" + l)
                project.type(2)
                project.location( File.expand_path(__FILE__, l))
              }
            }
            } if options[:resources]
          }
        end
gem 'buildr-android', :version => '= 0.0.1'
require 'buildr-android'

define 'android_project' do
  include Android
  
  puts version # will output from manifest
  #eclipse :scala
  
  compile.with :android
  test.with :robolectric
  
  package(:apk).tap do |apk|
    apk[:key] = "location"
    apk[:password] = "" # if none will ask in the shell
  end 
end

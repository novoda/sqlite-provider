SQLiteProvider
================================================

A simplification of database access.

SQLiteProvider implements a ContentProvider for you that allows database access using [Uri][1]'s

Download
--------

Download [the latest JAR][2] or grab via

Gradle:
````xml
dependencies {
    compile 'com.novoda:sqliteprovider-core:1.0.0'
}
````
Maven:
````xml
<dependency>
    <groupId>com.squareup.picasso</groupId>
    <artifactId>picasso</artifactId>
    <version>(insert latest version)</version>
</dependency>
````

You will need to declare the Novoda repository:

Gradle:
````groovy
buildscript {
        repositories {
            maven {
                url "https://github.com/novoda/public-mvn-repo/raw/master/releases"
            }
        }
    }
````
Maven:
````xml
<repositories>
    <repository>
      <id>public-mvn-repo-releases</id>
      <url>https://github.com/novoda/public-mvn-repo/raw/master/releases</url>
    </repository>
</repositories>
````

Check the [WIKI][5] for further instruction

Usage
--------

Simple example source code can be found in this demo module: [Android Simple Demo][3]

Advanced queries & source code can be found in this demo module: [Android Extended Demo][4]


Overview
--------

The library is meant to augment the ContentProvider interface to fit SQLite in a more pronounced way. The aim is 
to set convention on queries via URI.

The following Uris are possible (some may not be supported yet):

### General Uri by convention

Generic table support:

	Uri: content://<authority>/tableName
	Sql: select * from "tableName"
	
Primary key support:

	Uri: content://<authority>/tableName/1
	Sql: select * from "tableName" where _id=1
	
One to many support:

	Uri: content://<authority>/tableName/1/child
	Sql: select * from "child" where tableName_id=1
	
Group by & having support: 

	Uri: content://<authority>/tableName?groupBy=col&having=value
	Sql: select * from tableName group by col having value
	
limit support:

	Uri: content://<authority>/tableName?limit=number
	Sql: select * from tableName limit number
	
distinct support:

	Uri: content://<authority>/tableName?distinct=true
	Sql: select distinct * from tableName limit number

join support (TODO):

	Uri: content://<authority>/parent/1/child/2
	Sql: select * from child inner join parent on parent._id=child.parent_id;


### Info Uri

Gives the ability to query information from the table SQLITE_MASTER and versionning.

   	content://<authority>/_info
   
Querying the above will yield the following cursor:

   	type TEXT | name TEXT | tbl_name TEXT | rootpage INTEGER | sql TEXT
   
If you are interested to only get the table name, the following should be helpful:

   	getContentResolver().query(Uri.parse("content://<authority>/_info"), new String[] {"name"}, null, null, null);
   
   
To get the current version of the database similar to "select sqlite_version() AS sqlite_version":

		content://<authority>/_info?version
	
returns the following cursor:

		sqlite_version TEXT
	
### Size management

Executing some file resizing using vacuum

	content://<authority>/_vacuum

(TODO) set max size??

### Pragma Uris


Ability to execute Pragma calls against SQLite. This is handy to get further info on table or set values such as "PRAGMA synchronous=OFF"

    content://<authority>/_pragma?<pragma_name>=<value>

For instance the following will exectute "PRAGMA synchronous=OFF"

    content://<authority>/_pragma?synchronous=OFF

The following will give back table information for a specific table:

    content://<authority>/_pragma?table_info("tableName")

The resulting cursor is
      
    column name | data type | can be NULL? | the default value for the column


### Table creation

If you insert against 

    content://<authority>/_db?create=<tableName>&withId=<true|false>&foreignKey=[<comma_separated_list_of_parent_tables>]&createdAt=false&updatedAt=false

(TODO) alter?

tableName (mandatory): the table name
withId (optional): automatically add "_id PRIMARY KEY AUTOINCREMENT" - default is true
foreignKey (optional): automatically add <fk_name>_id as foreign key to table creation
createdAt (optional): will put a createdAt field which contains creation date - default to insert
updatedAt (optional): will put a updatedAt field which contains update date - default to insert

you should have content values put with "column name" mapped to a SQLite type (look at SQLiteType)

		Uri uri = Uri.parse("content://authority/_db?create=table
		ContentValues values = new ContentValues();
		values.put("name", "TEXT");
		values.put("rid", "INTEGER");
		getContentResolver().insert(uri, values);

 [1]: http://developer.android.com/reference/android/net/Uri.html
 [2]: https://github.com/novoda/public-mvn-repo/raw/master/releases/com/novoda/sqliteprovider-core/1.0.0/sqliteprovider-core-1.0.0.jar
 [3]: https://github.com/novoda/SQLiteProvider/tree/master/demo-simple
 [4]: https://github.com/novoda/SQLiteProvider/tree/master/demo-extended
 [5]: https://github.com/novoda/SQLiteProvider/wiki

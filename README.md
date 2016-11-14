# sqlite-provider [![](https://ci.novoda.com/buildStatus/icon?job=sqlite-provider)](https://ci.novoda.com/job/sqlite-provider/lastBuild/console) [![Download](https://api.bintray.com/packages/novoda/maven/sqlite-provider/images/download.svg) ](https://bintray.com/novoda/maven/sqlite-provider/_latestVersion) [![](https://raw.githubusercontent.com/novoda/novoda/master/assets/btn_apache_lisence.png)](LICENSE.txt)

A simplification of database access for Android.


## Description

sqlite-provider implements a ContentProvider for you that allows database access using [Uri][1]s
The library is meant to augment the ContentProvider interface to fit SQLite in a more pronounced way. The aim is to set convention on queries via Uris.


## Adding to your project

To start using this library, add these lines to the `build.gradle` of your project:

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.novoda:sqlite-provider:2.0.0'
}
```

## Upgrading from < 1.0.4

Please note the name change from `sqliteprovider-core` to `sqlite-provider` when the version went beyond 1.0.4 (all the way back in 2014!). If you're upgrading from a version that old, don't forget to change the name too!


## Simple usage

Simple example source code can be found in this demo module: [Android Simple Demo][2]

Advanced queries & source code can be found in this demo module: [Android Extended Demo][3]


## Links

Here are a list of useful links:

 * We always welcome people to contribute new features or bug fixes, [here is how](https://github.com/novoda/novoda/blob/master/CONTRIBUTING.md)
 * If you have a problem check the [Issues Page](https://github.com/novoda/sqlite-provider/issues) first to see if we are working on it
 * For further usage or to delve more deeply checkout the [Project Wiki](https://github.com/novoda/sqlite-provider/wiki)
 * Looking for community help, browse the already asked [Stack Overflow Questions](http://stackoverflow.com/questions/tagged/support-sqlite-provider) or use the tag: `support-sqlite-provider` when posting a new question


 [1]: http://developer.android.com/reference/android/net/Uri.html
 [2]: https://github.com/novoda/SQLiteProvider/tree/master/demo-simple
 [3]: https://github.com/novoda/SQLiteProvider/tree/master/demo-extended

ArchBlock
=========

Builds are available! You can get them at [the Bamboo server](http://bamboo.gserv.me/browse/PLUG-BLOCK/latest).

---

Much of the plugin information has been [moved to BukkitDev](http://dev.bukkit.org/bukkit-plugins/archblock/) - You may find usage instructions there.

Developing
----------

I will provide some API documentation shortly.

* Maven repo: http://cherry.gserv.me/repos/maven
* Ivy repo: http://cherry.gserv.me/repos/ivy

### Maven

```xml
    <repositories>
        <!-- ... -->
        <repository>
          <id>gserv-me</id>
          <name>gserv.me</name>
          <url>http://cherry.gserv.me/repos/maven</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- ... -->
        <dependency>
            <groupId>com.archivesmc.archblock</groupId>
            <artifactId>ArchBlock</artifactId>
            <version>0.0.2</version>
        </dependency>
    <dependencies>
```

### Gradle

```groovy
repositories {
    // ...
    maven {
        url "http://cherry.gserv.me/repos/maven"
        name "gserv.me"
    }
}

dependencies {
    // ...
    compile "com.archivesmc.archblock:ArchBlock:0.0.2"
}
```

Compiling
---------

We use Gradle to compile, which should make life easier for everyone involved. Please
be aware that you will need at least Java 7 to both build and use this plugin.

#### Compiling on Windows

1. Clone this repository
2. `cd` into the directory you cloned to and `gradlew.bat`
3. Find your jar in `build/libs`

#### Compiling on Linux

1. Clone this repository
2. `cd` into the directory you cloned to and `gradlew`
3. Find your jar in `build/libs`

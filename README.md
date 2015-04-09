ArchBlock
=========
This is a second fork of the plugin, this is for the dutch translation of the plugin.

Builds are available! You can get them at [the Bamboo server](http://bamboo.gserv.me/browse/PLUG-BLOCK/latest).

---

Much of the plugin information has been [moved to the wiki](https://github.com/TheArchives/ArchBlock/wiki) - You may find usage instructions there. If you're looking for plugins that work with ArchBlock, try [this page](https://github.com/TheArchives/ArchBlock/wiki/Integrations).

Developing
----------

Looking to integrate your plugin with ArchBlock? Take a look at [the developer documentation](https://github.com/TheArchives/ArchBlock/wiki/Development) for more information.

Compiling
---------

We use Gradle to compile, which should make life easier for everyone involved. Please
be aware that you will need at least Java 7 to both build and use this plugin.

#### Compiling on Windows

1. Clone this repository
2. `cd` into the directory you cloned to and run `gradlew.bat`
3. Find your jar in `build/libs`

#### Compiling on Linux

1. Clone this repository
2. `cd` into the directory you cloned to
3. Make `gradlew` executable: `chmod +x ./gradlew`
4. Run the build steps: `./gradlew`
5. Find your jar in `build/libs`

Developer Guide
===============

So, you're looking to integrate your plugin with ArchBlock? Look no further!

The first thing you'll want to do is set up your dependencies with either the Ivy
or Maven repo. I highly suggest that you use Gradle to build your plugin, but feel
free to use Maven or Ant if you're more comfortable with that.

* Maven repo: http://cherry.gserv.me/repos/maven
* Ivy repo: http://cherry.gserv.me/repos/ivy

When creating your build script, please be careful to never shade Archblock into your
JAR using Maven's Shade plugin or Gradle's FatJar or ShadowJar plugins. If you happen
to include ArchBlock's classes inside your plugin, you will most likely have strange
errors and warnings, and things could break in very strange ways, so **please make**
**sure you don't do this**.

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

### Ant

```
I couldn't come up with enough information on how to use Ant through
googling; if anyone uses Ant then please submit a pull request!
```

Recommended code paths
======================

When using ArchBlock, you can go one of two ways. You can either make your
plugin require ArchBlock to function, or you can use it to provide an
optional integration. The path you choose depends on what your plugin is
supposed to do, but both approaches will be covered here.

Using ArchBlock as an optional dependency
-----------------------------------------

If you just want to extend your plugin's existing functionality with support
for ArchBlock, this may be the route you want to take.

* Edit your plugin.yml to add ArchBlock as a soft dependency.

  ```yaml
  softdepend: [ArchBlock]
  ```

  This will ensure that your plugin loads only once ArchBlock has.

* Add a method to your plugin to detect whether ArchBlock is enabled or not.

  ```java
  // ...

  public class MyPlugin extends JavaPlugin {
      // ...

      public Boolean hasArchBlock() {
          return this.getServer().getPluginManager().isPluginEnabled("ArchBlock");
      }
  }
  ```

* Create a separate class that will handle all of your integration tasks

  ```java
  // ...
  import com.archivesmc.archblock.Plugin;

  public class ArchBlockIntegration {
      private MyPlugin plugin;
      private ArchBlock archBlockApi;

      public ArchBlockIntegration(Myplugin plugin) {
          this.plugin = plugin;
          Plugin archBlockPlugin = (Plugin) this.plugin.getServer().getPluginManager().getPlugin("ArchBlock");
          this.archBlockApi = archBlockPlugin.getApi();
      }

      public void doSomethingWithArchBlock() {
          // ...
      }
  }
  ```

* Finally, load your integration class when your plugin is enabled. Make sure you
  have null checks in appropriate places, too.

  ```java
  // ...
  private ArchBlockIntegration archBlockIntegration = null;

  public class MyPlugin extends JavaPlugin {
      // ...

      @Override
      public void onEnable() {
          // ...

          if (this.hasArchblock()) {
              this.archBlockIntegration = new ArchBlockIntegration(this);
          }
      }

      public void doSomethingWithArchBlock() {
          if (this.archBlockIntegration != null) {
              this.archBlockIntegration.doSomethingWithArchBlock();
          }
      }
  }
  ```
  
The goal here is to avoid importing any ArchBlock classes when ArchBlock isn't available.
If you try to import them without ArchBlock loaded, you'll get some exceptions and your
plugin will fail to load at all.

Using ArchBlock as a required dependency
----------------------------------------

If your plugin requires ArchBlock to function, this may be the route for you.

* Edit your plugin.yml and declare ArchBlock as a required dependency.

  ```yaml
  depend: [ArchBlock]
  ```

  This will ensure your plugin only loads if ArchBlock is available.

* Get an instance of the ArchBlock API on startup, ensuring that the plugin you're
  getting isn't actually some other plugin with the same name.

  ```java
  // ...
  import com.archivesmc.archblock.Plugin;

  public class MyPlugin extends JavaPlugin {
  // ...
      private ArchBlock archBlockApi;

      @Override
      public void onEnable() {
          // ...

          if (!this.setupArchBlock()) {
             this.getLogger.critical("This plugin requires ArchBlock to function properly!");
             this.getServer().getPluginManager().disablePlugin(this);
             return;
          }
      }

      public Boolean setupArchBlock() {
          JavaPlugin p = this.getServer().getPluginManager().getPlugin("ArchBlock");

          if (p instanceof Plugin) {
              this.archBlockApi = ((Plugin) p).getApi();
              return true;
          } else {
              return false;
          }
      }
  }
  ```

This is arguably a simpler path, but please don't take it unless your plugin is totally
unable to function with ArchBlock.

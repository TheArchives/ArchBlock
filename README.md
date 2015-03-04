This plugin is not finished!
============================

You can see my [TODO ticket](https://github.com/TheArchives/ArchBlock/issues/1) for more information on what's left.

ArchBlock
=========

Builds are available! You can get them at [my Bamboo server](http://bamboo.gserv.me/browse/PLUG-BLOCK/latest).

---

ArchBlocks provides a fairly revolutionary block protection system. We're tired
of the plot plugins, of the buyable regions and precious-stones-style area claims.

While these methods do work, we find them too restricting. If you don't set some kind of
limit, then it's very easy to abuse plugins that work in this way. They also aren't
exactly automatic - they take a fair amount of work and learning to use efficiently.

Instead, let's consider a different approach. What if you had every block protected
separately, just as you place it? What if you could then allow your friends to edit
your blocks and build upon them?

[WatchBlock](http://dev.bukkit.org/bukkit-plugins/watchblock-refired/) introduced
this concept to the Bukkit community, but every project has its day, and this project
had its one several years ago.

---

I [attempted to fix up](https://github.com/gdude2002/WatchBlock) WatchBlock some time
ago, and the code was horrendous - I'm not entirely sure how it was able to compile
in the first place. It's very buggy, very messy, and contains some.. morally questionable
code. It also hasn't seen a release since the summer of 2013.

As this plugin is very important to my network, it stands to reason that rewriting it
and adding the features that we've always wanted could be a good idea. It's clear that
the developers of WatchBlock don't really care about it any more, so.. let's get stuck
in.

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

What exactly is this plugin?
----------------------------

This is a simple protection plugin from the perspective of the user, and it protects from the
following things.

* Users breaking other players' blocks without permission
* Users placing blocks against other players' blocks without permission
* Users placing a piston next to a protected block and pushing that block without permission
  * This also extends to connected blocks, eg blocks pushing other blocks
* Users placing a sticky piston next to a protected block and pulling it out of place without
  permission
  * This also extends to connected blocks (eg, slime blocks) but may cause strange visual issues.
    Please note, the blocks being pulled may appear to disappear client-side, but in reality, they're
    still there. This appears to be a bug in either Bukkit or Minecraft itself, and you can fix it
    by relogging, changing world, or simply going far away enough to unload the affected chunks.

What **isn't** this plugin?
---------------------------

* This is not an automated region protection plugin - We hate PlotMe, and feel like it tends to stifle
  Creative servers by limiting users to small plots, and so this is our answer to that problem.
* This is not a chunk- or area-ownership plugin - We feel like plugins that take the PreciousStones
  approach can be annoying to use when all you want to do is build.
* This is not a region-ownership plugin - WorldGuard does this extremely well, and this isn't a
  protection model we really wanted to look at in the first place.
* This is not a commercial plugin - You are free to use this plugin on your servers, however you
  like, as long as you abide by [our LICENSE file](https://github.com/TheArchives/ArchBlock/blob/master/LICENSE).

Using the plugin
----------------

Want to try this plugin? Great! Be aware, though, that the setup process is fairly
involved - you are expected to be experienced with this kind of thing. If you're having
problems, please read the setup and installation guides fully.

#### Setting up

First things first - you'll need to set up some plugins. There are three of them, make sure
they're up to date!

* [WorldEdit](http://dev.bukkit.org/bukkit-plugins/worldedit/) - You'll need at least version 6.x.
* [WorldGuard](http://dev.bukkit.org/bukkit-plugins/worldguard/) - Likewise, you'll need at least
  version 6.x.
* [WGCustomFlags](http://dev.bukkit.org/bukkit-plugins/worldguard-custom-flags/) - You'll need the
  [beta build](http://ci.mewin.de/job/WGCustomFlags/) of this one.

If there's enough demand, I'll make these dependencies optional, but for now, they're all required.

#### Installing

1. Stop your server. No, really, please do this.
2. Download the plugin JAR. You can find the latest one [at the build server](http://bamboo.gserv.me/browse/PLUG-BLOCK/latest/).
3. Ensure that the plugin JAR is in your `plugins/` directory, and that the other required plugins
   are installed, and then start your server. You will see some errors.
4. Stop the server again, and fill out `plugins/ArchBlock/config.yml`. See below for details.
5. Set up your permissions. See below for details on that as well.
6. Finally, start your server. The plugin will create its tables, and you'll be ready to go.

#### Configuration

If you simply need to redownload your configuration,
[you can find the bundled config file here](https://github.com/TheArchives/ArchBlock/blob/master/src/main/resources/config.yml).

* `enabled` - This is set to `false` by default. Set it to `true` when you're sure all your configuration
  is correct. Doing so too early will cause your server to crash on load, as the threadpool will keep trying
  to get a connection.
* `db_config` - This section is for specifying your database connection details.
    * `jdbc_driver` - The JDBC driver to use for your database. Bukkit ships with the MySQL driver,
      but you may prefer to provide your own here. If you need to download one, you should be able
      to put the `.jar` file in the `libs/` folder, creating it if it isn't there, but this is untested.
    * `hibernate_dialect` - The Hibernate SQL dialect to use. This should match your database as well.
      You can get a list of dialects [here](https://docs.jboss.org/hibernate/orm/3.5/api/org/hibernate/dialect/package-summary.html),
      and you can specify these in the configuration in the form `"libs.org.hibernate.dialect.DIALECT_NAME"`.
      Please note the `libs.` prefix - this is to refer to the version of Hibernate that's included with the
      plugin.
    * `connection_url` - The JDBC connection URL for your database. For more information on constructing these,
      see the documentation for the `jdbc_driver` you supplied above. For reference, you can see how the
      [MySQL driver](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html)
      does it.
    * `username` - The username to use when connecting to the database.
    * `password` - The password to use when connecting to the database.
    * `debug` - Set this to `true` to output all generated SQL to the terminal. Note that this will slow your
      server down considerably, so you should only use it for debugging purposes.
* `version` - Don't touch this. This value is used for automated configuration migrations, and will be
  updated automatically along with the plugin. If you edit this, you'll probably break your config.

#### Permissions

ArchBlock only has a few permissions.

* `archblock.*` - Gives access to all admin commands and protection bypasses
    * `archblock.admin` - Give this to your staff, if you want them to have access to `/setowner` and
      any other admin commands.
    * `archblock.bypass` - Give this to your staff, if you want them to be able to break any block
      regardless of their position in peoples' friends lists.

#### Usage

Once your plugin has been set up and is working, that's it. Any blocks that are placed in any worlds,
assuming the world isn't in the list of disabled worlds or a designated WorldGuard region, will be
automatically protected. Your players get access to the following commands by default.

* `/friend <user>` - Add another user to one's friends list, which allows them to edit one's blocks.
    * This command is not reciprocal - If **User A** adds **User B** to their friends list, **User B**
      will be able to edit **User A's** blocks, but **User A** will not be able to edit **User B'**
      blocks unless they also add **User A** as a friend.
* `/unfriend <user>` - Remove a user from one's friends list, preventing them from editing one's blocks
  once again.
* `/friends` - Shows a list of the users one has added to their friends list.

Anyone with `archblock.admin` additionally gets access to the following.

* `/setowner <user>` - Having made a cuboid selection with WorldEdit as normal, this will set the owner
  of the selected blocks to the specified user. As such, this is intensive and may take a while, but it
  does occur in another thread, so it shouldn't lock up the server.
    * Please note, this doesn't get all of the selected blocks right now. We're still looking into this.

Finally, if the user commands are run from the console, a command block, a CraftIRC user, or basically
anything that isn't a player, they may be run on other users.

* `/friend <user> <friend>` - Adds `<friend>` to `<user>`'s friends list.
* `/unfriend <user> <friend>` - Removes `<friend>` from `<user>`'s friends list.
* `/friends <user>` - Shows a list of the friends on `<user>`'s friends list.

#### WorldGuard integration

As of right now, we use the **WGCustomFlags** plugin to add a flag to every WorldGuard region. If
you would like to disable protection in any region, you may set the flag `bypass-protection` to
`true`, and no edits, piston movements, etc will be checked there. WorldGuard 6.x has planned support
for custom flags, so we'll eventually be able to implement this ourselves, and thus we won't need
to use **WGCustomFlags** for it.

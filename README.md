ArchBlock
=========

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
2. `cd` into the directory you cloned to and `gradlew.bat shadowjar`
3. Find your jar in `build/libs`

#### Compiling on Linux

1. Clone this repository
2. `cd` into the directory you cloned to and `gradlew shadowjar`
3. Find your jar in `build/libs`

Using the plugin
----------------

No documentation here yet, sorry! I'll provide some as development continues.

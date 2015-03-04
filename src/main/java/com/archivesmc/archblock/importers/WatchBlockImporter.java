package com.archivesmc.archblock.importers;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;
import tk.minecraftopia.watchblock.WatchBlock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class WatchBlockImporter implements Importer{
    private Plugin plugin;
    private WatchBlock watchBlockPlugin;
    private File watchBlockConfigDir;

    private final Gson gson = new Gson();
    private final List<String> worlds = new ArrayList<>();

    public WatchBlockImporter(Plugin plugin) {
        this.plugin = plugin;
        this.watchBlockPlugin = (WatchBlock) this.plugin.getServer().getPluginManager().getPlugin("WatchBlock");
        this.watchBlockConfigDir = this.watchBlockPlugin.getDataFolder();
    }

    @Override
    public Boolean doImport() {
        this.info("Beginning WatchBlock import..");

        Boolean result;

        result = this.getWorlds();

        if (!result) {
            return result;
        }

        result = this.convertFriends();

        if (!result) {
            return result;
        }

        return true;
    }

    private Boolean getWorlds() {
        this.info("Looking for worlds to import..");

        List<File> dirs = Utils.listDirectories(this.watchBlockConfigDir);

        for (File f : dirs) {
            this.worlds.add(f.getName());
        }

        this.info(String.format(
                "Found worlds (%s): %s", this.worlds.size(), StringUtils.join(this.worlds, ", ")
        ));

        return true;
    }

    private Boolean convertFriends() {
        this.info("Converting friendships..");

        File friendsFile = new File(this.watchBlockConfigDir, "allow.yml");

        Yaml yaml = new Yaml();
        try {
            Map<String, Map<String, Boolean>> data = (Map) ((Map) yaml.load(new FileInputStream(friendsFile))).get("allow");
            Map<String, Boolean> friends;

            String left;
            String right;

            UUID leftUuid;
            UUID rightUuid;

            for (String player : data.keySet()) {
                this.doFetchUuid(player);
                friends = data.get(player);

                for (String friend : friends.keySet()) {
                    this.doFetchUuid(friend);
                }

                left = this.plugin.getApi().getUuidForUsername(player);

                if (left != null) {
                    leftUuid = UUID.fromString(left);

                    for (String friend : friends.keySet()) {
                        right = this.plugin.getApi().getUuidForUsername(friend);

                        if (right != null) {
                            rightUuid = UUID.fromString(right);

                            if (! this.plugin.getApi().hasFriendship(leftUuid, rightUuid)) {
                                this.plugin.getApi().createFriendship(leftUuid, rightUuid);
                                this.info(String.format(
                                        "Created friendship: %s -> %s",
                                        player, friend
                                ));
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        this.info("Friendships converted.");
        return true;
    }

    private void doFetchUuid(String player) throws InterruptedException {
        String stringUuid;
        UUID uuid;

        stringUuid = this.plugin.getApi().getUuidForUsername(player);

        if (stringUuid == null) {
            uuid = Utils.fetchUuid(player);
            this.plugin.getApi().storePlayer(uuid, player);

            if (uuid == null) {
                this.warning(String.format("Unable to fetch UUID for player: %s", player));
            } else {
                this.info(String.format("Fetched UUID for player: %s", player));
            }

            Thread.sleep(1500);  // Mojang rate-limiting..
        }
    }

    private void info(String message) {
        this.plugin.getLogger().info(
                String.format("IMPORT | %s", message)
        );
    }

    private void warning(String message) {
        this.plugin.getLogger().warning(
                String.format("IMPORT | %s", message)
        );
    }
}

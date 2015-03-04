package com.archivesmc.archblock.importers;

import com.archivesmc.archblock.Plugin;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import tk.minecraftopia.watchblock.WatchBlock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class WatchBlockImporter implements Importer{
    private Plugin plugin;
    private WatchBlock watchBlockPlugin;

    private final List<String> worlds = new ArrayList<>();
    private final Map<Integer, String> playerMappings = new HashMap<>();

    public WatchBlockImporter(Plugin plugin) {
        this.plugin = plugin;
        this.watchBlockPlugin = (WatchBlock) this.plugin.getServer().getPluginManager().getPlugin("WatchBlock");
    }

    private Boolean getWorlds() {
        Connection conn = this.watchBlockPlugin.getConnection();
        ResultSet rs;

        Statement statement;

        // Let's start off by getting the worlds.
        try {
            statement = conn.createStatement();
            statement.execute("SELECT worldname FROM worlds");
            rs = statement.getResultSet();

            while (rs.next()) {
                this.worlds.add(rs.getString("worldname"));
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error while getting world names!");
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        this.plugin.getLogger().info(String.format(
                "Found worlds (%s): %s",
                this.worlds.size(), StringUtils.join(this.worlds, ", ")
        ));

        return true;
    }

    private Boolean getPlayers() {
        Connection conn = this.watchBlockPlugin.getConnection();
        ResultSet rs;

        Statement statement;

        try {
            // Firstly, for the sake of informing the server staff, work out how many we need to convert.
            statement = conn.createStatement();
            statement.execute("SELECT count(*) FROM players");
            rs = statement.getResultSet();
            rs.next();

            Integer numberOfPlayersToConvert = rs.getInt(0);

            rs.close();
            statement.close();

            this.plugin.getLogger().info(String.format(
                    "Converting %s usernames to UUIDs. This may take some time!",
                    numberOfPlayersToConvert
            ));

            // After that, we'll get the actual usernames and internal IDs.
            statement = conn.createStatement();
            statement.execute("SELECT pid, playername FROM players");
            rs = statement.getResultSet();


            this.plugin.getLogger().info(
                    "Due to Mojang's rate-limiting, we'll be doing this at a rate of one username every " +
                            "1.5-2.0 seconds."
            );

            this.plugin.getLogger().info(String.format(
                    "Thus, this should take around %s seconds to complete, but may be longer!",
                    2 * numberOfPlayersToConvert
            ));

            InputStream in;
            String urlData;
            Gson gson = new Gson();
            Map jsonData;
            UUID uuid;

            while (rs.next()) {
                try {
                    in = new URL(String.format(
                            "https://api.mojang.com/users/profiles/minecraft/%s", rs.getString("playername")
                    )).openStream();

                    urlData = IOUtils.toString(in);
                    IOUtils.closeQuietly(in);

                    jsonData = gson.fromJson(urlData, Map.class);
                    uuid = UUID.fromString((String) jsonData.get("id"));

                    this.plugin.getApi().storePlayer(uuid, rs.getString("playername").toLowerCase());
                    this.plugin.getLogger().info(String.format(
                            "Got UUID for %s: %s", rs.getString("playername"), uuid.toString()
                    ));

                    this.playerMappings.put(rs.getInt("pid"), rs.getString("playername"));
                } catch (IOException e) {
                    this.plugin.getLogger().warning(String.format(
                            "Unable to find UUID for username: %s", rs.getString("playername")
                    ));
                }

                Thread.sleep(1500);
            }

            this.plugin.getLogger().info(String.format(
                    "Converted %s/%s usernames successfully.",
                    this.playerMappings.size(), numberOfPlayersToConvert
            ));
            this.plugin.getLogger().info(
                    "Be aware that any players that weren't converted will lose their block protections, " +
                    "but WatchBlock wouldn't have been able to prevent that anyway."
            );
        } catch (SQLException e) {
            this.plugin.getLogger().warning("Error while getting player UUIDs!");
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return true;
    }

    @Override
    public Boolean doImport() {
        this.plugin.getLogger().info("Beginning WatchBlock import..");

        Boolean result;

        result = this.getWorlds();

        if (!result) {
            return result;
        }

        result = this.getPlayers();

        if (!result) {
            return result;
        }

        return true;
    }
}

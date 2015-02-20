package com.archivesmc.archblock.storage.database;

import com.archivesmc.archblock.Plugin;
import com.archivesmc.archblock.storage.StorageHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bukkit.World;

import java.sql.*;
import java.util.List;

public class MySQL implements StorageHandler {
    private Plugin plugin;

    private String username;
    private String password;
    private String hostname;
    private Integer port;
    private String database;

    private Connection connection;

    public MySQL(Plugin plugin, String username, String password, String hostname, Integer port, String database) {
        this.plugin = plugin;

        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
    }

    @SuppressFBWarnings(value="OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE", justification="False positive")
    @Override
    public Boolean setup() {
        try {
            // Load up the MySQL driver in the weirdest but most accepted way possible
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        PreparedStatement statement = null;

        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%d/%s", this.hostname, this.port, this.database),
                    this.username, this.password
            );

            // First, make sure the database exists.
            // Unfortunately we can't use a prepared statement for this.
            statement = this.connection.prepareStatement(
                    String.format("CREATE DATABASE IF NOT EXISTS %s", this.database)
            );
            statement.execute();
            statement.close();

            // Next, use it..
            statement = this.connection.prepareStatement(
                    String.format("USE %s", this.database)
            );
            statement.execute();
            statement.close();

            // Next up, create the non-world tables.
            if (!this.hasTable("players")) {
                // Stores UUIDs to player names
                statement = this.connection.prepareStatement(
                        "CREATE TABLE players(" +
                            "UUID VARCHAR(255) PRIMARY KEY," +
                            "username VARCHAR(255)" +
                        ")"
                );
                statement.execute();
                statement.close();

                this.plugin.getLogger().info("Created 'players' table.");
            }

            if (!this.hasTable("friends")) {
                // Stores friendship relations
                statement = this.connection.prepareStatement(
                        "CREATE TABLE friends(" +
                            "UUID VARCHAR(255) PRIMARY KEY," +
                            "friend VARCHAR(255)" +
                        ")"
                );
                statement.execute();
                statement.close();

                this.plugin.getLogger().info("Created 'friends' table.");
            }

            // Alright, now set up the tables for each world, ignoring disabled ones
            List<String> disabledWorlds = this.plugin.getDisabledWorlds();

            for (World w : this.plugin.getServer().getWorlds()) {
                if (disabledWorlds.contains(w.getName())) {
                    continue;
                }

                if (!this.hasTable(String.format("%s_blocks", w.getName()))) {
                    statement = this.connection.prepareStatement(String.format(
                            "CREATE TABLE %s_blocks(" +
                                "UUID VARCHAR(255)," +
                                "x BIGINT," +
                                "y BIGINT," +
                                "z BIGINT," +
                                "PRIMARY KEY(x, y, z)" +
                            ")"
                    , w.getName()));

                    statement.execute();
                    statement.close();

                    this.plugin.getLogger().info(String.format("Created table for world: %s", w.getName()));
                }
            }

            this.plugin.getLogger().info("MySQL database has been set up.");
        } catch (SQLException e) {
            e.printStackTrace();

            if (statement != null) {
                try {
                    if (!statement.isClosed()) {
                        statement.close();
                    }
                } catch (SQLException f) {
                    f.printStackTrace();
                }
            }

            return false;
        }

        return true;
    }

    private boolean hasTable(String table) {
        try {
            DatabaseMetaData metaData = this.connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, table, null);

            if (resultSet.next()) {
                return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Implementation
}

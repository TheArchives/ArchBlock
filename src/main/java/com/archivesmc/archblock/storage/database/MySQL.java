package com.archivesmc.archblock.storage.database;

import com.archivesmc.archblock.storage.StorageHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL implements StorageHandler {
    private String username;
    private String password;
    private String hostname;
    private Integer port;
    private String database;

    private Connection connection;

    public MySQL(String username, String password, String hostname, Integer port, String database) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
    }

    @Override
    public Boolean setup() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%d/%s", this.hostname, this.port, this.database),
                    this.username, this.password
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

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

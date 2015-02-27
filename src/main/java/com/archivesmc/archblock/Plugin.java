package com.archivesmc.archblock;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.config.MainConfig;
import org.bukkit.plugin.java.JavaPlugin;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Plugin extends JavaPlugin {
    private ArchBlock api;
    private MainConfig mainConfig;

    private Configuration hibernateConfiguration;
    private SessionFactory sessionFactory;

    @Override
    public void onLoad() {
        this.api = new ArchBlock(this);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.mainConfig = new MainConfig(this);

        this.hibernateConfiguration = new Configuration().configure()
                .setProperty("hibernate.dialect", this.mainConfig.getDatabaseDialect())                // org.hibernate.dialect.MySQL5Dialect
                .setProperty("hibernate.connection.driver_class", this.mainConfig.getDatabaseDriver()) // com.mysql.jdbc.Driver
                .setProperty("hibernate.connection.url", this.mainConfig.getDatabaseURL())             // jdbc:mysql://localhost:3306/archblock
                .setProperty("hibernate.connection.username", this.mainConfig.getDatabaseUsername())   // correct_username
                .setProperty("hibernate.connection.password", this.mainConfig.getDatabasePassword())   // correct_password
                .setProperty("show_sql", this.mainConfig.getDatabseDebug() ? "true" : "false");        // false

        this.sessionFactory = this.hibernateConfiguration.buildSessionFactory();

        Session session = this.getSession();

        this.getLogger().info(String.format(
                "Loaded! Found %s players.", session.createQuery("select count(*) from Player").uniqueResult()
        ));

        session.close();
    }

    @Override
    public void onDisable() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }

    public ArchBlock getApi() {
        return this.api;
    }

    public Session getSession() {
        return this.sessionFactory.openSession();
    }

    public List<String> getDisabledWorlds() {
        return this.mainConfig.getDisabledWorlds();
    }
}

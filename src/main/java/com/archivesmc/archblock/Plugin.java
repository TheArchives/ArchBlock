package com.archivesmc.archblock;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.config.MainConfig;
import com.archivesmc.archblock.storage.entities.Block;
import com.archivesmc.archblock.storage.entities.Friendship;
import com.archivesmc.archblock.storage.entities.Player;
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
                .setProperty("show_sql", "false");

        this.sessionFactory = this.hibernateConfiguration.buildSessionFactory();

//        new SchemaExport(this.hibernateConfiguration).create(true, false);

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        Block block = new Block();
        block.setUuid("???");
        block.setX(0L);
        block.setY(0L);
        block.setZ(0L);
        block.setWorld("world");

        Friendship friendship = new Friendship();
        friendship.setPlayerUuid("???");
        friendship.setFriendUuid("!!!");

        Player player = new Player();
        player.setUuid("???");
        player.setUsername("arbot");

        session.save(block);
        session.save(friendship);
        session.save(player);

        session.getTransaction().commit();
        session.flush();
        session.close();
    }

    @Override
    public void onDisable() {
        this.sessionFactory.close();
    }

    public ArchBlock getApi() {
        return this.api;
    }

    public List<String> getDisabledWorlds() {
        return this.mainConfig.getDisabledWorlds();
    }
}

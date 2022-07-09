package fr.borane.simpleperms;

import fr.borane.simpleperms.commands.Demote;
import fr.borane.simpleperms.commands.Promote;
import fr.borane.simpleperms.commands.User;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
    public static Database db;
    public static Main INSTANCE;
    public HashMap<UUID, Integer> playersGroup = new HashMap<>();

    @Override
    public void onEnable(){
        INSTANCE = this;
        saveDefaultConfig();
        db = new Database(getConfig().getString("sql.host"),
                getConfig().getString("sql.user"),
                getConfig().getString("sql.pass"),
                getConfig().getString("sql.name"),
                getConfig().getInt("sql.port"));

        getCommand("promote").setExecutor(new Promote());
        getCommand("demote").setExecutor(new Demote());
        getCommand("user").setExecutor(new User());

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
    }

    @Override
    public void onDisable(){
        try {
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Main getInstance(){
        return INSTANCE;
    }
}

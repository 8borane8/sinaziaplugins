package fr.borane.hower.bungee;

import fr.borane.hower.bungee.commands.*;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Main extends Plugin {

    private static Main instance;

    @Override
    public void onEnable(){
        instance = this;
        createFile("config");

        getProxy().getPluginManager().registerCommand(this, new Lobby());
        getProxy().getPluginManager().registerCommand(this, new Pban());
        getProxy().getPluginManager().registerCommand(this, new Tban());
        getProxy().getPluginManager().registerCommand(this, new Pmute());
        getProxy().getPluginManager().registerCommand(this, new Tmute());
        getProxy().getPluginManager().registerCommand(this, new Punban());
        getProxy().getPluginManager().registerCommand(this, new Punmute());
        getProxy().getPluginManager().registerCommand(this, new Faction());
        getProxy().getPluginManager().registerCommand(this, new Minage());

        getProxy().getPluginManager().registerListener(this, new EventListener());
    }

    @Override
    public void onDisable(){

    }

    public static Main getInstance(){
        return instance;
    }

    private void createFile(String fileName){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), fileName + ".yml");
        if(!file.exists()){
            try {
                file.createNewFile();

                if(fileName.equals("config")){
                    Configuration config = getConfig(fileName);
                    config.set("lobby", "lobby");
                    config.set("faction", "faction");
                    config.set("minage", "minage");
                    config.set("messages.pban", "You are been banned by %p for %r");
                    config.set("messages.tban", "You are been banned by %p for %r during %d at %t");
                    config.set("messages.pmute", "You are been mutted by %p for %r");
                    config.set("messages.tmute", "You are been mutted by %p for %r during %d at %t");
                    config.set("messages.default_raison", "Default Raison");
                    saveConfig(config, fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Configuration getConfig(String fileName){
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig(Configuration config, String fileName){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), fileName + ".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

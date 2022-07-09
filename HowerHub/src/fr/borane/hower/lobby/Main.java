package fr.borane.hower.lobby;

import fr.borane.hower.lobby.commands.Setspawn;
import fr.borane.hower.lobby.commands.Spawn;
import fr.borane.hower.lobby.utils.EventsListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    public static Main INSTANCE;

    @Override
    public void onEnable(){
        INSTANCE = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new EventsListener(), this);

        getCommand("spawn").setExecutor(new Spawn(this));
        getCommand("setspawn").setExecutor(new Setspawn(this));

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(Player player : Main.INSTANCE.getServer().getOnlinePlayers()){

                    player.setSaturation(20);
                    player.setFoodLevel(20);
                    if(player.getLocation().getY() < 80){
                        Location spawn = new Location(Main.INSTANCE.getServer().getWorlds().get(0), Main.INSTANCE.getConfig().getDouble("spawn.posX"),
                                Main.INSTANCE.getConfig().getDouble("spawn.posY"),
                                Main.INSTANCE.getConfig().getDouble("spawn.posZ"),
                                (float)Main.INSTANCE.getConfig().getDouble("spawn.yam"),
                                (float)Main.INSTANCE.getConfig().getDouble("spawn.pitch"));
                        player.teleport(spawn);
                    }
                }
            }
        }, 1, 1);
    }

    @Override
    public void onDisable(){

    }
}

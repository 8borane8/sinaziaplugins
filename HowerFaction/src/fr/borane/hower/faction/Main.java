package fr.borane.hower.faction;

import fr.borane.hower.faction.commands.*;
import fr.borane.hower.faction.utils.Database;
import fr.borane.hower.faction.utils.EventsListener;
import net.minecraft.server.v1_7_R4.EntityHuman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin{

    public static Main INSTANCE;
    public static Database db;
    public static Integer clearlag = 240;
    public static List<UUID> freez_player = new ArrayList<>();
    public static HashMap<UUID, Long> fight_player = new HashMap<>();

    @Override
    public void onEnable(){
        INSTANCE = this;
        saveDefaultConfig();
        db = new Database(
                getConfig().getString("sql.host"),
                getConfig().getString("sql.user"),
                getConfig().getString("sql.pass"),
                getConfig().getString("sql.name"),
                getConfig().getInt("sql.port")
        );
        getServer().getPluginManager().registerEvents(new EventsListener(), this);

        getCommand("spawn").setExecutor(new Spawn());
        getCommand("setspawn").setExecutor(new Setspawn());
        getCommand("home").setExecutor(new Home());
        getCommand("sethome").setExecutor(new Sethome());
        getCommand("delhome").setExecutor(new Delhome());
        getCommand("money").setExecutor(new Money());
        getCommand("pay").setExecutor(new Pay());
        getCommand("clearlag").setExecutor(new Clearlag());
        getCommand("freez").setExecutor(new Freez());
        getCommand("unfreez").setExecutor(new Unfreez());
        getCommand("top").setExecutor(new Top());
        getCommand("vanish").setExecutor(new Vanish());
        getCommand("faction").setExecutor(new Faction());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("warp").setExecutor(new Warp());
        getCommand("setwarp").setExecutor(new Setwarp());
        getCommand("delwarp").setExecutor(new Delwarp());

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(clearlag == 0){
                    clearlag = 240;
                    getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.clearlag").replaceAll("&", "§"));

                    for (LivingEntity entity : getServer().getWorlds().get(0).getLivingEntities()) {
                        if (!(entity instanceof Player)) {
                            entity.remove();
                        }
                    }
                    for (Entity entity : getServer().getWorlds().get(0).getEntities()) {
                        if (!(entity instanceof Player)) {
                            entity.remove();
                        }
                    }

                    System.gc();
                    return;
                }
                clearlag--;
            }
        }, 20l, 20);
    }

    @Override
    public void onDisable(){

    }
}

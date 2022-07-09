package fr.borane.hower.faction.utils;

import com.mysql.jdbc.Statement;
import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

public class EventsListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Location spawn = new Location(Main.INSTANCE.getServer().getWorlds().get(0), Main.INSTANCE.getConfig().getDouble("spawn.posX"),
                Main.INSTANCE.getConfig().getDouble("spawn.posY"),
                Main.INSTANCE.getConfig().getDouble("spawn.posZ"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.yam"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.pitch"));
        event.setRespawnLocation(spawn);
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event){
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
        Main.INSTANCE.getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.quit_message").replaceAll("&", "§").replaceAll("%pseudo%", event.getPlayer().getName()));

        if(Main.fight_player.containsKey(event.getPlayer().getUniqueId()) == false){ return; }
        else if(Main.fight_player.get(event.getPlayer().getUniqueId()) + 30 * 1000 < new Timestamp(System.currentTimeMillis()).getTime()){ return; }
        event.getPlayer().setHealth(0D);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        try {
            PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?");
            preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            int factionid = 0;
            if(!resultSet.next()){
                preparedStatement = Main.db.getConnection().prepareStatement("INSERT INTO users (uuid) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    factionid = rs.getInt(1);
                }
            }else{
                factionid = resultSet.getInt("faction");
            }

            preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM factions WHERE id = ?");
            preparedStatement.setInt(1, factionid);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){ return; }
            String faction = resultSet.getString("name");
            event.getPlayer().setDisplayName("§8" + faction + "§f " + event.getPlayer().getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        event.setJoinMessage(null);
        Main.INSTANCE.getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.join_message").replaceAll("&", "§").replaceAll("%pseudo%", event.getPlayer().getName()));
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) throws SQLException {
        PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?");
        preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){ return; }

        preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM factions WHERE id = ?");
        preparedStatement.setInt(1, resultSet.getInt("faction"));
        resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){ return; }
        String faction = resultSet.getString("name");

        event.setCancelled(true);
        if(event.getPlayer().hasPermission("hower.faction.specific.chatcolor")){
            Main.INSTANCE.getServer().broadcastMessage("§8»"+faction+"§f " + event.getPlayer().getName() + ": " + event.getMessage().replaceAll("&", "§"));
        }else{
            Main.INSTANCE.getServer().broadcastMessage("§8»"+faction+ "§f " + event.getPlayer().getName() + ": " + event.getMessage());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(Main.freez_player.contains(event.getPlayer().getUniqueId()) &&
                (event.getTo().getX() != event.getFrom().getX() || event.getTo().getY() != event.getFrom().getY() || event.getTo().getZ() != event.getFrom().getZ())){
            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(Main.INSTANCE.getConfig().getString("messages.can_move_freez").replaceAll("&", "§"));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)){
            return;
        }
        Main.fight_player.put(event.getDamager().getUniqueId(), new Timestamp(System.currentTimeMillis()).getTime());
        Main.fight_player.put(event.getEntity().getUniqueId(), new Timestamp(System.currentTimeMillis()).getTime());

        if(Main.fight_player.containsKey(event.getDamager().getUniqueId()) == false && Main.fight_player.get(event.getDamager().getUniqueId()) + 30 * 1000 < new Timestamp(System.currentTimeMillis()).getTime()){
            ((Player) event.getDamager()).sendMessage(Main.INSTANCE.getConfig().getString("messages.fight").replaceAll("&", "§"));
        }
        if(Main.fight_player.containsKey(event.getEntity().getUniqueId()) == false && Main.fight_player.get(event.getEntity().getUniqueId()) + 30 * 1000 < new Timestamp(System.currentTimeMillis()).getTime()){
            ((Player) event.getEntity()).sendMessage(Main.INSTANCE.getConfig().getString("messages.fight").replaceAll("&", "§"));
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(Main.fight_player.containsKey(event.getPlayer().getUniqueId()) == false){ return; }
        else if(Main.fight_player.get(event.getPlayer().getUniqueId()) + 10 * 1000 < new Timestamp(System.currentTimeMillis()).getTime()){ return; }

        String cmd = event.getMessage();
        cmd = cmd.substring(1).split(" ")[0];
        for (String blacklist : Arrays.asList("spawn", "home", "lobby", "ec")) {
            if ((cmd.equalsIgnoreCase(blacklist))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Main.INSTANCE.getConfig().getString("messages.error_in_fight").replaceAll("&", "§"));
                break;
            }
        }
    }
}

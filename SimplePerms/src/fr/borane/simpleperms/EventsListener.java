package fr.borane.simpleperms;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws NoSuchFieldException, IllegalAccessException, SQLException {
        PreparedStatement preparedStatement = Main.getInstance().db.getConnection().prepareStatement("SELECT * FROM users WHERE uuid = ?");
        preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        if(!resultSet.next()){
            preparedStatement = Main.getInstance().db.getConnection().prepareStatement("INSERT INTO users(uuid) VALUES (?)");
            preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
            preparedStatement.executeUpdate();
            Main.getInstance().playersGroup.put(event.getPlayer().getUniqueId(), 1);
        }else{
            Main.getInstance().playersGroup.put(event.getPlayer().getUniqueId(), resultSet.getInt("id"));
        }

        Field f = CraftHumanEntity.class.getDeclaredField("perm");
        f.setAccessible(true);
        f.set(event.getPlayer(), new Permissions(event.getPlayer()));
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) throws SQLException {
        PreparedStatement preparedStatement = Main.getInstance().db.getConnection().prepareStatement("SELECT * FROM groups WHERE id = ?");
        preparedStatement.setInt(1, Main.getInstance().playersGroup.get(event.getPlayer().getUniqueId()));
        ResultSet resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){ return; }

        event.setCancelled(true);
        if(!event.getPlayer().hasPermission("simpleperms.chat")){ return; }
        if(!Main.INSTANCE.getConfig().getBoolean("chat")){ return; }
        if(event.getPlayer().hasPermission("simpleperms.chat.colors")){
            Main.getInstance().getServer().broadcastMessage(resultSet.getString("prefix").replaceAll("&", "§") + " " + resultSet.getString("pseudo_color").replaceAll("&", "§")  + event.getPlayer().getName() + "§f: "+ resultSet.getString("message_color").replaceAll("&", "§")  + event.getMessage().replaceAll("&", "§"));
        }else{
            Main.getInstance().getServer().broadcastMessage(resultSet.getString("prefix").replaceAll("&", "§") + " " + resultSet.getString("pseudo_color").replaceAll("&", "§")  + event.getPlayer().getName() + "§f: "+ resultSet.getString("message_color").replaceAll("&", "§")  + event.getMessage());
        }
    }
}

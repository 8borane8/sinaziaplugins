package fr.borane.hower.lobby.utils;

import fr.borane.hower.lobby.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage(null);
        Main.INSTANCE.getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.quit_message").replaceAll("&", "§").replaceAll("%pseudo%", event.getPlayer().getName()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage(Main.INSTANCE.getConfig().getString("messages.join_message").replaceAll("&", "§").replaceAll("%pseudo%", event.getPlayer().getName()));
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        for(String str : Main.INSTANCE.getConfig().getStringList("messages.welcome_message")){
            event.getPlayer().sendMessage(str.replaceAll("&", "§"));
        }
        event.getPlayer().getInventory().clear();
        event.getPlayer().setDisplayName(null);

        ItemStack navigation = new ItemStack(Material.COMPASS);
        ItemMeta meta = navigation.getItemMeta();
        meta.setDisplayName("Carte du voyageur");
        navigation.setItemMeta(meta);

        event.getPlayer().getInventory().setItem(4, navigation);

        Location spawn = new Location(Main.INSTANCE.getServer().getWorlds().get(0), Main.INSTANCE.getConfig().getDouble("spawn.posX"),
                Main.INSTANCE.getConfig().getDouble("spawn.posY"),
                Main.INSTANCE.getConfig().getDouble("spawn.posZ"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.yam"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.pitch"));
        event.getPlayer().teleport(spawn);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event){
        if(event.getPlayer().hasPermission("hower.lobby.bypass.moveitem")){ return; }
        event.setCancelled(true);
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event){
        if(event.getWhoClicked().hasPermission("hower.lobby.bypass.moveitem")){ return; }
        event.setCancelled(true);
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void entityDamageByBlockEvent(EntityDamageByBlockEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        if(event.getPlayer().hasPermission("hower.lobby.bypass.chat")){ return; }
        event.setCancelled(true);
        event.getPlayer().sendMessage(Main.INSTANCE.getConfig().getString("messages.chat_error").replaceAll("&", "§"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().hasPermission("hower.lobby.bypass.blockbreak")){ return; }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.getPlayer().hasPermission("hower.lobby.bypass.blockplace")){ return; }
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemRightClick(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand().getType() != Material.COMPASS){ return; }
        if(!event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){ return; }
        if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("Carte du voyageur")){ return; }

        event.getPlayer().performCommand("openselectservermenu");
    }
}

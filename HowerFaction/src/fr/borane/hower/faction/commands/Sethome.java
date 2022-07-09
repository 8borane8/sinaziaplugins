package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Sethome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.home.set")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length == 0){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()) != null && Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()).getKeys(false).size() >= 2){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.max_home_error").replaceAll("&", "§").replaceAll("%amount%", "2"));
            return false;
        }

        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0] + ".posX", ((Player)sender).getLocation().getX());
        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0] + ".posY", ((Player)sender).getLocation().getY());
        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0] + ".posZ", ((Player)sender).getLocation().getZ());

        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0] + ".yaw", ((Player)sender).getLocation().getYaw());
        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0] + ".pitch", ((Player)sender).getLocation().getPitch());
        Main.INSTANCE.saveConfig();
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.sethome_success").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

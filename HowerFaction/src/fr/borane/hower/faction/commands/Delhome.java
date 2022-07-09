package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Delhome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.home.del")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }

        if(args.length == 0){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_not_find").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }
        if(Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId() +"."+ args[0]) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_not_find").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }
        Main.INSTANCE.getConfig().set("homes." + ((Player)sender).getUniqueId() +"."+ args[0], null);
        Main.INSTANCE.saveConfig();
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.delhome_success").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

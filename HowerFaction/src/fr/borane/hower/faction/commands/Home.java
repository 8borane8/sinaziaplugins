package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.home")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length == 0){
            if(Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()) == null || Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()).getKeys(false).size() == 0){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.nohome").replaceAll("&", "§"));
                return false;
            }
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_list").replaceAll("&", "§"));
            for(String homename : Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()).getKeys(false)){
                sender.sendMessage("- " + homename);
            }
            return false;
        }
        if(Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()) == null || Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId()).getKeys(false).size() == 0){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_not_find").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }
        ConfigurationSection home = Main.INSTANCE.getConfig().getConfigurationSection("homes." + ((Player)sender).getUniqueId() +"."+ args[0]);
        if(home == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_not_find").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }


        Location homelocation = new Location(Main.INSTANCE.getServer().getWorlds().get(0), home.getDouble("posX"),
                home.getDouble("posY"),
                home.getDouble("posZ"),
                (float)home.getDouble("yaw"),
                (float)home.getDouble("pitch"));
        ((Player) sender).teleport(homelocation);

        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.home_success").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

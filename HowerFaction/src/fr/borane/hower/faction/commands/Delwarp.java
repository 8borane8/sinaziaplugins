package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Delwarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.warp.del")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length != 1){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getConfig().getConfigurationSection("warp." + args[0]) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.warp_not_found").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }
        Main.INSTANCE.getConfig().set("warp." + args[0], null);
        Main.INSTANCE.saveConfig();

        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.warp_del_success").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

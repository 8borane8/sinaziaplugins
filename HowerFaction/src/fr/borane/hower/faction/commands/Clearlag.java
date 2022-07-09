package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Clearlag implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.clearlag")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length != 0 && args[0].equals("force")){
            if(!sender.hasPermission("hower.faction.clearlag.force")){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
                return false;
            }
            Main.INSTANCE.getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.force_clearlag").replaceAll("&", "§"));
            Main.clearlag = 5;
            return false;
        }

        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.info_clearlag").replaceAll("&", "§").replaceAll("%time%", String.valueOf(Main.clearlag)));
        return false;
    }
}

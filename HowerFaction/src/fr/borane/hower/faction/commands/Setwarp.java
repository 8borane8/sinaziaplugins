package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setwarp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.warp.set")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }

        Main.INSTANCE.getConfig().set("warp." + args[0] + ".posX", ((Player) sender).getLocation().getX());
        Main.INSTANCE.getConfig().set("warp." + args[0] + ".posY", ((Player) sender).getLocation().getY());
        Main.INSTANCE.getConfig().set("warp." + args[0] + ".posZ", ((Player) sender).getLocation().getZ());

        Main.INSTANCE.getConfig().set("warp." + args[0] + ".yaw", ((Player) sender).getLocation().getYaw());
        Main.INSTANCE.getConfig().set("warp." + args[0] + ".pitch", ((Player) sender).getLocation().getPitch());
        Main.INSTANCE.saveConfig();
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.setwarp_success").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

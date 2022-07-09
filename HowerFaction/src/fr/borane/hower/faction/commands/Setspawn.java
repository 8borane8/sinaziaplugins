package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setspawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.spawn.set")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }

        Main.INSTANCE.getConfig().set("spawn.posX", ((Player) sender).getLocation().getX());
        Main.INSTANCE.getConfig().set("spawn.posY", ((Player) sender).getLocation().getY());
        Main.INSTANCE.getConfig().set("spawn.posZ", ((Player) sender).getLocation().getZ());

        Main.INSTANCE.getConfig().set("spawn.yaw", ((Player) sender).getLocation().getYaw());
        Main.INSTANCE.getConfig().set("spawn.pitch", ((Player) sender).getLocation().getPitch());
        Main.INSTANCE.saveConfig();
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.setspawn_success").replaceAll("&", "§"));
        return false;
    }
}

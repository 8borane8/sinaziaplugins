package fr.borane.hower.lobby.commands;

import fr.borane.hower.lobby.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setspawn implements CommandExecutor {
    private Main main;
    public Setspawn(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.lobby.spawn.set")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }

        main.getConfig().set("spawn.posX", ((Player) sender).getLocation().getX());
        main.getConfig().set("spawn.posY", ((Player) sender).getLocation().getY());
        main.getConfig().set("spawn.posZ", ((Player) sender).getLocation().getZ());

        main.getConfig().set("spawn.yaw", ((Player) sender).getLocation().getYaw());
        main.getConfig().set("spawn.pitch", ((Player) sender).getLocation().getPitch());
        main.saveConfig();
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.setspawn_success").replaceAll("&", "§"));
        return false;
    }
}

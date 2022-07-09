package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unfreez implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.freez")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length != 1){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getServer().getPlayer(args[0]) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }

        if(!Main.freez_player.contains(Main.INSTANCE.getServer().getPlayer(args[0]).getUniqueId())){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_freez").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }
        Main.freez_player.remove(Main.INSTANCE.getServer().getPlayer(args[0]).getUniqueId());
        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.unfreez").replaceAll("&", "§").replaceAll("%name%", args[0]));
        return false;
    }
}

package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Invsee implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.invsee")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length != 1){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getServer().getPlayer(args[0]) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§"));
            return false;
        }
        ((Player)sender).openInventory(Main.INSTANCE.getServer().getPlayer(args[0]).getInventory());
        return false;
    }
}

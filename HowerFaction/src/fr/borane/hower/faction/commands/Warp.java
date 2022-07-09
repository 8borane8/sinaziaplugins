package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.warp")){
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
        Location warp = new Location(Main.INSTANCE.getServer().getWorlds().get(0), Main.INSTANCE.getConfig().getDouble("warp." +args[0]+ ".posX"),
                Main.INSTANCE.getConfig().getDouble("warp." +args[0]+ ".posY"),
                Main.INSTANCE.getConfig().getDouble("warp." +args[0]+ ".posZ"),
                (float)Main.INSTANCE.getConfig().getDouble("warp." +args[0]+ ".yam"),
                (float)Main.INSTANCE.getConfig().getDouble("warp." +args[0]+ ".pitch"));
        ((Player) sender).teleport(warp);
        sender.sendMessage("§aTéléportation au warp !");
        return false;
    }
}

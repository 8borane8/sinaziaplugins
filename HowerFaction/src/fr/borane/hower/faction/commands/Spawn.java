package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.spawn")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        Location spawn = new Location(Main.INSTANCE.getServer().getWorlds().get(0), Main.INSTANCE.getConfig().getDouble("spawn.posX"),
                Main.INSTANCE.getConfig().getDouble("spawn.posY"),
                Main.INSTANCE.getConfig().getDouble("spawn.posZ"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.yam"),
                (float)Main.INSTANCE.getConfig().getDouble("spawn.pitch"));
        ((Player) sender).teleport(spawn);
        sender.sendMessage("§aTéléportation au spawn !");
        return false;
    }
}

package fr.borane.hower.lobby.commands;

import fr.borane.hower.lobby.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    private Main main;
    public Spawn(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.lobby.spawn")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        Location spawn = new Location(main.getServer().getWorlds().get(0), main.getConfig().getDouble("spawn.posX"),
                main.getConfig().getDouble("spawn.posY"),
                main.getConfig().getDouble("spawn.posZ"),
                (float)main.getConfig().getDouble("spawn.yam"),
                (float)main.getConfig().getDouble("spawn.pitch"));
        ((Player) sender).teleport(spawn);
        sender.sendMessage("§aTéléportation au spawn !");
        return false;
    }
}

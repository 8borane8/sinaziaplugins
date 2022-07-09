package fr.borane.simpleperms.commands;

import fr.borane.simpleperms.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class User implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("simpleperms.user")){
            sender.sendMessage("§4Vous n'avez pas la permission d'exécuter cette commande !");
            return false;
        }
        if(args.length != 1 && args.length != 3){
            sender.sendMessage("§4Utilisez /user [player] <[setgroup] [group]>");
            return false;
        }
        if(Main.getInstance().getServer().getPlayer(args[0]) == null){
            sender.sendMessage("§4Le joueur est introuvable");
            return false;
        }

        if(args.length == 1){
            sender.sendMessage("§aLe group de " + args[0] + " est: §e" + Main.getInstance().getConfig().get("users." + Main.getInstance().getServer().getPlayer(args[0]).getUniqueId()));
            return false;
        }

        if(args[1].equals("setgroup")){
            if(Main.getInstance().getConfig().getConfigurationSection("groups").getKeys(false).contains(args[2])){
                Main.getInstance().getConfig().set("users." + Main.getInstance().getServer().getPlayer(args[0]).getUniqueId(), args[2]);
                Main.getInstance().saveConfig();
                sender.sendMessage("§aSuccès");
            }else{
                sender.sendMessage("§4Le group n'existe pas");
                return false;
            }
        }else{
            sender.sendMessage("§4Utilisez /user [player] <[setgroup] [group]>");
            return false;
        }


        return false;
    }
}

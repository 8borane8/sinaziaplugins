package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Vanish implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.vanish")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(((Player)sender).hasPotionEffect(PotionEffectType.INVISIBILITY)){
            ((Player)sender).removePotionEffect(PotionEffectType.INVISIBILITY);
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.vanish_off").replaceAll("&", "§"));
        }else{
            ((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true));
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.vanish_on").replaceAll("&", "§"));
        }

        return false;
    }
}

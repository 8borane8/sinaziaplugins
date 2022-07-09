package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Top implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.money.top")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length == 1){
            if(args[0].equals("money")){
                try {
                    ResultSet resultSet = Main.db.getConnection().prepareStatement("SELECT * FROM users ORDER BY money ASC LIMIT 10").executeQuery();
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.money_top").replaceAll("&", "§"));
                    int i = 0;
                    while (resultSet.next()){
                        i++;
                        String pseudo = "Undefind";
                        if(Main.INSTANCE.getServer().getPlayer(UUID.fromString(resultSet.getString("uuid"))) != null){
                            pseudo = Main.INSTANCE.getServer().getPlayer(UUID.fromString(resultSet.getString("uuid"))).getName();
                        }
                        sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.money_top_arg").replaceAll("&", "§").replaceAll("%position%", String.valueOf(i)).replaceAll("%player%", pseudo).replaceAll("%amount%", String.valueOf(resultSet.getInt("money"))));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
        }
        return false;
    }
}

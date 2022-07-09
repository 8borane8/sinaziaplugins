package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.money")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length > 0){
            if(args[0].equals("give")){
                if(args.length != 3){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
                    return false;
                }
                if(!sender.hasPermission("hower.faction.money.give")){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
                    return false;
                }

                if(Main.INSTANCE.getServer().getPlayer(args[1]) == null){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§").replaceAll("%name%", args[1]));
                    return false;
                }

                try {
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_numeric_error").replaceAll("&", "§").replaceAll("%number%", args[2]));
                    return false;
                }

                try {
                    PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET money = money + ? WHERE uuid = ?");
                    preparedStatement.setString(1, args[2]);
                    preparedStatement.setString(2, Main.INSTANCE.getServer().getPlayer(args[1]).getUniqueId().toString());
                    preparedStatement.executeUpdate();
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.money_give_success").replaceAll("&", "§"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }else if(args[0].equals("set")){
                if(args.length != 3){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
                    return false;
                }
                if(!sender.hasPermission("hower.faction.money.set")){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
                    return false;
                }

                if(Main.INSTANCE.getServer().getPlayer(args[1]) == null){
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§").replaceAll("%name%", args[1]));
                    return false;
                }

                try {
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_numeric_error").replaceAll("&", "§").replaceAll("%number%", args[2]));
                    return false;
                }

                try {
                    PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET money = ? WHERE uuid = ?");
                    preparedStatement.setString(1, args[2]);
                    preparedStatement.setString(2, Main.INSTANCE.getServer().getPlayer(args[1]).getUniqueId().toString());
                    preparedStatement.executeUpdate();
                    sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.money_set_success").replaceAll("&", "§"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            return false;
        }

        try {
            PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT money FROM users WHERE uuid = ?");
            preparedStatement.setString(1, ((Player)sender).getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.money").replaceAll("&", "§").replaceAll("%amount%", String.valueOf(resultSet.getInt("money"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

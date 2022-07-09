package fr.borane.hower.faction.commands;

import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("hower.faction.money.pay")){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length < 2){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
            return false;
        }
        if(Main.INSTANCE.getServer().getPlayer(args[0]) == null){
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§").replaceAll("%name%", args[0]));
            return false;
        }

        try {
            Integer.parseInt(args[1]);
            if(args[1].contains("-")){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_numeric_error").replaceAll("&", "§"));
                return false;
            }
        } catch (NumberFormatException exception) {
            sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_numeric_error").replaceAll("&", "§"));
            return false;
        }

        try {
            PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT money FROM users WHERE uuid = ?");
            preparedStatement.setString(1, ((Player)sender).getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if(Integer.parseInt(args[1]) <= resultSet.getInt("money")){
                preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET money = money + ? WHERE uuid = ?");
                preparedStatement.setString(1, args[1]);
                preparedStatement.setString(2, Main.INSTANCE.getServer().getPlayer(args[0]).getUniqueId().toString());
                preparedStatement.executeUpdate();

                preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET money = money - ? WHERE uuid = ?");
                preparedStatement.setString(1, args[1]);
                preparedStatement.setString(2, ((Player) sender).getUniqueId().toString());
                preparedStatement.executeUpdate();

                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_success").replaceAll("&", "§").replaceAll("%amount%", args[1]).replaceAll("%player%", args[0]));
            }else{
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.pay_unsiffisant_found").replaceAll("&", "§").replaceAll("%amount%", String.valueOf(resultSet.getInt("money"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

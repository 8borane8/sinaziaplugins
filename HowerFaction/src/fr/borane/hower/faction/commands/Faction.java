package fr.borane.hower.faction.commands;

import com.mysql.jdbc.Statement;
import fr.borane.hower.faction.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Faction implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player = ((Player) sender);
        if(!player.hasPermission("hower.faction.faction")){
            player.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_permissions").replaceAll("&", "§"));
            return false;
        }
        if(args.length == 0){
            for(String str : Main.INSTANCE.getConfig().getStringList("messages.faction_help")){
                player.sendMessage(str.replaceAll("&", "§"));
            }
            return false;
        }

        if(args[0].equalsIgnoreCase("create")){
            if(args.length != 2){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
                return false;
            }
            if(args[1].length() > 14){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_create_max_length").replaceAll("&", "§"));
                return false;
            }
            try {
                PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT faction FROM users WHERE uuid = ?");
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    if(resultSet.getInt("faction") != 1){
                        player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_create_error").replaceAll("&", "§"));
                        return false;
                    }
                }
                preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM factions WHERE name = ?");
                preparedStatement.setString(1, args[1]);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_already_exist").replaceAll("&", "§").replaceAll("%name%", args[1]));
                    return false;
                }

                preparedStatement = Main.db.getConnection().prepareStatement("INSERT INTO factions (name, owner) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, args[1]);
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.execute();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                int generatedKey = 0;
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                }
                System.out.println(generatedKey);

                preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET faction = ? WHERE uuid = ?");
                preparedStatement.setInt(1, generatedKey);
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.executeUpdate();

                player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_create_success").replaceAll("&", "§").replaceAll("%name%", args[1]));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(args[0].equalsIgnoreCase("disband")){
            try {
                PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT faction FROM users WHERE uuid = ?");
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    if(resultSet.getInt("faction") != 1){
                        preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM factions WHERE id = ?");
                        preparedStatement.setInt(1, resultSet.getInt("faction"));
                        resultSet = preparedStatement.executeQuery();
                        if(resultSet.next()){
                            if(resultSet.getString("owner").equals(player.getUniqueId().toString())){

                                preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET faction = 1 WHERE faction = ?");
                                preparedStatement.setInt(1, resultSet.getInt("id"));
                                preparedStatement.executeUpdate();

                                preparedStatement = Main.db.getConnection().prepareStatement("DELETE FROM factions WHERE id = ?");
                                preparedStatement.setInt(1, resultSet.getInt("id"));
                                preparedStatement.executeUpdate();

                                Main.INSTANCE.getServer().broadcastMessage(Main.INSTANCE.getConfig().getString("messages.faction_disband_success").replaceAll("&", "§").replaceAll("%faction%", resultSet.getString("name").replaceAll("%name%", player.getName())));
                                return false;
                            }
                            player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_disband_not_owner").replaceAll("&", "§"));
                            return false;
                        }
                    }
                }
                player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_disband_error").replaceAll("&", "§"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(args[0].equalsIgnoreCase("leave")){
            try {
                PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT faction FROM users WHERE uuid = ?");
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    if(resultSet.getInt("faction") == 1){
                        player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_leave_wilderness_error").replaceAll("&", "§"));
                        return false;
                    }

                    preparedStatement = Main.db.getConnection().prepareStatement("SELECT owner FROM factions WHERE id = ?");
                    preparedStatement.setInt(1, resultSet.getInt("faction"));
                    resultSet = preparedStatement.executeQuery();
                    if(resultSet.next()){
                        if (resultSet.getString("owner").equals(player.getUniqueId().toString())){
                            player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_leave_owner_error").replaceAll("&", "§"));
                            return false;
                        }
                    }

                    preparedStatement = Main.db.getConnection().prepareStatement("UPDATE users SET faction = 1 WHERE uuid = ?");
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if(args[0].equalsIgnoreCase("owner")){
            if(args.length != 2){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.missing_arguments").replaceAll("&", "§"));
                return false;
            }

            if(Main.INSTANCE.getServer().getPlayer(args[1]) == null){
                sender.sendMessage(Main.INSTANCE.getConfig().getString("messages.player_not_found").replaceAll("&", "§").replaceAll("%name%", args[1]));
                return false;
            }
            try {
                PreparedStatement preparedStatement = Main.db.getConnection().prepareStatement("SELECT faction FROM users WHERE uuid = ?");
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    preparedStatement = Main.db.getConnection().prepareStatement("SELECT * FROM factions WHERE id = ?");
                    preparedStatement.setInt(1, resultSet.getInt("faction"));
                    resultSet = preparedStatement.executeQuery();

                    if(resultSet.next()){
                        preparedStatement = Main.db.getConnection().prepareStatement("UPDATE factions SET owner = ? WHERE id = ?");
                        preparedStatement.setInt(1, resultSet.getInt("id"));
                        preparedStatement.setString(2, Main.INSTANCE.getServer().getPlayer(args[1]).getUniqueId().toString());
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            player.sendMessage(Main.INSTANCE.getConfig().getString("messages.faction_arg_ot_found").replaceAll("&", "§"));
        }

        return false;
    }
}

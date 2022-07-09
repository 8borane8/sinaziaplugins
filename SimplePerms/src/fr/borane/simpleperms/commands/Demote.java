package fr.borane.simpleperms.commands;

import fr.borane.simpleperms.Main;
import fr.borane.simpleperms.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Demote implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        if(!sender.hasPermission("simpleperms.demote")){
            sender.sendMessage("§4Vous n'avez pas la permission d'exécuter cette commande !");
            return false;
        }
        if(args.length < 1){
            sender.sendMessage("§4Utilisez /promote [player]");
            return false;
        }
        if(Main.getInstance().getServer().getPlayer(args[0]) == null){
            sender.sendMessage("§4Le joueur est introuvable");
            return false;
        }

        try {
            if(Main.getInstance().playersGroup.get(((Player)sender).getUniqueId()) > 1){
                Main.getInstance().playersGroup.replace(((Player)sender).getUniqueId(), Main.getInstance().playersGroup.get(((Player)sender).getUniqueId()) - 1);
                PreparedStatement preparedStatement = Main.getInstance().db.getConnection().prepareStatement("UPDATE users SET id = ? WHERE uuid = ?");
                preparedStatement.setInt(1, Main.getInstance().playersGroup.get(((Player)sender).getUniqueId()));
                preparedStatement.setString(2, ((Player)sender).getUniqueId().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Field f = CraftHumanEntity.class.getDeclaredField("perm");
            f.setAccessible(true);
            f.set(sender, new Permissions(((Player)sender)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage("§aSuccès");
        return false;
    }
}

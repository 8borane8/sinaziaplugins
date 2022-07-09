package fr.borane.simpleperms;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Permissions extends PermissibleBase {

    private Player player;
    private List<String> permissions = new ArrayList();

    public Permissions(Player player) {
        super(player);
        this.player = player;
        try {
            add_permissions(Main.getInstance().playersGroup.get(player.getUniqueId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add_permissions(Integer group_id) throws SQLException {
        PreparedStatement preparedStatement = Main.getInstance().db.getConnection().prepareStatement("SELECT * FROM permissions WHERE id = ?");
        preparedStatement.setInt(1, group_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            if(!resultSet.getBoolean("value")){
                permissions.add("-" + resultSet.getString("name"));
                continue;
            }
            permissions.add(resultSet.getString("name"));
        }

        preparedStatement = Main.getInstance().db.getConnection().prepareStatement("SELECT * FROM groups WHERE id = ?");
        preparedStatement.setInt(1, Main.getInstance().playersGroup.get(player.getUniqueId()));
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getInt("inheritance") != 0){
            add_permissions(resultSet.getInt("inheritance"));
        }
    }

    @Override
    public boolean hasPermission(String str){
        if(permissions.contains("*") || player.isOp()){
            return true;
        }

        if(permissions.contains(str)){
            System.out.println(str);
            return true; }
        if(permissions.contains("-" + str)){ return false; }

        return super.hasPermission(str);
    }
}

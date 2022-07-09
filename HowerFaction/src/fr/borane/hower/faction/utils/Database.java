package fr.borane.hower.faction.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {
    private String host;
    private String user;
    private String pass;
    private String name;
    private int port;

    private Connection connection;

    public Database(String host, String user, String pass, String name, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.name = name;
        this.port = port;
        this.connect();
    }

    private String toUri(){
        final StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(name);
        return sb.toString();
    }

    public void connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Logger.getLogger("Minecraft").info("Successfully connected to DB.");
            connection = DriverManager.getConnection(toUri(), user, pass);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    public Connection getConnection() throws SQLException {
        if(connection != null && !connection.isClosed()){
            return connection;
        }

        connect();
        return connection;
    }
}

package me.codingmatt.twitch.objects;

import com.mysql.jdbc.Connection;
import me.codingmatt.twitch.TwitchBot;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class MySQLConnection {
    private String databaseHost;
    private int databasePort=3306;
    private String table;
    private String databaseUser;
    private String databasePassword;

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getTable() {
        return table;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public Connection connection;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String dbURL = "jdbc:mysql://"+databaseHost+"/"+table;
        connection = (Connection) DriverManager.getConnection(dbURL, databaseUser, databasePassword);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void testConnection(){
        try {
            TwitchBot.logger.info("Trying to contact the MySQL database to determent if any fallback is needed");
            connect();
            disconnect();
        } catch (Exception e){
            TwitchBot.logger.error("Could Not Connect To MySQL Database, Falling Back!");
            e.printStackTrace();
            TwitchBot.databaseFallback=true;
        }
    }


}

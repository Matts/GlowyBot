package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Channels {
    public Channels(){
        try {
            setupDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setupDB() throws ClassNotFoundException {
        Statement stmt = null;
        try {
            TwitchBot.mySQLConnection.connect();
            stmt = TwitchBot.mySQLConnection.connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `CHANNELS`(NAME CHAR(50), LINKPURGE BOOLEAN);";
            stmt.executeUpdate(sql);
            stmt.close();
            TwitchBot.mySQLConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> channelsToJoin() throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        Statement stmt = TwitchBot.mySQLConnection.connection.createStatement();
        ResultSet rs = null;
        ArrayList<String> channels = new ArrayList<String>();
        rs = stmt.executeQuery("SELECT * FROM `CHANNELS`");
        while (rs.next()) {
            channels.add(rs.getString("NAME"));
        }
        stmt.close();
        rs.close();
        TwitchBot.mySQLConnection.disconnect();
        return channels;
    }

    public void addChannel(String channel) throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        String sql = "INSERT INTO `CHANNELS` (NAME, LINKPURGE) VALUES(?, ?)";
        PreparedStatement statement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        statement.setString(1, channel.toLowerCase());
        statement.setBoolean(2, true);
        statement.executeUpdate();
        statement.close();
        TwitchBot.mySQLConnection.disconnect();
    }

    public void removeChannel(String channel) throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        String sql = "DELETE FROM `CHANNELS` WHERE `NAME` = ?;";
        PreparedStatement statement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        statement.setString(1, channel.toLowerCase());
        statement.executeUpdate();
        statement.close();
        TwitchBot.mySQLConnection.disconnect();
    }

    public boolean shouldPurgeLink(String channels) throws SQLException, ClassNotFoundException {
        boolean purgeLink = false;
        TwitchBot.mySQLConnection.connect();
        try{
            ResultSet result;
            String sql = "SELECT * FROM `CHANNELS` WHERE `NAME`=?";
            PreparedStatement preparedStatement =  TwitchBot.mySQLConnection.connection.prepareStatement(sql);
            preparedStatement.setString(1, channels.toLowerCase());
            result = preparedStatement.executeQuery();
            if(result.next()){
                purgeLink = result.getBoolean("LINKPURGE");
            }
            TwitchBot.mySQLConnection.disconnect();
            result.close();
            preparedStatement.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return purgeLink;
    }

    public void updatePurgeLink(String channel, boolean shouldPurge) throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        PreparedStatement statement = TwitchBot.mySQLConnection.connection.prepareStatement("UPDATE `CHANNELS` SET `LINKPURGE`=? WHERE `NAME`=?");
        statement.setBoolean(1, shouldPurge);
        statement.setString(2, channel.toLowerCase());
        statement.executeUpdate();
        statement.close();
        TwitchBot.mySQLConnection.disconnect();
    }
}

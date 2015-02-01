package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModuleFactoids")
public class ModuleFactoid extends ListenerAdapter<PircBotX> {

    public ModuleFactoid(){
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
            String sql = "CREATE TABLE IF NOT EXISTS `FACTOIDS`(COMMAND CHAR(50), USER CHAR(50), OUTPUT CHAR(255), PERM CHAR(50), CHANNEL CHAR(50)) ";
            stmt.executeUpdate(sql);
            stmt.close();
            TwitchBot.mySQLConnection.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        //!add Hello mod this is a command
        if (event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("add")) {
            try {
                addCommand(event.getChannel().getName(), event.getMessage().trim().split(" ")[1], event.getMessage().trim().substring(event.getMessage().split(" ")[0].length() + event.getMessage().split(" ")[1].length() + event.getMessage().split(" ")[2].length() + 2), event.getMessage().trim().split(" ")[2], event.getUser().getNick());
            } catch (Exception e) {
                event.respond(e.getMessage());
            }
        }
        if (event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("remove")) {
            try {
                removeCommand(event.getChannel().getName(), event.getMessage().trim().split(" ")[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("get")){
            try {
                event.getChannel().send().message(getOutput(event.getChannel().getName(), event.getMessage().trim().split(" ")[1]));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            String output = getOutput(event.getChannel().getName(), event.getMessage().trim().split(" ")[0].substring(1));
            if(event.getMessage().trim().split(" ")[0].substring(0,1).equalsIgnoreCase("!")) {
                if(!output.equalsIgnoreCase(event.getMessage().trim().split(" ")[0].substring(1))) {
                    event.getChannel().send().message(output);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addCommand(String channel, String command, String output, String permission, String user) throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        String sql = "INSERT INTO `FACTOIDS` (COMMAND, USER, OUTPUT, PERM, CHANNEL) VALUES(?,?,?,?,?)";
        PreparedStatement statement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        statement.setString(1, command.toLowerCase());
        statement.setString(2, user);
        statement.setString(3, output.trim());
        statement.setString(4, permission);
        statement.setString(5, channel.toLowerCase());
        statement.executeUpdate();
        statement.close();
        TwitchBot.mySQLConnection.disconnect();
    }

    public void removeCommand(String channel, String command) throws SQLException, ClassNotFoundException {
        TwitchBot.mySQLConnection.connect();
        String sql = "DELETE FROM `FACTOIDS` WHERE `CHANNEL` = ? AND `COMMAND` = ?;";
        PreparedStatement statement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        statement.setString(1, channel.toLowerCase());
        statement.setString(2, command.toLowerCase());
        statement.executeUpdate();
        statement.close();
        TwitchBot.mySQLConnection.disconnect();
    }

    public String getOutput(String channel, String command) throws SQLException, ClassNotFoundException {
        ResultSet result;
        TwitchBot.mySQLConnection.connect();
        String sql = "SELECT * FROM `FACTOIDS` WHERE `CHANNEL` = ? AND `COMMAND`=?";
        PreparedStatement preparedStatement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        preparedStatement.setString(1, channel.toLowerCase());
        preparedStatement.setString(2, command.toLowerCase());
        result = preparedStatement.executeQuery();
        if (result.next())
            return result.getString("OUTPUT");
        TwitchBot.mySQLConnection.disconnect();
        return command.toLowerCase();
    }

    public String getPermission(String channel, String command) throws SQLException, ClassNotFoundException {
        ResultSet result;
        TwitchBot.mySQLConnection.connect();
        String sql = "SELECT * FROM `FACTOIDS` WHERE `CHANNEL` = ? AND `COMMAND`=?";
        PreparedStatement preparedStatement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        preparedStatement.setString(1, channel.toLowerCase());
        preparedStatement.setString(2, command.toLowerCase());
        result = preparedStatement.executeQuery();
        if(result.next())
            return result.getString("PERM");
        TwitchBot.mySQLConnection.disconnect();
        return command.toLowerCase();
    }

    public String getUser(String channel, String command) throws SQLException, ClassNotFoundException {
        ResultSet result;
        TwitchBot.mySQLConnection.connect();
        String sql = "SELECT * FROM `FACTOIDS` WHERE `CHANNEL` = ? AND `COMMAND`=?";
        PreparedStatement preparedStatement = TwitchBot.mySQLConnection.connection.prepareStatement(sql);
        preparedStatement.setString(1, channel.toLowerCase());
        preparedStatement.setString(2, command.toLowerCase());
        result = preparedStatement.executeQuery();
        if(result.next())
            return result.getString("USER");
        TwitchBot.mySQLConnection.disconnect();
        return command.toLowerCase();
    }
}

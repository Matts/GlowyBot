package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.Factoid;
import me.codingmatt.twitch.objects.annotations.Listeners;
import me.codingmatt.twitch.utils.Permission;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModuleFactoids", internal = true)
public class ModuleFactoid extends ListenerAdapter<PircBotX> {

    ArrayList<Factoid> factoids = new ArrayList<Factoid>();

    public ModuleFactoid(){
        try {
            setupDB();

            cacheFactoids();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
        if (event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("add")) {
            if(Permission.hasPermission(Permission.Perm.MOD,event.getUser().getNick().toLowerCase(),event.getChannel().getName().toLowerCase())) {
                try {
                    addCommand(event.getChannel().getName(), event.getMessage().trim().split(" ")[1], event.getMessage().trim().substring(event.getMessage().split(" ")[0].length() + event.getMessage().split(" ")[1].length() + event.getMessage().split(" ")[2].length() + 2), event.getMessage().trim().split(" ")[2], event.getUser().getNick());
                    event.respond("Command Created!");
                } catch (Exception e) {
                    e.printStackTrace();
                    event.respond(e.getMessage());
                }
            }else{
                Permission.noPerm(event, Permission.Perm.MOD);
            }
        }
        if (event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("remove")) {
            if(Permission.hasPermission(Permission.Perm.MOD,event.getUser().getNick().toLowerCase(),event.getChannel().getName().toLowerCase())) {
                try {
                    removeCommand(event.getChannel().getName(), event.getMessage().trim().split(" ")[1]);
                    event.respond("Command Removed!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.respond(e.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    event.respond(e.getMessage());
                }
            }else{
                Permission.noPerm(event, Permission.Perm.MOD);
            }
        }
        if(event.getMessage().trim().split(" ")[0].startsWith("!")) {
            String output = getOutput(event.getChannel().getName(), event.getMessage().trim().split(" ")[0].substring(1));
            if(!output.equalsIgnoreCase(event.getMessage().trim().split(" ")[0].substring(1))) {
                //if(Permission.Perm.parsePerm(getPermission(event.getChannel().getName(),event.getMessage().trim().split(" ")[0].substring(1))).getHeight()>=Permission.getHighestPerm(event.getUser().getNick(),event.getChannel().getName()).getHeight()) {
                    event.getChannel().send().message(output);
                //}
            }
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
        factoids.add(new Factoid(channel.toLowerCase(), user.toLowerCase(), command.toLowerCase(), output.toLowerCase().trim(), permission.toLowerCase()));
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
        for(Factoid f : factoids){
            if(f.getCommand().toLowerCase().equalsIgnoreCase(command)){
                factoids.remove(f);
            }
        }
            }

    public String getOutput(String channel, String command) {
        for (int i = 0; i < factoids.size(); i++) {
            if(factoids.get(i).getChannel().equalsIgnoreCase(channel) && factoids.get(i).getCommand().equalsIgnoreCase(command)){
                return factoids.get(i).getOutput();
            }
        }
        return command;
    }

    public String getPermission(String channel, String command) {
        for (int i = 0; i < factoids.size(); i++) {
            if(factoids.get(i).getChannel().equalsIgnoreCase(channel) && factoids.get(i).getChannel().equalsIgnoreCase(command)){
                return factoids.get(i).getPermission();
            }
        }
        return command;
    }

    public String getUser(String channel, String command) {
        for (int i = 0; i < factoids.size(); i++) {
            if(factoids.get(i).getChannel().equalsIgnoreCase(channel) && factoids.get(i).getChannel().equalsIgnoreCase(command)){
                return factoids.get(i).getCreator();
            }
        }
        return command;
    }

    public void cacheFactoids() throws SQLException, ClassNotFoundException {
        factoids.clear();
        TwitchBot.mySQLConnection.connect();
        ResultSet set;
        String sql = "SELECT * FROM `FACTOIDS`;";
        Statement stmt = TwitchBot.mySQLConnection.connection.createStatement();
        set = stmt.executeQuery(sql);
        while(set.next()){
            String channel,creator,command,output,permission;
            channel=set.getString("CHANNEL");
            creator=set.getString("USER");
            command=set.getString("COMMAND");
            output=set.getString("OUTPUT");
            permission=set.getString("PERM");

            factoids.add(new Factoid(channel.toLowerCase(),creator.toLowerCase(),command.toLowerCase(),output.toLowerCase(),permission.toLowerCase()));
        }
        TwitchBot.mySQLConnection.disconnect();
    }

}

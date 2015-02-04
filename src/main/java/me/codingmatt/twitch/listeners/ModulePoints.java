package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.MySQLConnection;
import me.codingmatt.twitch.objects.annotations.Listeners;
import me.codingmatt.twitch.objects.annotations.WIP;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModulePoints") @WIP
public class ModulePoints extends ListenerAdapter<PircBotX>{
    MySQLConnection sql;

    public ModulePoints(){
        sql = TwitchBot.mySQLConnection.getInstance();
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        super.onMessage(event);

    }



    public void addPoints(String channel, String user, int points){

    }

    public void removePoints(String channel, String user, int points){

    }

    public void getPoints(String channel, String user){

    }

    public void removeUser(String channel, String user){

    }

    public void addUser(String channel, String user){

    }
}

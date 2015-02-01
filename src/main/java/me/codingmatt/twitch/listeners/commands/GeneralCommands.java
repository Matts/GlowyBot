package me.codingmatt.twitch.listeners.commands;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.annotations.Command;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Command
public class GeneralCommands extends CommandBase {
    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("join")){
            try {
                TwitchBot.channels.addChannel(event.getMessage().trim().split(" ")[1]);
                event.getBot().sendIRC().joinChannel(event.getMessage().trim().split(" ")[1]);
            } catch (Exception e){
                e.printStackTrace();
                event.respond(e.getMessage());
            }
        }
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("leave")){
            try {
                TwitchBot.channels.removeChannel(event.getMessage().trim().split(" ")[1]);
                event.getBot().sendRaw().rawLine("PART " + event.getMessage().trim().split(" ")[1]);
            } catch (Exception e){
                e.printStackTrace();
                event.respond(e.getMessage());
            }
        }
    }
}

package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners
public class CommandFactoid extends ListenerAdapter<PircBotX> {
    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        if(event.getMessage().trim().substring(1).equalsIgnoreCase("add")){

        }
        if(event.getMessage().trim().substring(1).equalsIgnoreCase("remove")){
            
        }
    }
}

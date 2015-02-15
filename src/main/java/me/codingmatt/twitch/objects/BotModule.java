package me.codingmatt.twitch.objects;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.annotations.Module;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class BotModule extends ListenerAdapter<PircBotX> {
    private String moduleName;

    public BotModule(){
        readAnnotation();
    }

    public void init(){
        TwitchBot.logger.info("Initializing External Module " + moduleName);
    }

    private void readAnnotation(){
        Module a = getClass().getAnnotation(Module.class);
        moduleName = a.name();
    }
}

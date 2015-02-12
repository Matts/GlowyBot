package me.codingmatt.twitch.modules;

import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModuleHello")
public class ModuleHello extends ListenerAdapter<PircBotX> {

    public ModuleHello() {
        System.out.println("Hello World!");
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        super.onMessage(event);
        event.respond("Hello!");
    }
}

package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.utils.Registry;
import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModuleCommands")
public class ModuleCommands extends ListenerAdapter<PircBotX> {

    public ModuleCommands(){
        Registry.setCommands();
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if(event.getMessage().trim().split(" ")[0].substring(0,1).equalsIgnoreCase("!")){
            for (int i = 0; i < Registry.commands.size() ; i++) {
                Registry.commands.get(i).onMessage(event);
            }
        }
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) throws Exception {
        super.onJoin(event);
        event.getChannel().send().message("Yo, i am GlowBot");
    }
}

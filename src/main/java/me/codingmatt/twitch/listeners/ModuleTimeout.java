package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners
public class ModuleTimeout extends ListenerAdapter<PircBotX> {
    public ArrayList<String> channelPurgeLinks = new ArrayList<String>();

    public ModuleTimeout(){
        updatePurgeList();
    }

    public void updatePurgeList(){
        for (int i = 0; i < TwitchBot.channelsJoined.size(); i++) {
            try {

                if(TwitchBot.channels.shouldPurgeLink(TwitchBot.channelsJoined.get(i))){
                    channelPurgeLinks.add(TwitchBot.channelsJoined.get(i).toLowerCase());
                    System.out.println(TwitchBot.channelsJoined.get(i));
                }else{
                    channelPurgeLinks.remove(TwitchBot.channelsJoined.get(i).toLowerCase());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        super.onMessage(event);
        if(channelPurgeLinks.contains(event.getChannel().getName().toLowerCase())){
            if(isLink(event.getMessage())) {
                event.getChannel().send().message(".timeout " + event.getUser().getNick() + " 1");
            }
        }
        if(event.getMessage().trim().split(" ")[0].equalsIgnoreCase("!links")){
            TwitchBot.channels.updatePurgeLink(event.getChannel().getName(), Boolean.parseBoolean(event.getMessage().trim().split(" ")[1]));
            if(Boolean.parseBoolean(event.getMessage().trim().split(" ")[1])){
                channelPurgeLinks.add(event.getChannel().getName().toLowerCase());
            }else{
                channelPurgeLinks.remove(event.getChannel().getName().toLowerCase());
            }
        }
    }

    public boolean isLink(String message){
        String[] mess = message.split(" ");
        for(int i = 0;i<mess.length;i++) {
            if (mess[i].matches("((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)")) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
}

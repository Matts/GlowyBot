package me.codingmatt.twitch.listeners;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.annotations.Listeners;
import me.codingmatt.twitch.utils.Permission;
import me.codingmatt.twitch.utils.Viewers;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Listeners(name = "ModuleTimeout")
public class ModuleTimeout extends ListenerAdapter<PircBotX> {
    public ArrayList<String> channelPurgeLinks = new ArrayList<String>();

    public HashMap<String, ArrayList<String>> permitted = new HashMap<String, ArrayList<String>>();

    public ModuleTimeout(){
        updatePurgeList();
    }

    public void updatePurgeList(){
        for (int i = 0; i < TwitchBot.channelsJoined.size(); i++) {
            try {

                if(TwitchBot.channels.shouldPurgeLink(TwitchBot.channelsJoined.get(i))){
                    channelPurgeLinks.add(TwitchBot.channelsJoined.get(i).toLowerCase());
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
        if (channelPurgeLinks.contains(event.getChannel().getName().toLowerCase())) {
            if (isLink(event.getMessage())) {
                if(!Permission.hasPermission(Permission.Perm.MOD,event.getUser().getNick().toLowerCase(),event.getChannel().getName().toLowerCase())) {
                    if (!permitted.containsKey(event.getUser().getNick().toLowerCase())) {
                        event.getChannel().send().message(".timeout " + event.getUser().getNick() + " 1");
                    } else {
                        if (permitted.get(event.getUser().getNick().toLowerCase()).contains(event.getChannel().getName())) {
                            permitted.get(event.getUser().getNick().toLowerCase()).remove(event.getChannel().getName());
                        } else {
                            event.getChannel().send().message(".timeout " + event.getUser().getNick() + " 1");
                        }
                    }
                }else
                    Permission.noPerm(event, Permission.Perm.MOD);
            }
        }
        if (event.getMessage().trim().split(" ")[0].equalsIgnoreCase("!links")) {
            if(Permission.hasPermission(Permission.Perm.BROADCASTER,event.getUser().getNick().toLowerCase(),event.getChannel().getName().toLowerCase())) {
                TwitchBot.channels.updatePurgeLink(event.getChannel().getName(), Boolean.parseBoolean(event.getMessage().trim().split(" ")[1]));
                if (Boolean.parseBoolean(event.getMessage().trim().split(" ")[1])) {
                    event.respond("Link Purging Activated!");
                    channelPurgeLinks.add(event.getChannel().getName().toLowerCase());
                } else {
                    event.respond("Link Purging Disabled!");
                    channelPurgeLinks.remove(event.getChannel().getName().toLowerCase());
                }
            }else{
                Permission.noPerm(event, Permission.Perm.BROADCASTER);
            }
        }
        if (event.getMessage().trim().split(" ")[0].equalsIgnoreCase("!permit")) {
            if(Permission.hasPermission(Permission.Perm.MOD, event.getUser().getNick().toLowerCase(), event.getChannel().getName().toLowerCase())) {
                if (permitted.containsKey(event.getUser().getNick().toLowerCase())) {
                    permitted.get(event.getMessage().trim().split(" ")[1].toLowerCase()).add(event.getChannel().getName().toLowerCase());
                    event.respond("Permitted " + event.getMessage().trim().split(" ")[1].toLowerCase());
                } else {
                    permitted.put(event.getMessage().trim().split(" ")[1].toLowerCase(), new ArrayList<String>());
                    permitted.get(event.getMessage().trim().split(" ")[1].toLowerCase()).add(event.getChannel().getName());
                    event.respond("Permitted " + event.getMessage().trim().split(" ")[1].toLowerCase());
                }
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

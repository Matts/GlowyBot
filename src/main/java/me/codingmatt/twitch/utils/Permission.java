package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Permission {
    public static boolean hasPermission(Perm perm, String username, String channel) {
        if (perm.height <= Perm.TWITCH_STAFF.height) {
            if (Viewers.twitchPeeps.containsKey(channel.toLowerCase())) {
                if (Viewers.twitchPeeps.get(channel.toLowerCase()).contains(username.toLowerCase())) {
                    return true;
                }
            }
        }
        if (perm.height <= Perm.MOD.height) {
            if (Viewers.moderators.containsKey(channel.toLowerCase())) {
                if (Viewers.moderators.get(channel.toLowerCase()).contains(username.toLowerCase())) {
                    return true;
                }
            }
        }
        if (perm.height <= Perm.VIEWER.height) {
            if (Viewers.channelViewers.containsKey(channel.toLowerCase())) {
                if (Viewers.channelViewers.get(channel.toLowerCase()).contains(username.toLowerCase())) {
                    return true;
                }
            }
        }
        if(username.toLowerCase().equalsIgnoreCase(channel.substring(1).toLowerCase())){
            if(perm.height<=Perm.BROADCASTER.height){
                return true;
            }
        }
        for (int i = 0; i < TwitchBot.controllers.length; i++) {
            if(TwitchBot.controllers[i].equalsIgnoreCase(username.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static void noPerm(MessageEvent event, Perm perm){
        event.respond("You Do Not Have The Permission '" + perm + "'");
    }

    public static Perm getHighestPerm(String user, String channel){
        if(Viewers.twitchPeeps.get(channel.toLowerCase()).contains(user.toLowerCase())){
            return Perm.TWITCH_STAFF;
        }
        if(user.toLowerCase().equalsIgnoreCase(channel.toLowerCase())){
            return Perm.BROADCASTER;
        }
        if(Viewers.moderators.get(channel.toLowerCase()).contains(user.toLowerCase())){
            return Perm.TWITCH_STAFF;
        }
        if(Viewers.channelViewers.get(channel.toLowerCase()).contains(user.toLowerCase())){
            return Perm.TWITCH_STAFF;
        }
        return Perm.VIEWER;
    }

    public static enum Perm{
        VIEWER(1), MOD(2), BROADCASTER(3), TWITCH_STAFF(100);

        private int height;

        Perm(int height){
            this.height=height;
        }

        public int getHeight() {
            return height;
        }

        public static Perm parsePerm(String perm){
            if(VIEWER.toString().equalsIgnoreCase(perm.toLowerCase())){
                return VIEWER;
            }
            if(MOD.toString().equalsIgnoreCase(perm.toLowerCase())){
                return MOD;
            }
            if(BROADCASTER.toString().equalsIgnoreCase(perm.toLowerCase())){
                return BROADCASTER;
            }
            if(TWITCH_STAFF.toString().equalsIgnoreCase(perm.toLowerCase())){
                return TWITCH_STAFF;
            }
            return VIEWER;
        }
    }
}

package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Viewers {
    public static HashMap<String, ArrayList<String>> channelViewers = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> moderators = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, ArrayList<String>> twitchPeeps = new HashMap<String, ArrayList<String>>();


    public static  Thread updateViewers = new Thread("viewerUpdater"){
        @Override
        public synchronized void run() {
            while(true){
                    try {
                        sleep(180000);
                        updateAllViewers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }}};

    public static synchronized void updateAllViewers() {
        for (int i = 0; i < TwitchBot.channelsJoined.size(); i++) {
            try {
                updateViewers(TwitchBot.channelsJoined.get(i));
                updateModerators(TwitchBot.channelsJoined.get(i));
                updateTwitchPeeps(TwitchBot.channelsJoined.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateViewers(String channel) throws Exception {
        ArrayList<String> TMPViewers = new ArrayList<String>();
        TMPViewers.clear();
        JSONObject json = new JSONObject(JSONParser.readUrl("http://tmi.twitch.tv/group/user/"+channel.toLowerCase().substring(1)+"/chatters"));

        JSONArray view = json.getJSONObject("chatters").getJSONArray("viewers");
        TMPViewers.clear();
        for(int j = 0; j < view.length(); j++){
            TMPViewers.add(view.getString(j));
        }
        JSONArray mods = json.getJSONObject("chatters").getJSONArray("moderators");
        for(int j = 0; j < mods.length(); j++) {
            TMPViewers.add(mods.getString(j));
        }
        JSONArray staff = json.getJSONObject("chatters").getJSONArray("staff");
        for(int j = 0; j < staff.length(); j++) {
            TMPViewers.add(staff.getString(j));
        }
        JSONArray admins = json.getJSONObject("chatters").getJSONArray("admins");
        for(int j = 0; j < admins.length(); j++) {
            TMPViewers.add(admins.getString(j));
        }
        JSONArray gMods = json.getJSONObject("chatters").getJSONArray("global_mods");
        for(int j = 0; j < gMods.length(); j++) {
            TMPViewers.add(gMods.getString(j));
        }


        if(channelViewers.containsKey(channel)) {
            channelViewers.remove(channel);
        }
        channelViewers.put(channel, TMPViewers);
    }

    public static void updateModerators(String channel) throws Exception {
        ArrayList<String> TMPMods = new ArrayList<String>();
        TMPMods.clear();
        JSONObject json = new JSONObject(JSONParser.readUrl("http://tmi.twitch.tv/group/user/"+channel.toLowerCase().substring(1)+"/chatters"));

        JSONArray mods = json.getJSONObject("chatters").getJSONArray("moderators");
        for(int j = 0; j < mods.length(); j++) {
            TMPMods.add(mods.getString(j));
        }
        JSONArray staff = json.getJSONObject("chatters").getJSONArray("staff");
        for(int j = 0; j < staff.length(); j++) {
            TMPMods.add(staff.getString(j));
        }
        JSONArray admins = json.getJSONObject("chatters").getJSONArray("admins");
        for(int j = 0; j < admins.length(); j++) {
            TMPMods.add(admins.getString(j));
        }
        JSONArray gMods = json.getJSONObject("chatters").getJSONArray("global_mods");
        for(int j = 0; j < gMods.length(); j++) {
            TMPMods.add(gMods.getString(j));
        }



        if(moderators.containsKey(channel)) {
            moderators.remove(channel);
        }
        moderators.put(channel, TMPMods);
    }

    public static void updateTwitchPeeps(String channel) throws Exception {
        ArrayList<String> TMPMods = new ArrayList<String>();
        TMPMods.clear();
        JSONObject json = new JSONObject(JSONParser.readUrl("http://tmi.twitch.tv/group/user/"+channel.toLowerCase().substring(1)+"/chatters"));

        JSONArray staff = json.getJSONObject("chatters").getJSONArray("staff");
        for(int j = 0; j < staff.length(); j++) {
            TMPMods.add(staff.getString(j));
        }
        JSONArray admins = json.getJSONObject("chatters").getJSONArray("admins");
        for(int j = 0; j < admins.length(); j++) {
            TMPMods.add(admins.getString(j));
        }
        JSONArray gMods = json.getJSONObject("chatters").getJSONArray("global_mods");
        for(int j = 0; j < gMods.length(); j++) {
            TMPMods.add(gMods.getString(j));
        }
        if(channelViewers.containsKey(channel)) {
            twitchPeeps.remove(channel);
        }
        twitchPeeps.put(channel, TMPMods);
    }
}

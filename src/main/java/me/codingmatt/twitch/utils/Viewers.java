package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Viewers {
    private static ArrayList<String> TMPViewers = new ArrayList<String>();

    public static HashMap<String, ArrayList<String>> channelViewers = new HashMap<String, ArrayList<String>>();


    public static  Thread updateViewers = new Thread("viewerUpdater"){
        @Override
        public synchronized void run() {
            while(true){
                    try {
                        for (int i = 0; i < TwitchBot.channelsJoined.size(); i++) {
                            updateViewers(TwitchBot.channelsJoined.get(i));
                        }
                        sleep(180000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }}};

    public static void updateViewers(String channel) throws Exception {
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

        if(channelViewers.containsKey(channel)) {
            channelViewers.remove(channel);
        }
        channelViewers.put(channel, TMPViewers);
    }
}

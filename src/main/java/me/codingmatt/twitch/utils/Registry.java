package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.annotations.Command;
import me.codingmatt.twitch.objects.annotations.Listeners;
import org.pircbotx.hooks.Listener;

import java.util.ArrayList;
import java.util.Set;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Registry {
    public static ArrayList<Listener> listeners = new ArrayList<Listener>();
    public static ArrayList<CommandBase> commands = new ArrayList<CommandBase>();

    public static void setCommands(){
        Set<Class<?>> classes = TwitchBot.reflection.getTypesAnnotatedWith(Command.class);
        for (Class<?> clss : classes) {
            try {
                Registry.commands.add(((CommandBase) clss.newInstance()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setBaseListeners(){
        Set<Class<?>> classes = TwitchBot.reflection.getTypesAnnotatedWith(Listeners.class);
        for (Class<?> clss : classes) {
            try {
                Registry.listeners.add(((Listener) clss.newInstance()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

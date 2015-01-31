package me.codingmatt.twitch.objects;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public abstract class CommandBase {
    public abstract void onMessage(MessageEvent<PircBotX> event);
}

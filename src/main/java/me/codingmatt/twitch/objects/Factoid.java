package me.codingmatt.twitch.objects;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Factoid {
    private int PriKey;
    private String channel;
    private String creator;
    private String command;
    private String output;
    private String permission;

    public Factoid(int PriKey, String channel, String creator, String command, String output, String permission) {
        this.PriKey=PriKey;
        this.channel = channel;
        this.creator = creator;
        this.command = command;
        this.output = output;
        this.permission = permission;
    }

    public String getChannel() {
        return channel;
    }

    public String getCreator() {
        return creator;
    }

    public String getCommand() {
        return command;
    }

    public String getOutput() {
        return output;
    }

    public String getPermission() {
        return permission;
    }
}

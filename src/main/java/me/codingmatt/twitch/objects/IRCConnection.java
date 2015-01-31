package me.codingmatt.twitch.objects;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class IRCConnection {
    private String name;
    private String login;
    private String version;
    private boolean autoNickChange;

    private String serverHost;
    private int serverPort;
    private String serverPassword;
    private String defaultChannel;

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAutoNickChange(boolean autoNickChange) {
        this.autoNickChange = autoNickChange;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public void setPort(int port) {
        this.serverPort = port;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public void setDefaultChannel(String defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getVersion() {
        return version;
    }

    public boolean doAutoNickChange() {
        return autoNickChange;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getDefaultChannel() {
        return defaultChannel;
    }
}

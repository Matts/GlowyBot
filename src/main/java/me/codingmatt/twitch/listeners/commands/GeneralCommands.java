package me.codingmatt.twitch.listeners.commands;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.CommandBase;
import me.codingmatt.twitch.objects.annotations.Command;
import me.codingmatt.twitch.utils.Permission;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.SQLException;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Command(name = "GeneralCommands")
public class GeneralCommands extends CommandBase {
    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("join")){
            if (event.getMessage().trim().split(" ").length == 2) {
                if (Permission.hasPermission(Permission.Perm.TWITCH_STAFF, event.getUser().getNick(), event.getChannel().getName())) {
                    try {
                        TwitchBot.channels.addChannel(event.getMessage().trim().split(" ")[1]);
                        event.getBot().sendIRC().joinChannel(event.getMessage().trim().split(" ")[1]);
                        event.getChannel().send().message("ADMIN_MODE, (Force) Joining " + event.getMessage().trim().split(" ")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        event.respond(e.getMessage());
                    }
                } else {
                    Permission.noPerm(event, Permission.Perm.TWITCH_STAFF);
                }
            }else{
                try {
                    TwitchBot.channels.addChannel("#"+event.getUser().getNick().toLowerCase());
                    event.getBot().sendIRC().joinChannel("#" + event.getUser().getNick().toLowerCase());
                    event.getChannel().send().message("I successfully joined your channel (#"+event.getUser().getNick()+")");
                    event.getChannel().send().message("ADMIN_MODE, (Force) Parting " + event.getMessage().trim().split(" ")[1]);
                } catch (SQLException e) {
                    event.respond(e.getMessage());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    event.respond(e.getMessage());
                    e.printStackTrace();
                }

            }
        }
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("leave")) {
            if (event.getMessage().trim().split(" ").length == 2) {
                if (Permission.hasPermission(Permission.Perm.TWITCH_STAFF, event.getUser().getNick(), event.getChannel().getName())) {
                    try {
                        TwitchBot.channels.removeChannel(event.getMessage().trim().split(" ")[1]);
                        event.getBot().sendRaw().rawLine("PART " + event.getMessage().trim().split(" ")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                        event.respond(e.getMessage());
                    }
                } else {
                    Permission.noPerm(event, Permission.Perm.TWITCH_STAFF);
                }
            }else{
                try {
                    TwitchBot.channels.removeChannel("#" + event.getUser().getNick().toLowerCase());
                    event.getBot().sendRaw().rawLine("PART " + "#"+ event.getUser().getNick().toLowerCase());
                    event.getChannel().send().message("I successfully left your channel (#"+event.getUser().getNick()+")");
                } catch (SQLException e) {
                    event.respond(e.getMessage());
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    event.respond(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("die")){
            if (Permission.hasPermission(Permission.Perm.TWITCH_STAFF, event.getUser().getNick(), event.getChannel().getName())) {
                if( event.getMessage().trim().split(" ").length>1 && event.getMessage().trim().split(" ")[1].equalsIgnoreCase("confirm")) {
                    event.getChannel().send().message("/me *cries*. I see, I am not wanted here anymore, Good Bye! *Shoots Myself*");
                    event.getBot().stopBotReconnect();
                    event.getBot().sendIRC().quitServer(event.getUser().getNick() + " killed me with !die");
                }else{
                    event.respond("Are you sure you want to shut me down (type: !die confirm to confirm)");
                }
            } else {
                Permission.noPerm(event, Permission.Perm.TWITCH_STAFF);
            }
        }
        if(event.getMessage().trim().split(" ")[0].substring(1).equalsIgnoreCase("test")){
            event.respond("test");
        }
    }
}

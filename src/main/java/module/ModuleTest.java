package module;

import me.codingmatt.twitch.objects.BotModule;
import me.codingmatt.twitch.objects.annotations.Module;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Module(name="ModuleTest", author = "Matt", version = "nop")
public class ModuleTest extends BotModule {
    public ModuleTest(){
        super();
        System.out.println("Test");
    }
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        super.onMessage(event);
        if(event.getMessage().equalsIgnoreCase("!moduletest")){
            event.respond("woo this works :D");
        }
    }
}

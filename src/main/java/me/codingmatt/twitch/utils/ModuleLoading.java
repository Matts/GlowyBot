package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.BotModule;
import me.codingmatt.twitch.objects.annotations.Module;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class ModuleLoading {

    public static void LoadModule(File folder, String cp){
        try {
            URL url = folder.toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);

            Class cls = cl.loadClass(cp);
            BotModule module = (BotModule) cls.newInstance();
            module.init();
            Registry.modules.add(module);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    static Yaml yaml = new Yaml();
    public static void loadAllModules(){
        File file = new File(System.getProperty("user.dir")+"\\modules");
        if(!file.exists()){
            file.mkdirs();
        }
        String[] names = file.list();
        for(String name : names)
        {
            File file1 = new File(file.getAbsolutePath()+"\\" + name);
            if (file1.isDirectory())
            {

                File check = new File(file1.getAbsolutePath()+"\\module.yml");
                if(check.exists()) {
                    Map<String, Object> conf = null;
                    try {
                        conf = (Map<String, Object>) yaml.load(new FileInputStream(
                                check));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    LoadModule(file1, (String) conf.get("mainClass"));
                }else{
                    TwitchBot.logger.error("Found a folder in /modules but doesn't contain a module.yml. Skipping!");
                }

            }
        }

    }
}

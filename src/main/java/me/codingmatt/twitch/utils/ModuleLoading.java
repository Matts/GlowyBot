package me.codingmatt.twitch.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            cls.newInstance();
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
        String[] names = file.list();
        for(String name : names)
        {
            File file1 = new File(file.getAbsolutePath()+"\\" + name);
            if (file1.isDirectory())
            {

                File check = new File(file1.getAbsolutePath()+"\\module.yml");
                Map<String, Object> conf = null;
                try {
                    conf = (Map<String, Object>) yaml.load(new FileInputStream(
                            check));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                LoadModule(file1, (String)conf.get("mainClass"));

            }
        }

    }
}

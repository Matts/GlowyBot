package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.objects.BotModule;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class JarModuleLoading {
    public JarModuleLoading() throws IOException {
        File dir = new File("modules");
        if(!dir.exists()){
            dir.mkdirs();
        }
        final ArrayList<Path> jarFiles = new ArrayList<Path>();
        Path startPath = Paths.get("modules");
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.getFileName().toString().trim().endsWith(".jar")) {
                    jarFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        for (int i = 0; i < jarFiles.size(); i++) {
            URL[] urls = new URL[1];
            urls[0] = jarFiles.get(i).toFile().toURL();
            try {
                loadModuleFromJar(urls);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadModuleFromJar(URL[] jar) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String mainClass = getMainClass(jar[0]);
        URLClassLoader child = new URLClassLoader(jar, this.getClass().getClassLoader());
        Class classToLoad = Class.forName (mainClass, true, child);
        BotModule instance = (BotModule) classToLoad.newInstance();
        instance.init();
        Registry.modules.add(instance);
    }

    private String getMainClass(URL inputJar) throws IOException {
        JarFile j = new JarFile(inputJar.getPath());
        ZipEntry zipEntry = j.getEntry("module.info");
        InputStream input = j.getInputStream(zipEntry);
        BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        String line;
        while((line = br.readLine())!=null){
            if(line.startsWith("mainClass: ")){
                return line.split(": ")[1].trim();
            }
        }
       return null;
    }
}

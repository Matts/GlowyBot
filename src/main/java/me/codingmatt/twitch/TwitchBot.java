package me.codingmatt.twitch;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import me.codingmatt.twitch.objects.IRCConnection;
import me.codingmatt.twitch.objects.MySQLConnection;

import me.codingmatt.twitch.objects.annotations.Listeners;
import me.codingmatt.twitch.utils.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import org.pircbotx.hooks.Listener;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class TwitchBot {

    public static final Logger logger = LoggerFactory.getLogger(TwitchBot.class.getName());
    public static final Reflections reflection = new Reflections("me.codingmatt.twitch");

    public static ConfigParser configParser;
    public static MySQLConnection mySQLConnection;

    public static boolean databaseFallback=false;

    public static Channels channels;
    public static ArrayList<String> channelsJoined = new ArrayList<String>();

    public static String VERSION;

    public static String[] controllers;

    public static void main(String[] args){

ModuleLoading.loadAllModules();

        try {
            File file = new File("config.xml");
            if(!file.exists())
                firstSetup();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        configParser = new ConfigParser("config.xml");

        VERSION = ConfigParser.getVersion();

        new Updater();

        mySQLConnection = configParser.setupSQLFromConfig();
        mySQLConnection.testConnection();

        channels = new Channels();
        try {
            channelsJoined = channels.channelsToJoin();
        } catch (Exception e){
            e.printStackTrace();
        }
        Viewers.updateAllViewers();

        Viewers.updateViewers.start();

        
        new TwitchBot();

    }

    public TwitchBot() {
        IRCConnection config = configParser.servers.get(0); //TODO: Set-up multibot

        Configuration.Builder confi = new Configuration.Builder();
        confi.setName(config.getName());
        confi.setLogin(config.getLogin());
        confi.setVersion(config.getVersion());
        confi.setAutoNickChange(config.doAutoNickChange());
        confi.setServerHostname(config.getServerHost());
        confi.setServerPort(config.getServerPort());
        confi.setServerPassword(config.getServerPassword());
        confi.addAutoJoinChannel(config.getDefaultChannel());
        confi.setEncoding(Charset.isSupported("UTF-8") ? Charset.forName("UTF-8") : Charset.defaultCharset());

        Registry.setBaseListeners();

        ArrayList<Listener> usingListeners = new ArrayList<Listener>();

        for (Listener listen : Registry.listeners) {
            for (int i = 0; i < config.getModules().length; i++) {
                if(config.getModules()[i].equalsIgnoreCase(listen.getClass().getAnnotation(Listeners.class).name())){
                    usingListeners.add(listen);
                }
            }
        }

        for (int i = 0; i < usingListeners.size(); i++) {
            confi.addListener(usingListeners.get(i));
        }


        for (String channel : channelsJoined) {
            confi.addAutoJoinChannel(channel);
        }

        controllers = config.getControllers();
        PircBotX bot = new PircBotX(confi.buildConfiguration());

        try {
            logger.info("Starting " + confi.getName() + " | version " + confi.getVersion());
            bot.startBot();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IrcException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public static void firstSetup() throws ParserConfigurationException, IOException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getSystemResourceAsStream("./wizard.txt")));

        String mySQLHost, mySQLPort, mySQLTable, mySQLUsername, mySQLPassword;
        String serverHost, defaultChannel, password, port, name, autoNickChange, login, version;
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.endsWith("*n*")){
                System.out.println(line);
                break;
            }else{
                System.out.println(line);
            }
        }
        scanner.nextLine();
        System.out.println(reader.readLine());
        serverHost=scanner.nextLine();
        System.out.println(reader.readLine());
        port = scanner.nextLine();
        System.out.println(reader.readLine());
        password = scanner.nextLine();
        System.out.println(reader.readLine());
        name = scanner.nextLine();
        System.out.println(reader.readLine());
        autoNickChange = scanner.nextLine();
        System.out.println(reader.readLine());
        login = scanner.nextLine();
        System.out.println(reader.readLine());
        version = scanner.nextLine();
        System.out.println(reader.readLine());
        defaultChannel = scanner.nextLine();
        System.out.println(reader.readLine());
        mySQLHost = scanner.nextLine();
        System.out.println(reader.readLine());
        mySQLPort = scanner.nextLine();
        System.out.println(reader.readLine());
        mySQLUsername = scanner.nextLine();
        System.out.println(reader.readLine());
        mySQLPassword = scanner.nextLine();
        System.out.println(reader.readLine());
        mySQLTable = scanner.nextLine();
        while ((line = reader.readLine()) != null) {
            if(line.endsWith("*n*")){
                System.out.println(line);
                break;
            }else{
                System.out.println(line);
            }
        }

        DocumentBuilderFactory doc = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = doc.newDocumentBuilder();
        Document xmlDoc = builder.newDocument();

        Element rootElement = xmlDoc.createElement("config");

        Element updateXMLElement = xmlDoc.createElement("updateXML");

        updateXMLElement.setAttribute("host", "https://dl.dropboxusercontent.com/u/14369750/update.xml;");

        Element mySQLElement = xmlDoc.createElement("mysql");

        mySQLElement.setAttribute("host", mySQLHost.equalsIgnoreCase("") ? "localhost" : mySQLHost);
        mySQLElement.setAttribute("port", mySQLPort.equalsIgnoreCase("") ? "3306" : mySQLPort);
        mySQLElement.setAttribute("table", mySQLTable.equalsIgnoreCase("") ? "twitchbot" : mySQLTable);

        Element mysqlUserNameElement = xmlDoc.createElement("username");
        Element mysqlPasswordElement = xmlDoc.createElement("password");

        addTextToElement(xmlDoc, mysqlUserNameElement, mySQLUsername.equalsIgnoreCase("")?"root":mySQLUsername);
        addTextToElement(xmlDoc, mysqlPasswordElement, mySQLPassword.equalsIgnoreCase("")?"toor":mySQLPassword);

        mySQLElement.appendChild(mysqlUserNameElement);
        mySQLElement.appendChild(mysqlPasswordElement);


        Element serversElement = xmlDoc.createElement("servers");
        Element serverElement = xmlDoc.createElement("server");

        Element serverNameElement = xmlDoc.createElement("name");
        Element loginNameElement = xmlDoc.createElement("login");
        Element versionNameElement = xmlDoc.createElement("version");
        Element moduleElement = xmlDoc.createElement("modules");

        String[] modules = new String[]{"ModuleCommands","ModuleFactoid","ModuleTimeout"};

        for (int i = 0; i < modules.length; i++) {
            Element module = xmlDoc.createElement("module");
            module.setAttribute("name", modules[i]);
            moduleElement.appendChild(module);
        }

        serverNameElement.setAttribute("autoNickChange", autoNickChange.equalsIgnoreCase("") ? "true" : autoNickChange);

        serverElement.setAttribute("host", serverHost.equalsIgnoreCase("") ? "irc.twitch.tv" : serverHost);
        serverElement.setAttribute("port", port.equalsIgnoreCase("") ? "6667" : port);
        serverElement.setAttribute("password", password.equalsIgnoreCase("") ? "" : password);
        serverElement.setAttribute("defaultChannel", defaultChannel.equalsIgnoreCase("") ? "#mattsonmc" : defaultChannel);


        addTextToElement(xmlDoc, serverNameElement, name.equalsIgnoreCase("")? "TwitchBot":name);
        addTextToElement(xmlDoc, loginNameElement, login.equalsIgnoreCase("")? "Bot" : login);
        addTextToElement(xmlDoc, versionNameElement, version);



        serverElement.appendChild(serverNameElement);
        serverElement.appendChild(loginNameElement);
        serverElement.appendChild(versionNameElement);
        serverElement.appendChild(moduleElement);

        serversElement.appendChild(serverElement);

        rootElement.appendChild(updateXMLElement);
        rootElement.appendChild(mySQLElement);
        rootElement.appendChild(serversElement);

        xmlDoc.appendChild(rootElement);

        OutputFormat outFormat = new OutputFormat(xmlDoc);
        outFormat.setIndenting(true);

        File xmlFile = new File("config.xml");
        FileOutputStream outStream = new FileOutputStream(xmlFile);

        XMLSerializer serializer = new XMLSerializer(outStream, outFormat);
        serializer.serialize(xmlDoc);


    }

    public static void addTextToElement(Document xmlDoc, Element e, String text){
        Text txt = xmlDoc.createTextNode(text);
        e.appendChild(txt);
    }

}


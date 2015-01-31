package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.IRCConnection;
import me.codingmatt.twitch.objects.MySQLConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class ConfigParser {
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    public static ArrayList<IRCConnection> servers = new ArrayList<IRCConnection>();

    public ConfigParser(String doc) {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            TwitchBot.logger.error(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            TwitchBot.logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            TwitchBot.logger.error(e.getMessage());
        }

        setServerConfiguration();

    }



    private void setServerConfiguration(){
        NodeList serverList = document.getElementsByTagName("servers");

        for (int i = 0; i < serverList.getLength(); i++) {
            Node parent = serverList.item(i);
            if(parent.getNodeType() == parent.ELEMENT_NODE)
            {
                for (int j = 0; j <parent.getChildNodes().getLength() ; j++) {
                    if(parent.getChildNodes().item(j).getNodeType() == parent.ELEMENT_NODE)
                    {
                        IRCConnection config = new IRCConnection();

                        config.setServerHost(((Element) parent.getChildNodes().item(j)).getAttribute("host"));
                        config.setPort(Integer.parseInt(((Element) parent.getChildNodes().item(j)).getAttribute("port")));
                        config.setServerPassword(((Element) parent.getChildNodes().item(j)).getAttribute("password"));
                        config.setDefaultChannel(((Element) parent.getChildNodes().item(j)).getAttribute("defaultChannel"));

                        for (int k = 0; k < parent.getChildNodes().item(j).getChildNodes().getLength(); k++) {
                            if(parent.getChildNodes().item(j).getChildNodes().item(k).getNodeType() == parent.ELEMENT_NODE)
                            {
                                if(parent.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("name")){
                                    config.setAutoNickChange(Boolean.parseBoolean(((Element) parent.getChildNodes().item(j).getChildNodes().item(k)).getAttribute("autoNickChange")));
                                    config.setName(parent.getChildNodes().item(j).getChildNodes().item(k).getTextContent().trim());
                                }
                                if(parent.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("login")){
                                    config.setLogin(parent.getChildNodes().item(j).getChildNodes().item(k).getTextContent().trim());
                                }
                                if(parent.getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("version")){
                                    config.setVersion(parent.getChildNodes().item(j).getChildNodes().item(k).getTextContent().trim());
                                }
                            }
                        }
                        servers.add(config);
                    }
                }
            }
        }

    }

    public MySQLConnection setupSQLFromConfig(){
        NodeList serverList = document.getElementsByTagName("mysql");
        MySQLConnection connection = new MySQLConnection();
        for (int i = 0; i < serverList.getLength(); i++) {
            Node parent = serverList.item(i);
            if (parent.getNodeType() == parent.ELEMENT_NODE) {
                Element eParent = (Element) parent;
                connection.setDatabaseHost(eParent.getAttribute("host"));
                connection.setDatabasePort(Integer.parseInt(eParent.getAttribute("port")));
                connection.setTable(eParent.getAttribute("table"));

                NodeList childs = parent.getChildNodes();
                for (int j = 0; j < childs.getLength(); j++) {
                    if (childs.item(j).getNodeType() == parent.ELEMENT_NODE) {
                        if(childs.item(j).getNodeName().equalsIgnoreCase("username")){
                            connection.setDatabaseUser(childs.item(j).getTextContent());
                        }
                        if(childs.item(j).getNodeName().equalsIgnoreCase("password")){
                            connection.setDatabasePassword(childs.item(j).getTextContent());
                        }
                    }
                }

            }
        }
        return connection;
    }


}

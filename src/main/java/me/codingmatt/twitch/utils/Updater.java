package me.codingmatt.twitch.utils;

import me.codingmatt.twitch.TwitchBot;
import me.codingmatt.twitch.objects.Update;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
public class Updater {
    public static String hosts=null;

    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;
    private static Document document;

    public static String host;

    public Updater(){
        hosts = TwitchBot.configParser.getUpdaterXMLURL();
        String[] hostsArray = hosts.split(";");
        for (int i = 0; i < hostsArray.length ; i++) {
            if(ping(hostsArray[i])){
                host=hostsArray[i];
                break;
            }
        }

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new URL(host).openStream());
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

        getUpdates();

        if(isUpdateAvailable()){
            if(ConfigParser.willAutoUpdate()){
                //TODO: DOWNLOAD
            }else{
                //TODO: WARN
            }
        }
    }

    public static boolean ping(String url) {
        HttpURLConnection connection = null;
        URL u = null;
        try {
            u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            return (200 <= code && code <= 399);
        } catch (MalformedURLException e) {
            return false;
        } catch (ProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }


    }

    public static ArrayList<Update> updates = new ArrayList<Update>();

    public void getUpdates(){
        updates.clear();
        NodeList update = document.getElementsByTagName("update");
        for (int i = 0; i < update.getLength(); i++) {
            Update updateObject = new Update();
            Node parent = update.item(i);
            if (parent.getNodeType() == parent.ELEMENT_NODE) {
                Element e = (Element) parent;
                updateObject.setLatest(Boolean.parseBoolean(e.getAttribute("latest")));
                updateObject.setVersion(e.getAttribute("version"));
                updateObject.setBranch(e.getAttribute("branch"));

                NodeList children = parent.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() == child.ELEMENT_NODE) {
                        if(child.getNodeName().equalsIgnoreCase("link")){
                            String[] links = child.getTextContent().split(";");
                            for (int k = 0; k < links.length; k++) {
                                if(ping(links[k])){
                                    updateObject.setLink(links[k]);
                                    break;
                                }
                            }
                        }
                        if(child.getNodeName().equalsIgnoreCase("changelog")){
                            updateObject.setChangelog(child.getTextContent());
                        }
                        if(child.getNodeName().equalsIgnoreCase("md5")){
                            updateObject.setMd5(child.getTextContent());
                        }
                    }
                }
            }
            updates.add(updateObject);
        }
    }

    public boolean isUpdateAvailable(){
        for (int i = 0; i < updates.size(); i++) {
            if(updates.get(i).isLatest()){
                if(!updates.get(i).getVersion().equalsIgnoreCase(TwitchBot.VERSION)){
                    return true;
                }
            }
        }
        return false;
    }
}

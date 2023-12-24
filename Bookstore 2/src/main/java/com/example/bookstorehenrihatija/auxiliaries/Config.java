package com.example.bookstorehenrihatija.auxiliaries;

import java.io.*;
import java.util.Properties;

public class Config {
    public final String configPath = "src/main/resources/config.properties";
    Properties configFile = new Properties();
    public String getProperty(String key){
        try{
            configFile.load(new FileInputStream(configPath));
        }catch(IOException e){
            e.printStackTrace();
        }
        return this.configFile.getProperty(key);
    }
    public void setProperty(String key, String value){
        configFile.setProperty(key, value);
        try{
            configFile.store(new FileOutputStream(configPath), "");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}

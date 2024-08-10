package com.fx.fx_app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fx.fx_app.exceptions.ConfigSettingException;

public class ConfigLoader {

    public static String getProperty(String key) throws ConfigSettingException, IOException, NullPointerException {

        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            
            Properties properties = new Properties();
            properties.load(input);

            String property = properties.getProperty(key);

            if (property == null) {
                throw new ConfigSettingException(key);
            }
            
            return property;
        } 
    }
}

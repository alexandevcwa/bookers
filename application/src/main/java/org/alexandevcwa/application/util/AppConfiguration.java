package org.alexandevcwa.application.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfiguration {

    private static AppConfiguration appConfiguration;

    private Properties properties;

    private AppConfiguration() throws IOException {
        try (InputStream in = AppConfiguration.class.getResourceAsStream("/application.properties")){
            this.properties = new Properties();
            this.properties.load(in);
        }
    }

    public static AppConfiguration getInstance() throws IOException {
        if (appConfiguration == null){
            appConfiguration = new AppConfiguration();
        }
        return appConfiguration;
    }

    public String getProperty(String key){
        return this.properties.getProperty(key);
    }
}

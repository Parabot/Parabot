package org.parabot.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author JKetelaar
 * @deprecated
 */
public class ProjectProperties {

    private static ProjectProperties instance;
    private Properties cached = new Properties();

    private ProjectProperties(){
        setProperties();
    }

    private void setProperties(){
        InputStream input;
        try {
            String propertiesFileName = "/app.properties";

            Properties properties = new Properties();
            InputStream inputStream = ProjectProperties.class.getClassLoader()
                    .getResourceAsStream(propertiesFileName);

            cached.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties getCached(){
        return cached;
    }

    public static Object getProjectVersion(){
        System.out.println(getInstance().getCached().getProperty("application.version"));
        return getInstance().getCached().getProperty("application.version");
    }

    public static ProjectProperties getInstance(){
        return instance == null ? instance = new ProjectProperties() : instance;
    }
}

package org.parabot.core;

import org.parabot.environment.api.utils.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author JKetelaar
 */
public class ProjectProperties {

    private static ProjectProperties instance;
    private final Properties cached = new Properties();

    private ProjectProperties() {
        setProperties();
    }

    public static Version getProjectVersion() {
        return new Version(getInstance().getCached().getProperty("application.version"));
    }

    public static ProjectProperties getInstance() {
        return instance == null ? instance = new ProjectProperties() : instance;
    }

    private void setProperties() {
        InputStream input;
        try {
            String propertiesFileName = "storage/parabot.properties";

            input = getClass().getClassLoader()
                    .getResourceAsStream(propertiesFileName);

            cached.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Properties getCached() {
        return cached;
    }
}

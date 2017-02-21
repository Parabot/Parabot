package org.parabot.core;

import com.google.inject.Singleton;
import org.parabot.environment.api.utils.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author JKetelaar
 */
@Singleton
public class ProjectProperties {

    private Properties cached = new Properties();

    public ProjectProperties() {
        setProperties();
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

    public Version getProjectVersion() {
        return new Version(getCached().getProperty("application.version"));
    }
}

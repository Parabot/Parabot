package org.parabot.core;

import org.parabot.environment.api.utils.Version;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration extends org.parabot.api.Configuration {
    public static final String DOWNLOAD_BOT = "http://bdn.parabot.org/versions/";
    public static final String REGISTRATION_PAGE = "https://www.parabot.org/community/register/";
    public static final Version BOT_VERSION = ProjectProperties.getProjectVersion();
}

package org.parabot.core.settings;

import org.parabot.core.Core;
import org.parabot.environment.api.utils.Version;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration extends org.parabot.api.Configuration {
    public static final String REGISTRATION_PAGE = "https://www.parabot.org/community/register/";
    public static final Version BOT_VERSION = Core.getInjector().getInstance(ProjectProperties.class).getProjectVersion();
}

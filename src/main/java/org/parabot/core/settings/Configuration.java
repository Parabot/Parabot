package org.parabot.core.settings;

import org.parabot.core.Core;
import org.parabot.environment.api.utils.Version;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration extends org.parabot.api.Configuration {
    public static final String  REGISTRATION_PAGE = "https://www.parabot.org/community/register";
    public static final Version BOT_VERSION       = Core.getInjector().getInstance(ProjectProperties.class).getProjectVersion();
    public static final String  USER_AGENT        = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/601.6.17 (KHTML, like Gecko) Version/9.1.1 Safari/601.6.17";
}

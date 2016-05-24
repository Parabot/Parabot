package org.parabot.core;

import org.parabot.environment.api.utils.Version;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration {
//    public static final String LOGIN_SERVER = "https://www.parabot.org/community/api/login.php?username=%s&password=%s";
    public static final String LOGIN_SERVER = "http://bdn.parabot.org/api/v2/users/login";
    public static final String GET_SCRIPTS = "http://bdn.parabot.org/api/get.php?action=scripts_scripts&server=";
    public static final String GET_SCRIPT = "http://bdn.parabot.org/api/get.php?action=scripts_script&id=";
    public static final String GET_SERVER_PROVIDERS = "http://bdn.parabot.org/api/get.php?action=server_providers";
    public static final String GET_SERVER_PROVIDER = "http://bdn.parabot.org/api/get.php?action=server_provider&name=";
    public static final String GET_SERVER_PROVIDER_INFO = "http://bdn.parabot.org/api/get.php?action=server_information&name=";
    public static final String GET_SERVER_SETTINGS = "http://bdn.parabot.org/api/get.php?action=get_settings";
    public static final String GET_BOT_VERSION = "http://bdn.parabot.org/api/v2/bot/version";
    public static final String API_DOWNLOAD_BOT = "http://bdn.parabot.org/api/v2/bot/download/client/";
    public static final String DOWNLOAD_BOT = "http://bdn.parabot.org/versions/";
    public static final String REGISTRATION_PAGE = "https://www.parabot.org/community/register/";
    public static final String GET_RANDOMS = "http://bdn.parabot.org/api/get.php?action=randoms";
    public static final String DATA_API = "http://bdn.parabot.org/api/v2/data/";
    public static final String ITEM_API = DATA_API + "items/";
    public static final String BUGSNAG_API = "d79752cf94dd4beb24c3d312a8609f53";

    public static final Version BOT_VERSION = ProjectProperties.getProjectVersion();

    public static final String BOT_TITLE = "Parabot";
    public static final String BOT_SLOGAN = "The best RuneScape private server bot";
}

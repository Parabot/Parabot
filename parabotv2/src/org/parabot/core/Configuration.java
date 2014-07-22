package org.parabot.core;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration {
    public static final String LOGIN_SERVER = "http://www.parabot.org/community/api/login.php?username=%s&password=%s";
    public static final String SDN_SCRIPTS = "http://sdn.parabot.org/scripts.php?user=%s";
    public static final String SDN_SCRIPTS_JSON = "http://sdn.parabot.org/scripts.php?method=json&user=%s";
    public static final String GET_SDN_SCRIPT = "http://sdn.parabot.org/getscript.php?user=%s&pass=%s&scriptid=%d";
    public static final String GET_SERVER_PROVIDERS = "http://sdn.parabot.org/providers/index.php";
    public static final String GET_SERVER_PROVIDERS_JSON = "http://sdn.parabot.org/providers/index.php?method=json";
    public static final String GET_SERVER_PROVIDER = "http://sdn.parabot.org/providers/provider.php?id=";
    public static final String GET_SERVER_PROVIDER_INFO = "http://sdn.parabot.org/providers/getInformation.php?id=";
    public static final String GET_BOT_VERSION = "http://bot.parabot.org/version.txt";
    public static final String REGISTRATION_PAGE = "http://www.parabot.org/community/index.php?app=core&module=global&section=register";

    public static final double BOT_VERSION = 2.1;

}

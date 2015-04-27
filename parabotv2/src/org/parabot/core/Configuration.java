package org.parabot.core;

/**
 * Holds some important constants
 *
 * @author Everel
 */
public class Configuration {
    public static final String LOGIN_SERVER = "https://www.parabot.org/community/api/login.php?username=%s&password=%s";
    public static final String GET_SCRIPTS = "http://bdn.parabot.org/api/get.php?action=scripts_scripts&server=";
    public static final String GET_SCRIPT = "http://bdn.parabot.org/api/get.php?action=scripts_script&id=";
    public static final String GET_SERVER_PROVIDERS = "http://bdn.parabot.org/api/get.php?action=server_providers";
    public static final String GET_SERVER_PROVIDER = "http://bdn.parabot.org/api/get.php?action=server_provider&name=";
    public static final String GET_SERVER_PROVIDER_INFO = "http://bdn.parabot.org/api/get.php?action=server_information&name=";
    public static final String GET_BOT_VERSION = "http://bot.parabot.org/version.txt";
    public static final String REGISTRATION_PAGE = "https://www.parabot.org/community/index.php?app=core&module=global&section=register";
    public static final String GET_PASSWORD = "http://bdn.parabot.org/api/get.php?action=password";
    public static final String GET_RANDOMS = "http://bdn.parabot.org/api/get.php?action=randoms";
    public static final String DATA_API = "http://bdn.parabot.org/api/v2/data/";
    public static final String ITEM_API = DATA_API + "items/";

    public static final double BOT_VERSION = 2.1;
}

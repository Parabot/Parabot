package org.parabot.core;

/**
 * 
 * @author Everel
 *
 */
public class Configuration {
	public static final String LOGIN_SERVER = "http://www.parabot.org/community/api/login.php?username=%s&password=%s";
	public static final String SDN_SCRIPTS = "http://sdn.parabot.org/scripts.php?user=%s";
	public static final String GET_SDN_SCRIPT = "http://sdn.parabot.org/getscript.php?user=%s&pass=%s&scriptid=%d";
	public static final String GET_SERVER_PROVIDERS = "http://sdn.parabot.org/list/providers.php";
	public static final String GET_SERVER_PROVIDER = "http://sdn.parabot.org/providers/%s";
	public static final String GET_BOT_VERSION = "http://bot.parabot.org/version.txt";
	
	public static final double BOT_VERSION = 2.04; // BETA
	
}

package org.parabot.core.forum;

/**
 * 
 * Class which holds parabot forum account user and pass, only specific classes
 * have access to it unless it's a modified version of parabot intended to
 * steal user information.
 * 
 * @author Everel
 * 
 */
public class Account {
	private String username;
	private String password;

	/**
	 * 
	 * @param username - UTF-8 encoded forum account username
	 * @param password - UTF-8 encoded forum account password
	 */
	public Account(final String username, final String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Gets user's parabot account name
	 * @return username, already URL UTF-8 encoded.
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Gets user's parabot password
	 * @return password, already URL UTF-8 encoded.
	 */
	public String getPassword() {
		return this.password;
	}

}

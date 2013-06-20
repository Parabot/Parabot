package org.parabot.environment.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author Clisprail
 *
 */
public class WebUtil {
	private static String agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	
	/**
	 * Agent to set at a URL connection
	 * @param userAgent
	 */
	public static void setUserAgent(final String userAgent) {
		agent = userAgent;
	}
	
	/**
	 * Gets useragent
	 * @return useragent
	 */
	public static String getUserAgent() {
		return agent;
	}

	/**
	 * Fetches content of a page
	 * @param location
	 * @return contents of page
	 * @throws MalformedURLException
	 */
	public static String getContents(final String location)
			throws MalformedURLException {
		return getContents(new URL(location));
	}

	/**
	 * Get contents from URL
	 * @param url
	 * @return page contents
	 */
	public static String getContents(final URL url) {
		try {
			final BufferedReader in = getReader(url);
			final StringBuilder builder = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			return builder.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets BufferedReader from URL
	 * @param url
	 * @return BufferedReader from URL
	 */
	public static BufferedReader getReader(final URL url) {
		try {
			return new BufferedReader(new InputStreamReader(getInputStream(url)));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets inputstream from url
	 * @param url
	 * @return inputstream from url
	 */
	public static InputStream getInputStream(final URL url) {
		final URLConnection con = getConnection(url);
		try {
			return con.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Opens a connection
	 * @param url
	 * @return URLConnection to URL
	 */
	public static URLConnection getConnection(final URL url) {
		try {
			final URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", agent);
			return con;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}

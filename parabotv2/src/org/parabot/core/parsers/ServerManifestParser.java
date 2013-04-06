package org.parabot.core.parsers;

import java.net.URL;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;

/**
 * 
 * @author Clisprail
 *
 */
public class ServerManifestParser {
	private URL url = null;
	
	public ServerManifestParser(final URL url) {
		this.url = url;
	}
	
	/**
	 * Gets server descriptions
	 * @return list of descriptions
	 */
	public ServerDescription[] getDescriptions() {
		if(Core.isDevMode()) {
			return localDesc();
		}
		return publicDesc();
	}

	private ServerDescription[] publicDesc() {
		return null;
	}

	private ServerDescription[] localDesc() {
		return null;
	}
	

}

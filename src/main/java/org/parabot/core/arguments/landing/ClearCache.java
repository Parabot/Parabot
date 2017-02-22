package org.parabot.core.arguments.landing;

import org.parabot.api.io.Directories;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class ClearCache implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"clearcache"};
	}

	@Override
	public void has(Object value) {
		Directories.clearCache();
	}
}

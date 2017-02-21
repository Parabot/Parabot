package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class NoSec implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"no_sec"};
	}

	@Override
	public void has(@Nullable Object value) {
		Core.disableSec();
	}
}

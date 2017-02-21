package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class Dump implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"dump"};
	}

	@Override
	public void has(@Nullable Object value) {
		Core.setDump(true);
	}
}

package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class WithLocal implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"withlocal"};
	}

	@Override
	public void has(@Nullable Object value) {
		Core.setMode(Core.LaunchMode.INCLUDE_LOCAL);
	}
}

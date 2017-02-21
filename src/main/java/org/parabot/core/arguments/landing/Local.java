package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class Local implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"local"};
	}

	@Override
	public void has(@Nullable Object value) {
		Core.setMode(Core.LaunchMode.LOCAL_ONLY);
	}
}

package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.api.io.Directories;
import org.parabot.core.arguments.LandingArgument;

import java.io.File;

/**
 * @author EmmaStone
 */
public class ServersBin implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"serversbin"};
	}

	@Override
	public void has(@Nullable Object value) {
		String[] values = value.toString().split(" ");
		int i = 0;

		Directories.setServerCompiledDirectory(new File(values[++i]));
	}
}

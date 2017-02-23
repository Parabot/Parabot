package org.parabot.core.arguments.landing;

import org.parabot.api.io.Directories;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class CreateDirs implements LandingArgument {

	@Override
	public String[] getArguments() {
		return new String[]{"createdirs"};
	}

	@Override
	public void has(Object value) {
		Directories.validate();
		System.out.println(TranslationHelper.translate(("DIRECTORIES_CREATED")));
		System.exit(0);
	}
}

package org.parabot;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.parabot.api.io.Directories;
import org.parabot.api.translations.TranslationHelper;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;
import org.parabot.core.ui.BotUI;

/**
 * @author Everel, JKetelaar, Matt, Dane
 * @version 3.0
 * @see <a href="http://www.parabot.org">Homepage</a>
 */
public final class Landing {

    public static void main(String... args) {
        parseArgs(args);

        Directories.validate();

        Core.verbose(TranslationHelper.translate("DEBUG_MODE") + Core.isMode(Core.LaunchMode.LOCAL_ONLY));

        if (!Core.isMode(Core.LaunchMode.LOCAL_ONLY) && Core.hasValidation() && !Core.isValid()) {
            Core.downloadNewVersion();
            return;
        }

        Core.verbose(TranslationHelper.translate("STARTING_LOGIN_GUI"));
        Core.getInjector().getInstance(BotUI.class).start();
    }

    private static void parseArgs(String... args) {
        OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        for (LandingArgument.Argument argument : LandingArgument.Argument.values()) {
            for (String s : argument.getLandingArgumentClass().getArguments()) {
                if (argument.isRequiredArg()) {
                    optionParser.accepts(s).withRequiredArg().ofType(String.class);
                } else {
                    optionParser.accepts(s);
                }
            }
        }

        OptionSet set = optionParser.parse(args);
        for (LandingArgument.Argument argument : LandingArgument.Argument.values()) {
            for (String s : argument.getLandingArgumentClass().getArguments()) {
                if (set.has(s)) {
                    argument.getLandingArgumentClass().has(set.valueOf(s));
                    break;
                }
            }
        }
    }
}

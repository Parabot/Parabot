package org.parabot.core.arguments.landing;

import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author JKetelaar
 */
public class Verbose implements LandingArgument {
    @Override
    public String[] getArguments() {
        return new String[]{"v", "verbose"};
    }

    @Override
    public void has() {
        Core.setVerbose(true);
    }
}

package org.parabot.core.arguments.landing;

import org.parabot.core.arguments.LandingArgument;

/**
 * @author EmmaStone
 */
public class Server implements LandingArgument {

    @Override
    public String[] getArguments() {
        return new String[]{ "server" };
    }

    @Override
    public void has(Object value) {
        String[] values = value.toString().split(" ");
        int      i      = 0;

        // ToDo: V3
    }
}

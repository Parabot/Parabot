package org.parabot.core.arguments.landing;

import com.sun.istack.internal.Nullable;
import org.parabot.core.Core;
import org.parabot.core.arguments.LandingArgument;

/**
 * @author JKetelaar
 */
public class NoValidation implements LandingArgument {
    @Override
    public String[] getArguments() {
        return new String[]{"no_validation"};
    }

    @Override
    public void has(@Nullable Object value) {
        Core.disableValidation();
    }
}

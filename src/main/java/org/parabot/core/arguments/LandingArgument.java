package org.parabot.core.arguments;

import org.parabot.core.Core;
import org.parabot.core.arguments.landing.NoValidation;
import org.parabot.core.arguments.landing.Verbose;

/**
 * @author JKetelaar
 */
public interface LandingArgument {
    String[] getArguments();
    void has();

    public enum Argument {
        VERBOSE(Verbose.class),
        NO_VALIDATION(NoValidation.class);

        private Class<?> argumentClass;

        Argument(Class<?> argumentClass) {
            this.argumentClass = argumentClass;
        }

        public LandingArgument getLandingArgumentClass(){
            return (LandingArgument) Core.getInjector().getInstance(this.argumentClass);
        }
    }
}

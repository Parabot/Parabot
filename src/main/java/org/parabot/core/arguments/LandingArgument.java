package org.parabot.core.arguments;

import org.parabot.core.Core;
import org.parabot.core.arguments.landing.*;

/**
 * @author JKetelaar
 */
public interface LandingArgument {
    String[] getArguments();

    void has(Object value);

    enum Argument {
        AUTH(Auth.class, true),
        CLEAR_CACHE(ClearCache.class, false),
        CREATE_DIRS(CreateDirs.class, false),
        DUMP(Dump.class, false),
        LOCAL(Local.class, false),
        MAC(Mac.class, true),
        NO_SEC(NoSec.class, false),
        NO_VALIDATION(NoValidation.class, false),
        PROXY(Proxy.class, true),
        SCRIPTS_BIN(ScriptsBin.class, true),
        SERVER(Server.class, true),
        SERVERS_BIN(ScriptsBin.class, true),
        VERBOSE(Verbose.class, false),
        WITH_LOCAL(WithLocal.class, false);

        private Class<?> argumentClass;
        private boolean  requiredArg;

        Argument(Class<?> argumentClass, boolean requiredArg) {
            this.argumentClass = argumentClass;
            this.requiredArg = requiredArg;
        }

        public LandingArgument getLandingArgumentClass() {
            return (LandingArgument) Core.getInjector().getInstance(this.argumentClass);
        }

        public boolean isRequiredArg() {
            return requiredArg;
        }
    }
}

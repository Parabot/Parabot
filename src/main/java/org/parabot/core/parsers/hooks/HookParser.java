package org.parabot.core.parsers.hooks;

import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.asm.wrappers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Parses an XML files which injects the hooks and other bytecode manipulation
 * methods
 *
 * @author Everel
 */
public abstract class HookParser {


    public HookParser(HookFile hookFile) {

    }

    public abstract Interface[] getInterfaces();

    public abstract Super[] getSupers();

    public abstract Getter[] getGetters();

    public abstract Setter[] getSetters();

    public abstract Invoker[] getInvokers();

    public abstract Callback[] getCallbacks();

    public abstract HashMap<String, String> getConstants();

    public Injectable[] getInjectables() {
        ArrayList<Injectable> injectables = new ArrayList<>();

        Interface[] interfaces = getInterfaces();
        Collections.addAll(injectables, interfaces);

        Getter[] getters = getGetters();
        Collections.addAll(injectables, getters);

        Setter[] setters = getSetters();
        Collections.addAll(injectables, setters);

        Invoker[] invokers = getInvokers();
        Collections.addAll(injectables, invokers);

        Callback[] callbacks = getCallbacks();
        Collections.addAll(injectables, callbacks);

        Super[] supers = getSupers();
        Collections.addAll(injectables, supers);

        if (injectables.isEmpty()) {
            return new Injectable[0];
        }

        return injectables.toArray(new Injectable[injectables.size()]);
    }
}

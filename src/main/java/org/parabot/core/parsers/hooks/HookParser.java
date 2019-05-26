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
        if (interfaces != null) {
            Collections.addAll(injectables, interfaces);
        }
        Getter[] getters = getGetters();
        if (getters != null) {
            Collections.addAll(injectables, getters);
        }
        Setter[] setters = getSetters();
        if (setters != null) {
            Collections.addAll(injectables, setters);
        }
        Super[] supers = getSupers();
        if (supers != null) {
            Collections.addAll(injectables, supers);
        }
        Invoker[] invokers = getInvokers();
        if (invokers != null) {
            Collections.addAll(injectables, invokers);
        }
        Callback[] callbacks = getCallbacks();
        if (callbacks != null) {
            Collections.addAll(injectables, callbacks);
        }
        return injectables.toArray(new Injectable[injectables.size()]);
    }

}

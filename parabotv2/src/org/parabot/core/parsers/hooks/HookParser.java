package org.parabot.core.parsers.hooks;

import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.interfaces.Injectable;
import org.parabot.core.asm.wrappers.*;
import java.util.ArrayList;
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
        ArrayList<Injectable> injectables = new ArrayList<Injectable>();
        Interface[] interfaces = getInterfaces();
        if (interfaces != null) {
            for (Interface inf : interfaces) {
                injectables.add(inf);
            }
        }
        Getter[] getters = getGetters();
        if (getters != null) {
            for (Getter get : getters) {
                injectables.add(get);
            }
        }
        Setter[] setters = getSetters();
        if (setters != null) {
            for (Setter set : setters) {
                injectables.add(set);
            }
        }
        Super[] supers = getSupers();
        if (supers != null) {
            for (Super sup : supers) {
                injectables.add(sup);
            }
        }
        Invoker[] invokers = getInvokers();
        if (invokers != null) {
            for (Invoker vok : invokers) {
                injectables.add(vok);
            }
        }
        Callback[] callbacks = getCallbacks();
        if (callbacks != null) {
            for (Callback callback : callbacks) {
                injectables.add(callback);
            }
        }
        return injectables.toArray(new Injectable[injectables.size()]);
    }

}

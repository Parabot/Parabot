package org.parabot.core.asm.wrappers;

import org.parabot.core.asm.adapters.AddSuperAdapter;
import org.parabot.core.asm.interfaces.Injectable;

/**
 * This class is used for changing the super class of a class
 *
 * @author Everel
 */
public class Super implements Injectable {
    private final String className;
    private final String superClassName;

    public Super(String className, String superClassName) {
        this.className = className;
        this.superClassName = superClassName;
    }

    /**
     * Adds a superclass to a class
     * Short route for getAdapter().inject
     */
    @Override
    public void inject() {
        getAdapter().inject();
    }

    public AddSuperAdapter getAdapter() {
        return new AddSuperAdapter(className, superClassName);
    }

}

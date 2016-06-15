package org.parabot.core.ui.listeners.key;

/**
 * @author JKetelaar
 */
public abstract class Binding {

    private int key;

    public Binding(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public abstract void perform();
}

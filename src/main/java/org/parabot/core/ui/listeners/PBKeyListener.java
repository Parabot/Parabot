package org.parabot.core.ui.listeners;

import org.parabot.core.ui.listeners.key.ActionEventBinding;
import org.parabot.core.ui.listeners.key.Binding;
import org.parabot.environment.OperatingSystem;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JKetelaar
 */
public class PBKeyListener implements KeyListener {

    private int mainKey;

    private List<Binding> bindings;

    public PBKeyListener() {
        this.bindings = new ArrayList<>();
        this.mainKey = (OperatingSystem.getOS() == OperatingSystem.MAC ? KeyEvent.VK_META : KeyEvent.VK_CONTROL);
        this.fillBindings();
    }

    public PBKeyListener(int mainKey) {
        this.bindings = new ArrayList<>();
        this.mainKey = mainKey;
        this.fillBindings();
    }

    private void fillBindings() {
        this.bindings.add(new ActionEventBinding(KeyEvent.VK_R, "Run"));
        this.bindings.add(new ActionEventBinding(KeyEvent.VK_R, "Stop"));
    }

    public int getMainKey() {
        return mainKey;
    }

    public void setMainKey(int mainKey) {
        this.mainKey = mainKey;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void addBinding(Binding binding) {
        this.bindings.add(binding);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == mainKey) {
            for (Binding binding : bindings) {
                if (binding.getKey() == e.getKeyCode()) {
                    binding.perform();
                }
            }
        }
    }
}

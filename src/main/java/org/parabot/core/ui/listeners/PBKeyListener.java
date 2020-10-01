package org.parabot.core.ui.listeners;

import org.parabot.core.ui.listeners.key.ActionEventBinding;
import org.parabot.core.ui.listeners.key.Binding;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JKetelaar
 */
public class PBKeyListener implements KeyListener {

    private List<Binding> bindings;

    public PBKeyListener() {
        this.bindings = new ArrayList<>();
        this.fillBindings();
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void addBinding(Binding binding) {
        for (Binding bind : this.bindings) {
            if (bind.getKey() == binding.getKey()) {
                return;
            }
        }
        this.bindings.add(binding);
    }

    public void resetBindings() {
        this.bindings = new ArrayList<>();
        this.fillBindings();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            for (Binding binding : bindings) {
                if (binding.getKey() == e.getKeyCode()) {
                    binding.perform();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void fillBindings() {
        this.bindings.add(new ActionEventBinding(KeyEvent.VK_R, "Run"));
        this.bindings.add(new ActionEventBinding(KeyEvent.VK_R, "Stop"));
    }
}

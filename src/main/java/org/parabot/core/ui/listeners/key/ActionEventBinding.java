package org.parabot.core.ui.listeners.key;

import org.parabot.core.ui.BotUI;

/**
 * @author JKetelaar
 */
public class ActionEventBinding extends Binding {

    private final String actionString;

    public ActionEventBinding(int key, String actionString) {
        super(key);
        this.actionString = actionString;
    }

    @Override
    public void perform() {
        BotUI.getInstance().performCommand(actionString);
    }

}

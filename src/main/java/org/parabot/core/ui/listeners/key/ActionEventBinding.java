package org.parabot.core.ui.listeners.key;

import org.parabot.core.Core;
import org.parabot.core.ui.BotUI;

/**
 * @author JKetelaar
 */
public class ActionEventBinding extends Binding {

    private String actionString;

    public ActionEventBinding(int key, String actionString) {
        super(key);
        this.actionString = actionString;
    }

    @Override
    public void perform() {
        // ToDo: Perform command in V3
        // Core.getInjector().getInstance(BotUI.class).performCommand(actionString);
    }

}

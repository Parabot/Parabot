package org.parabot.core.ui.fonts;

import java.awt.Font;
import java.util.ArrayList;

/**
 * @author Capslock
 */
public class Fonts {
    private static final java.util.List<ParabotFont> FONT_CACHE = new ArrayList<>();

    /**
     * Calls the getResource with the default size of 12
     *
     * @param resource
     *
     * @return
     */
    public static Font getResource(final String resource) {
        return getResource(resource, 12f);
    }

    public static Font getResource(final String fileName, float size) {
        ParabotFont parabotFont = null;

        for (ParabotFont font : FONT_CACHE) {
            if (font.getLocation().equalsIgnoreCase(fileName) && font.getSize() == size) {
                parabotFont = font;
            }
        }

        if (parabotFont == null) {
            parabotFont = new ParabotFont(fileName, size);
            FONT_CACHE.add(parabotFont);
        }

        return parabotFont.getFont();
    }
}

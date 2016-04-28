package org.parabot.core.ui.fonts;

import org.parabot.core.ui.utils.Fonts;

import java.awt.*;
import java.io.IOException;

/**
 * @author Capslock
 */
public class ParabotFont {

    private String name;
    private String location;
    private Font font;

    public ParabotFont(String location, float size) {
        if (!location.toLowerCase().startsWith("/storage/fonts/")){
            location = "/storage/fonts/" + location;
        }
        this.location = location;

        try {
            this.font = createFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private Font createFont(float size) throws IOException, FontFormatException {
        return Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream(this.location)).deriveFont(size);
    }

    public float getSize() {
        return font.getSize();
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof ParabotFont) {
                ParabotFont otherFont = (ParabotFont) obj;
                if (otherFont.getName().equalsIgnoreCase(this.getName())) {
                    if (otherFont.getSize() == this.getSize()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

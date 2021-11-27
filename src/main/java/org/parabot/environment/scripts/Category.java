package org.parabot.environment.scripts;

import org.parabot.core.ui.images.Images;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Holds script categories
 *
 * @author Dane, Paradox
 */
public enum Category {

    AGILITY, COMBAT, COOKING, CRAFTING, CONSTRUCTION, DUNGEONEERING, FARMING, FIREMAKING, FISHING, FLETCHING, HERBLORE, HUNTER, MAGIC, MINIGAMES, MINING, MONEYMAKING, OTHER, PRAYER, RUNECRAFTING, SLAYER, SMITHING, THIEVING, UTILITY, WOODCUTTING;

    /**
     * Cache
     */
    private static final HashMap<String, BufferedImage> images = new HashMap<>();

    static {
        images.put("script", Images.getResource("/storage/images/category/script.png"));
    }

    /**
     * Gets category icon image from filename
     *
     * @param s Name of the image - used for the hashmap index
     *
     * @return icon
     */
    public static BufferedImage getIcon(String s) {
        if (images.get(s) == null) {
            images.put(s, Images.getResource("/storage/images/category/" + s + ".png"));
        }
        return images.get(s);
    }

    /**
     * Gets image belonging to this category
     *
     * @return icon
     */
    public BufferedImage getIcon() {
        return Category.getIcon(this.name().toLowerCase());
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(name().charAt(0));
        b.append(name().toLowerCase().substring(1));
        return new String(b);
    }

}

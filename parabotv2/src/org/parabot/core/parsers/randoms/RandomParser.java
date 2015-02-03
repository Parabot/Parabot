package org.parabot.core.parsers.randoms;

import java.util.ArrayList;

/**
 * @author JKetelaar
 */
public abstract class RandomParser {

    private static final ArrayList<RandomParser> parsers = new ArrayList<>();

    public static void enable() {
        parsers.add(new PublicRandoms());

        for (RandomParser randomParser : parsers) {
            randomParser.parse();
        }
    }

    public abstract void parse();
}

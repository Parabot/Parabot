package org.parabot.environment.randoms;

/**
 * @author JKetelaar
 */
public enum RandomType {

    SCRIPT(0, "Script"),
    ON_SCRIPT_START(1, "On script start"),
    ON_SERVER_START(2, "On server start"),
    ON_SCRIPT_FINISH(3, "On script finish");

    private final int id;
    private final String name;

    RandomType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RandomType getDefault() {
        return SCRIPT;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

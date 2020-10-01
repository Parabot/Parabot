package org.parabot.environment.api.utils;

import org.parabot.core.ui.utils.UILog;

public class Version implements Comparable<Version> {

    private static boolean notified;
    private final String version;

    public Version(String version) {
        if (version == null) {
            throw new IllegalArgumentException("Version can not be null");
        }
        if (!version.matches("[0-9]+(\\.[0-9]+)*") && !version.contains("RC")) {
            throw new IllegalArgumentException("Invalid version format");
        }
        this.version = version;
    }

    public final String get() {
        return this.version;
    }

    public boolean isNightly() {
        return this.version.contains("RC");
    }

    @Override
    public int compareTo(Version that) {
        if (that == null) {
            return 1;
        }

        if (version.contains("RC")) {
            notifyRC();
            return 1;
        }

        String[] thisParts = this.get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart) {
                return -1;
            }
            if (thisPart > thatPart) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that != null && this.getClass() == that.getClass() && this.compareTo((Version) that) == 0;
    }

    private static void notifyRC() {
        if (!notified) {
            UILog.log(
                    "Version warning",
                    "This is an RC version of Parabot\n" +
                            "This could be an unstable version of Parabot, and might crash at anytime\n\n" +
                            "If you find an error within the client, please report any at:\n" +
                            "https://github.com/Parabot/Parabot/issues"
            );
            notified = true;
        }
    }
}
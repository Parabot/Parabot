package org.parabot.environment.api.utils;

/**
 * A simple timer class
 *
 * @author Everel, Parameter
 */
public class Timer {
    private long start;
    private long end;

    /**
     * Timer Constructor
     *
     * @param end
     */
    public Timer(long end) {
        start = System.currentTimeMillis();
        this.end = System.currentTimeMillis() + end;
    }

    /**
     * Timer Constructor
     *
     * @param end
     * @param start
     */
    public Timer(long end, long start) {
        this.start = start;
        this.end = System.currentTimeMillis() + end;
    }

    /**
     * Timer Constructor
     */
    public Timer() {
        this(0);
    }

    /**
     * Determines the remaining time left.
     *
     * @return the remaining time.
     */
    public long getRemaining() {
        return end - System.currentTimeMillis();
    }

    /**
     * Determines if the end time has been reached, does not mean it stopped
     * running.
     */
    public boolean isFinished() {
        return System.currentTimeMillis() > end;
    }

    /**
     * Stops and resets the timer
     */
    public void restart() {
        stop();
        reset();
    }

    /**
     * Resets the timer if stopped
     */
    public void reset() {
        if (start == 0) {
            start = System.currentTimeMillis();
        }
    }

    /**
     * Resets the timer
     */
    public void stop() {
        end = (end - start) + System.currentTimeMillis();
        start = 0;
    }

    /**
     * Determines if timer is running
     *
     * @return <b>true</b> if timer is running
     */
    public boolean isRunning() {
        return start != 0;
    }

    /**
     * Gets the run time in long millis.
     *
     * @return the elapsed time.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - start;
    }

    /**
     * Calculates hourly gains based on given variable
     *
     * @param gained variable
     *
     * @return hourly gains
     */
    public int getPerHour(final int gained) {
        return getPerHour(gained, 0);
    }

    /**
     * Calculates hourly gains based on given variable, with variable start amount
     *
     * @param gained      total gained amount
     * @param startAmount start amount
     *
     * @return hourly gains
     */
    public int getPerHour(final int gained, final int startAmount) {
        return (int) (((gained - startAmount) * 3600000D) / (System.currentTimeMillis() - start));
    }

    /**
     * Generates string based on DD:HH:MM:SS
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        long elapsed = getElapsedTime();
        int day = (int) (elapsed / 86400000);
        elapsed -= day * 86400000;
        int hour = (int) (elapsed / 3600000);
        elapsed -= hour * 3600000;
        int minute = (int) (elapsed / 60000);
        elapsed -= minute * 60000;
        int second = (int) (elapsed / 1000);
        if (day > 0) {
            b.append(day).append("d:");
        }
        b.append(hour < 10 ? "0" : "").append(hour).append(":");
        b.append(minute < 10 ? "0" : "").append(minute).append(":");
        b.append(second < 10 ? "0" : "").append(second);
        return new String(b);
    }
}

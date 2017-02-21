package org.parabot.environment.randoms;

import com.google.inject.Singleton;
import org.parabot.core.Core;
import org.parabot.core.parsers.randoms.RandomParser;
import org.parabot.core.ui.Logger;

import java.util.ArrayList;

/**
 * @author JKetelaar
 */
@Singleton
public class RandomHandler {
    private ArrayList<Random> randoms;

    /**
     * The randoms that will actually run
     */
    private ArrayList<Random> activeRandoms;

    public RandomHandler() {
        this.randoms = new ArrayList<>();
        this.activeRandoms = new ArrayList<>();
    }

    public void init() {
        RandomParser.enable();
        checkAndRun(RandomType.ON_SERVER_START);
    }

    /**
     * Adds a random to the random list
     *
     * @param random The random that will be added to the arraylist
     */
    public void addRandom(Random random) {
        if (random == null) {
            throw new NullPointerException("Null random");
        }
        for (Random r : randoms) {
            if (r.getClass() == random.getClass()) {
                Core.verbose("Ignored added random, duplicate.");
                return;
            }
        }
        randoms.add(random);
        setActive(random);
    }

    /**
     * @param random
     * @deprecated
     */
    @Deprecated
    public void addRandom(org.parabot.environment.scripts.randoms.Random random) {
        new IllegalArgumentException("This type of random is deprecated").printStackTrace();
    }

    /**
     * Adds a random to the active randoms
     *
     * @param random
     */
    public void setActive(Random random) {
        this.activeRandoms.add(random);
    }

    /**
     * Adds a random to the active randoms
     *
     * @param random
     */
    public void setActive(String random) {
        for (Random r : this.randoms) {
            if (r.getName().equalsIgnoreCase(random.toLowerCase())) {
                this.activeRandoms.add(r);
            }
        }
    }

    /**
     * Sets the whole random arraylist to the arraylist given as argument
     *
     * @param randoms The new random arraylist
     */
    public void setRandoms(ArrayList<Random> randoms) {
        this.randoms = randoms;
    }

    /**
     * Clears all added randoms
     */
    public void clearRandoms() {
        this.randoms.clear();
    }

    /**
     * Clears all active randoms
     */
    public void clearActiveRandoms() {
        this.activeRandoms.clear();
    }

    /**
     * Checks if random occurs and runs it
     *
     * @return returns <b>true</b> if a random has been executed, otherwise <b>false</b>
     */
    public boolean checkAndRun(RandomType type) {
        for (Random r : this.activeRandoms) {
            if (r.getRandomType().getId() == type.getId() && r.activate()) {
                Logger.addMessage("Running random '" + r.getName() + "'", true);
                try {
                    r.execute();
                }catch (Exception e){
                    Logger.addMessage("Random failed: '" + r.getName() + "'", false);
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if random occurs and runs it
     *
     * @return returns <b>true</b> if a random has been executed, otherwise <b>false</b>
     * @see RandomHandler#checkAndRun(RandomType)
     * @deprecated
     */
    @Deprecated
    public boolean checkAndRun() {
        new IllegalArgumentException("This method is deprecated").printStackTrace();
        return false;
    }

    public ArrayList<Random> getRandoms() {
        return this.randoms;
    }

    public ArrayList<Random> getActiveRandoms() {
        return this.activeRandoms;
    }
}
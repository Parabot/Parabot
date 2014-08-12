package org.parabot.environment.scripts.randoms;

import java.util.ArrayList;

import org.parabot.core.Core;

/**
 * 
 * @author Everel
 *
 */
public class RandomHandler {
    private ArrayList<Random> randoms;
    
    public RandomHandler() {
    	this.randoms = new ArrayList<>();
    }

    /**
     * Adds a random to the random list
     * @param random
     */
    public void addRandom(Random random) {
    	if(random == null) {
    		throw new NullPointerException("Null random");
    	}
    	for(Random r : randoms) {
    		if(r.getClass() == random.getClass()) {
    			Core.verbose("Ignored added random, duplicate.");
    			return;
    		}
    	}
        randoms.add(random);
    }
    
    /**
     * Clears all added randoms
     */
    public void clearRandoms() {
    	randoms.clear();
    }

    /**
     * Checks if random occurs and runs it
     * @return returns <b>true</b> if a random has been executed, otherwise <b>false</b>
     */
    public boolean checkAndRun() {
        for(Random r : randoms) {
            if(r.activate()) {
                r.execute();
                return true;
            }
        }
        return false;
    }
}
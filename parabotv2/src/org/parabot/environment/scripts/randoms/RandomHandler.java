package org.parabot.environment.scripts.randoms;

import org.parabot.core.Core;

import java.util.ArrayList;

/**
 * 
 * @author Everel
 *
 */
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

    /**
     * Adds a random to the random list
     * @param random The random that will be added to the arraylist
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
        setActive(random);
    }

    /**
     * Adds a random to the active randoms
     * @param random
     */
    public void setActive(Random random){
        this.activeRandoms.add(random);
    }

    /**
     * Adds a random to the active randoms
     * @param random
     */
    public void setActive(String random){
        for (Random r : this.randoms){
            if (r.getName().equalsIgnoreCase(random.toLowerCase())){
                this.activeRandoms.add(r);
            }
        }
    }

    /**
     * Sets the whole random arraylist to the arraylist given as argument
     * @param randoms The new random arraylist
     */
    public void setRandoms(ArrayList<Random> randoms){
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
    public void clearActiveRandoms(){
        this.activeRandoms.clear();
    }

    /**
     * Checks if random occurs and runs it
     * @return returns <b>true</b> if a random has been executed, otherwise <b>false</b>
     */
    public boolean checkAndRun() {
        for(Random r : this.activeRandoms) {
            if(r.activate()) {
                Core.verbose("Running random '" + r.getName() + "'.");
                r.execute();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Random> getRandoms(){
        return this.randoms;
    }

    public ArrayList<Random> getActiveRandoms(){
        return this.activeRandoms;
    }
}
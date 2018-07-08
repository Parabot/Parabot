package org.parabot.core;

import java.util.concurrent.Semaphore;

//This stuff really belongs in the Context class but the way you guys currently have it doing stuff is funky so I put it here instead

public class GameSync{
    
    //start with 0 permitted until the game releases the semaphore
    private static final Semaphore SEMAPHORE = new Semaphore(0);
    
    private GameSync(){
        
    }
    
    public static void lock(){
        try{
            SEMAPHORE.acquire();
        }catch(InterruptedException e){
            //TODO you need to handle this in some way
        }
    }
    
    public static void unlock(){
        SEMAPHORE.release();
    }
    
}
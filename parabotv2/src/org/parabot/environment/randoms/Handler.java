package org.parabot.environment.randoms;

import org.parabot.environment.scripts.Script;

import java.util.ArrayList;

/**
 * User: Jeroen
 * Date: 18/02/14
 * Time: 19:37
 */
public class Handler {


    public static class RandomChecker {
        private ArrayList<Random> randoms;

        public void addRandom(Random random) {
            randoms.add(random);
        }

        public void checkAndRun() {
            Script s = new Script();
            for(Random r : randoms) {
                if(r.shouldRun()) {
                    s.setState(1);
                    r.run();
                }
            }
        }
    }

    public interface Random {
        public boolean shouldRun();
        public void run();
    }

    public abstract class RandomScript {
        //private RandomChecker randomChecker = ServerProvider.getRandomChecker();

        public void scriptLoop() {
            while(true) {
               // randomChecker.checkAndRun();
               // framework.loop();
            }
        }
    }

}

package org.parabot.environment.scripts.framework;

import java.util.Collection;

/**
 * Holds various script frameworks
 *
 * @author Everel
 */
public class Frameworks {

    public static Looper getLooper(LoopTask loopTask) {
        return new Looper(loopTask);
    }

    public static StrategyWorker getStrategyWorker(Collection<Strategy> strategies) {
        return new StrategyWorker(strategies);
    }

}

class Looper extends AbstractFramework {
    private LoopTask loopTask = null;

    public Looper(LoopTask loopTask) {
        this.loopTask = loopTask;
    }

    @Override
    public boolean execute() {
        int sleepTime = loopTask.loop();
        if (sleepTime < 0) {
            return false;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class StrategyWorker extends AbstractFramework {
    private final Collection<Strategy> strategies;

    public StrategyWorker(Collection<Strategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean execute() {
        for (Strategy s : strategies) {
            if (s.activate()) {
                s.execute();
                return true;
            }
        }
        return true;
    }

}

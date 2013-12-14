package org.parafork.environment.scripts;

import java.util.Collection;

import org.parafork.core.Context;
import org.parafork.core.Core;
import org.parafork.core.ui.components.BotToolbar;
import org.parafork.core.ui.components.LogArea;
import org.parafork.environment.api.utils.Time;
import org.parafork.environment.scripts.framework.AbstractFramework;
import org.parafork.environment.scripts.framework.LoopTask;
import org.parafork.environment.scripts.framework.SleepCondition;
import org.parafork.environment.scripts.framework.Strategy;

/**
 * 
 * Script template, scripts are 'add-ons' which executes various tasks in-game
 * 
 * @author Everel
 *
 */
public class Script implements Runnable {
	private Collection<Strategy> strategies = null;
	private int frameWorkType = 0;
	private AbstractFramework frameWork = null;
	
	public static final int TYPE_STRATEGY = 0;
	public static final int TYPE_LOOP = 1;
	public static final int TYPE_OTHER = 2;
	
	private int state = 0;
	public static final int STATE_RUNNING = 0;
	public static final int STATE_PAUSE = 1;
	public static final int STATE_STOPPED = 2;
	
	public boolean onExecute() { 
		return true;
	}
	
	public void onFinish() {
		
	}
	
	public final void provide(final Collection<Strategy> strategies) {
		this.strategies = strategies;
	}
	
	public final int getFrameWorkType() {
		return frameWorkType;
	}
	
	public final void setFrameWork(int frameWorkType) {
		if(frameWorkType < 0 || frameWorkType > 2) {
			throw new RuntimeException("Invalid framework type");
		}
		this.frameWorkType = frameWorkType;
	}
	
	public final void setAbstractFrameWork(AbstractFramework f) {
		this.frameWork = f;
	}

	@Override
	public final void run() {
		Core.verbose("Initializing script...");
		Context.resolve().getServerProvider().initScript(this);
		Core.verbose("Done.");
		
		if(!onExecute()) {
			Core.verbose("Script#onExecute returned false, unloading and stopping script...");
			Context.resolve().getServerProvider().unloadScript(this);
			this.state = STATE_STOPPED;
			Core.verbose("Done.");
			return;
		}
		
		Core.verbose("Detecting script framework...");
		Context.resolve().setRunningScript(this);
		BotToolbar.getInstance().toggleRun();
		if(this instanceof LoopTask) {
			Core.verbose("Script framework detected: LoopTask");
			frameWorkType = TYPE_LOOP;
			frameWork = Frameworks.getLooper((LoopTask) this);
		} else if(strategies != null && !strategies.isEmpty()) {
			Core.verbose("Script framework detected: Strategies");
			frameWorkType = TYPE_STRATEGY;
			frameWork = Frameworks.getStrategyWorker(strategies);
		} else {
			Core.verbose("Unknown script framework: Other");
			frameWorkType = TYPE_OTHER;
		}
		Core.verbose("Running script...");
		LogArea.log("Script started.");
		try {
			while(this.state != STATE_STOPPED) {
				if(this.state == STATE_PAUSE) {
					sleep(500);
					continue;
				}
				if(!frameWork.execute()) {
					break;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		Core.verbose("Script stopped/finished, unloading and stopping...");
		onFinish();
		LogArea.log("Script stopped.");
		Context.resolve().getServerProvider().unloadScript(this);
		this.state = STATE_STOPPED;
		Context.resolve().setRunningScript(null);
		BotToolbar.getInstance().toggleRun();
		Core.verbose("Done.");
	}
	
	/**
	 * Sleeps until the SleepCondition is valid.
	 * 
	 * @param conn
	 *            the condition.
	 * @param timeout
	 *            the time in miliseconds before it stops sleeping.
	 * @return whether it ran successfully without timing out.
	 */
	public final boolean sleep(SleepCondition conn, int timeout) {
		long start = System.currentTimeMillis();
		while (!conn.isValid()) {
			if (start + timeout < System.currentTimeMillis()) {
				return false;
			}
			Time.sleep(50);
		}
		return true;
	}
	
	/**
	 * Sets the script's state
	 * @param state
	 */
	public final void setState(final int state) {
		if(state < 0 || state > 2) {
			throw new IllegalArgumentException("Illegal state");
		}
		this.state = state;
	}
	
	/**
	 * Sleeps for an amount of milliseconds
	 * @param ms
	 */
	public final void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}

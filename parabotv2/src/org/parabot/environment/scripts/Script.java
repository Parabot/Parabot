package org.parabot.environment.scripts;

import java.util.Collection;

import org.parabot.core.Context;
import org.parabot.core.ui.components.BotToolbar;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.AbstractFramework;
import org.parabot.environment.scripts.framework.LoopTask;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;

/**
 * 
 * @author Clisprail
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
		Context.resolve().getServerProvider().initScript(this);
		if(!onExecute()) {
			Context.resolve().getServerProvider().unloadScript(this);
			this.state = STATE_STOPPED;
			return;
		}
		
		Context.resolve().setRunningScript(this);
		BotToolbar.getInstance().toggleRun();
		if(this instanceof LoopTask) {
			frameWorkType = TYPE_LOOP;
			frameWork = Frameworks.getLooper((LoopTask) this);
		} else if(strategies != null && !strategies.isEmpty()) {
			frameWorkType = TYPE_STRATEGY;
			frameWork = Frameworks.getStrategyWorker(strategies);
		} else {
			frameWorkType = TYPE_OTHER;
		}
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
		onFinish();
		
		Context.resolve().getServerProvider().unloadScript(this);
		this.state = STATE_STOPPED;
		Context.resolve().setRunningScript(null);
		BotToolbar.getInstance().toggleRun();
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

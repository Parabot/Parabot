package org.parabot.core.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

/**
 * Logs to System.out
 */
public class SystemConsoleHandler extends ConsoleHandler {
	public SystemConsoleHandler() {
		super();
		setOutputStream(System.out);
	}
	
	@Override
	public void publish(final LogRecord logRecord) {
		System.out.println("PUBLISH");
		System.out.println(logRecord.getMessage());
	}
}

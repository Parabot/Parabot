package org.parabot.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private final boolean appendNewLine;

	public LogFormatter()
	{
		this(true);
	}

	public LogFormatter(final boolean appendNewLine)
	{
		this.appendNewLine = appendNewLine;
	}

	@Override
	public String format(final LogRecord record) {
		final StringBuilder result = new StringBuilder().append("[").append(record.getLevel().getName()).append("] ").append(new Date(record.getMillis())).append(": ").append(record.getLoggerName()).append(": ").append(record.getMessage()).append(throwableToString(record.getThrown()));
		if (appendNewLine) {
			result.append(LogFormatter.LINE_SEPARATOR);
		}
		return result.toString();
	}

	@Override
	public String formatMessage(final LogRecord record) {
		return String.format(record.getMessage());
	}

	private String throwableToString(final Throwable t) {
		if (t != null) {
			final Writer exception = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(exception);
			t.printStackTrace(printWriter);
			return exception.toString();
		}
		return "";
	}
}

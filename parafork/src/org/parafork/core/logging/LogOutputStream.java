package org.parafork.core.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Paris
 */
public class LogOutputStream extends OutputStream {
	protected boolean hasBeenClosed = false;
	protected Logger category;
	protected Level priority;
	protected StringBuilder buffer;

	public LogOutputStream(final Logger category, final Level priority) {
		this.priority = priority;
		this.category = category;
		buffer = new StringBuilder();
	}

	@Override
	public void close() {
		flush();
		hasBeenClosed = true;
	}

	@Override
	public void flush() {
		final String txt = buffer.toString().replace("\\s+$", "");
		if (txt.trim().length() != 0) {
			category.log(priority, txt);
		}
		reset();
	}

	private void reset() {
		buffer.setLength(0);
	}

	@Override
	public void write(final int b) throws IOException {
		if (hasBeenClosed) {
			throw new IOException("The stream has been closed.");
		} else if (b != 0) {
			buffer.append((char) (b & 0xff));
		}
	}
}
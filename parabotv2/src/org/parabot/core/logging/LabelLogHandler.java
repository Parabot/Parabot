package org.parabot.core.logging;

import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JLabel;

public class LabelLogHandler extends Handler {
	public final JLabel label;
	private final Color defaultColor;

	public LabelLogHandler() {
		this.label = new JLabel();
		this.defaultColor = label.getForeground();
	}

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
	}

	@Override
	public void publish(final LogRecord record) {
		StringBuilder b = new StringBuilder(record.getMessage());

		if (record.getLevel().intValue() > Level.WARNING.intValue()) {
			label.setForeground(new Color(0xcc0000));
		} else {
			label.setForeground(defaultColor);
			b.append(" ...");
		}

		label.setText(new String(b));
	}
}

package org.parafork.core.ui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.parafork.core.Core;
import org.parafork.core.logging.LogFormatter;
import org.parafork.core.logging.LogTextArea;
import org.parafork.core.logging.SystemConsoleHandler;
import org.parafork.core.logging.TextAreaLogHandler;

/**
 * 
 * The LogArea of the BotUI
 * 
 * @author Everel
 *
 */
public class LogArea extends JScrollPane {

	private static final long serialVersionUID = 6571141103751675714L;
	private static LogArea instance = null;
	private static LogTextArea logArea = new LogTextArea();

	private LogArea() {
		super(TextAreaLogHandler.TEXT_AREA,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVisible(true);
		registerLogging();
	}

	public static LogArea getInstance() {
		return instance == null ? instance = new LogArea() : instance;
	}

	private static final Logger log = Logger.getLogger("Bot");

	public static void log(String s) {
		log.info(s);
	}

	public static void error(String s) {
		log.severe(s);
	}

	public static void clearLog() {
		logArea.clearSelection();
	}

	public static void hideLog() {
		logArea.setVisible(false);
	}

	public static void showLog() {
		logArea.setVisible(true);
	}

	public void registerLogging() {
		Core.verbose("Registering logging...");
		final Properties logging = new Properties();
		final String logFormatter = LogFormatter.class.getCanonicalName();
		final String fileHandler = FileHandler.class.getCanonicalName();
		logging.setProperty("handlers",
				TextAreaLogHandler.class.getCanonicalName() + "," + fileHandler);
		logging.setProperty(".level", "INFO");
		logging.setProperty(SystemConsoleHandler.class.getCanonicalName()
				+ ".formatter", logFormatter);
		logging.setProperty(fileHandler + ".formatter", logFormatter);
		logging.setProperty(TextAreaLogHandler.class.getCanonicalName()
				+ ".formatter", logFormatter);
		final ByteArrayOutputStream logout = new ByteArrayOutputStream();
		try {
			logging.store(logout, "");
			LogManager.getLogManager().readConfiguration(
					new ByteArrayInputStream(logout.toByteArray()));
		} catch (final Exception ignored) {
		}
		Core.verbose("Done.");
	}

}

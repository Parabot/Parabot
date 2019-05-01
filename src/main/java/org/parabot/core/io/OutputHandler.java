package org.parabot.core.io;

import org.parabot.core.Directories;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class OutputHandler {

	public OutputHandler() {
	}

	public void setOutput() throws FileNotFoundException {


		// Create a new file output stream.
		final PrintStream fileOut = new PrintStream(Directories.getSettingsPath() + "\\out.txt");
		// Create a new file error stream.
		final PrintStream fileErr = new PrintStream(Directories.getSettingsPath() + "\\err.txt");


		System.setOut(new PrintStream(System.out) {
			public void println(String s) {
				fileOut.println(s);
				super.println(s);
			}
		});

		System.setErr(new PrintStream(System.err) {
			public void println(String s) {
				fileErr.println(s);
				super.println(s);
			}
		});
	}
}

package org.parabot.environment;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.lib.Library;
import org.parabot.core.lib.javafx.JavaFX;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.environment.api.utils.WebUtil;

import java.util.LinkedList;


/**
 * 
 * Initiliazes the bot environment
 * 
 * @author Everel
 * 
 */
public class Environment {

	/**
	 * Loads a new environment
	 * 
	 * @param desc
	 */
	public static void load(final ServerDescription desc) {
		
		LinkedList<Library> libs = new LinkedList<>();
		libs.add(new JavaFX());
		
		for(Library lib : libs) {
			if(!lib.hasJar()) {
				Core.verbose("Downloading " + lib.getLibraryName() + "...");
				VerboseLoader.setState("Downloading " + lib.getLibraryName() + "...");
				WebUtil.downloadFile(lib.getDownloadLink(), lib.getJarFile(), VerboseLoader.get());
				Core.verbose("Downloaded " + lib.getLibraryName() + ".");
			}
			Core.verbose("Initializing " + lib.getLibraryName());
			lib.init();
		}
		
		
		Core.verbose("Loading server: " + desc.toString());

		ServerParser.SERVER_CACHE.get(desc).run();
		
	}
}

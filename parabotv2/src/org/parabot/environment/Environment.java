package org.parabot.environment;

import java.util.LinkedList;

import org.parabot.core.Core;
import org.parabot.core.desc.ServerDescription;
import org.parabot.core.lib.Library;
import org.parabot.core.lib.javafx.JavaFX;
import org.parabot.core.parsers.servers.ServerParser;
import org.parabot.core.ui.components.MainScreenComponent;
import org.parabot.environment.api.utils.WebUtil;


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
	 * @param url
	 */
	public static void load(final ServerDescription desc) {
		
		LinkedList<Library> libs = new LinkedList<Library>();
		libs.add(new JavaFX());
		
		for(Library lib : libs) {
			if(!lib.hasJar()) {
				Core.verbose("Downloading " + lib.getLibraryName() + "...");
				MainScreenComponent.setState("Downloading " + lib.getLibraryName() + "...");
				WebUtil.downloadFile(lib.getDownloadLink(), lib.getJarFile(), MainScreenComponent.get());
				Core.verbose("Downloaded " + lib.getLibraryName() + ".");
			}
			Core.verbose("Initializing " + lib.getLibraryName());
			lib.init();
		}
		
		
		Core.verbose("Loading server: " + desc.toString());

		ServerParser.SERVER_CACHE.get(desc).run();
		
	}
}

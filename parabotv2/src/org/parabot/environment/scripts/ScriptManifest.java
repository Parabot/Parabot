package org.parabot.environment.scripts;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A script manifest
 * @author Clisprail
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

	String author();

	String name();
	
	Category category();

	double version();

	String description();
	
	String[] servers();

}
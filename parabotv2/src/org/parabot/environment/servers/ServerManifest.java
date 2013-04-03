package org.parabot.environment.servers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A server manifest
 * @author Clisprail
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerManifest {

	String author();

	String name();

	double version();

	Type type();

}

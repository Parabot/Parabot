package org.parabot.environment.scripts;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A script manifest, holds all script data
 *
 * @author Everel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

    String author();

    String name();

    Category category();

    double version();

    String description();

    String[] servers();

    boolean vip() default false;

    boolean premium() default false;

}
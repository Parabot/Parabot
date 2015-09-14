package org.parabot.core.ui.utils;

import org.parabot.core.ui.images.Images;
import org.parabot.environment.OperatingSystem;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * Holds various swing util based methods
 * 
 * @author Dane
 *
 */
public class SwingUtil {

	/**
	 * Packs, centers, and shows the frame.
	 * 
	 * @param f
	 */
	public static void finalize(JFrame f) {
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public static void setParabotIcons(JFrame f) {
		f.setIconImage(Images.getResource("/storage/images/icon.png"));
		
		if(OperatingSystem.getOS() == OperatingSystem.MAC) {
	        /** Adds the dock icon to mac users */
	        try {
	            Class<?> util = Class.forName("com.apple.eawt.Application");
	            Object application = util.getMethod("getApplication", new Class[] { }).invoke(null);
	            Method setDockIconImage = util.getMethod("setDockIconImage", new Class[] { Image.class });
	            setDockIconImage.invoke(application, Images.getResource("/storage/images/icon.png"));
	        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
	        } catch (Throwable t) {
	        	t.printStackTrace();
	        }
		}
	}

}

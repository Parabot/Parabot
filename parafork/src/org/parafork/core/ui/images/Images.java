package org.parafork.core.ui.images;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * 
 * Caches and loads images from resource
 * 
 * @author Everel
 *
 */
public final class Images {
	private static final HashMap<String, BufferedImage> imageCache = new HashMap<String, BufferedImage>();
	
	public static BufferedImage getResource(final String resource) {
		if(imageCache.containsKey(resource)) {
			return imageCache.get(resource);
		}
		try {
			final BufferedImage img = ImageIO.read(Images.class.getResourceAsStream(resource));
			imageCache.put(resource, img);
			return img;
		} catch (Throwable t) {
			throw new RuntimeException("Failed to load image from resource. " + t.getMessage());
		}
	}

}

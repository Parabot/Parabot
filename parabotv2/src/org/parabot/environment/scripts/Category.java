
package org.parabot.environment.scripts;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.parabot.core.ui.images.Images;

/**
 * Holds script categories
 * 
 * @author Dane
 */
public enum Category
{

	AGILITY, COMBAT, COOKING, CRAFTING, CONSTRUCTION, FARMING, FIREMAKING, FISHING, FLETCHING, HERBLORE, HUNTER, MAGIC, MINING, OTHER, PRAYER, RUNECRAFTING, SLAYER, SMITHING, THIEVING, UTILITY, WOODCUTTING;

	/**
	 * Gets image belonging to this category
	 * 
	 * @return icon
	 */
	public BufferedImage getIcon()
	{
		return Category.getIcon( this.name().toLowerCase() );
	}


	/**
	 * Gets category icon image from filename
	 * 
	 * @param s
	 * @return icon
	 */
	public static BufferedImage getIcon( String s )
	{
		if( images.get( s ) == null ) {
			images.put( s, Images.getResource( "/org/parabot/core/ui/images/category/" + s + ".png" ) );
		}
		return images.get( s );
	}


	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append( name().charAt( 0 ) );
		b.append( name().toLowerCase().substring( 1 ) );
		return new String( b );
	}

	/**
	 * Cache
	 */
	private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	static {
		images.put( "script", Images.getResource( "/org/parabot/core/ui/images/category/script.png" ) );
	}

}

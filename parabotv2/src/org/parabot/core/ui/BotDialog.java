
package org.parabot.core.ui;

import org.parabot.core.ui.components.PaintComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Everel
 */
public class BotDialog extends JDialog
{

	private static final long serialVersionUID = 521800552287194673L;
	private static BotDialog instance;


	private BotDialog( BotUI botUI )
	{
		super( botUI );

		botUI.setDialog( this );
		setUndecorated( true );
		getRootPane().setOpaque( false );
		setBackground( new Color( 0, 0, 0, 0 ) );
		setFocusableWindowState( true );
		setPreferredSize( botUI.getSize() );
		setSize( botUI.getSize() );
		setVisible( true );
		setContentPane( PaintComponent.getInstance( botUI.getSize() ) );
		botUI.setVisible( true );

	}


	public void setDimensions( Dimension dimension )
	{
		setUndecorated( true );
		getRootPane().setOpaque( false );
		setBackground( new Color( 0, 0, 0, 0 ) );
		setFocusableWindowState( true );
		setPreferredSize( dimension );
		setSize( dimension );
		setVisible( true );
		setContentPane( PaintComponent.getInstance() );
		PaintComponent.getInstance().setDimensions( dimension );
	}


	public static BotDialog getInstance( BotUI botUI )
	{
		return instance == null ? instance = new BotDialog( botUI ): instance;
	}


	public static BotDialog getInstance()
	{
		return getInstance( null );
	}

}

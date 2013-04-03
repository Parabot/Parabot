package org.parabot.core.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.parabot.core.Context;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.images.Images;

/**
 * Bot toolbar
 * 
 * @author Clisprail
 *
 */
public class BotToolbar extends JToolBar {
	private static final long serialVersionUID = 5373484845104212180L;
	private static BotToolbar instance = null;
	private JButton tab = null;
	private static Map<TabButton, Context> environments = new HashMap<TabButton, Context>();
	
	public BotToolbar() {
		setFloatable(false);
		final JButton run = new JButton();
		final JButton pause = new JButton();
		tab = new JButton();
		tab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ServerSelector.getInstance().setVisible(true);
			}
			
		});
		run.setFocusable(false);
		pause.setFocusable(false);
		tab.setFocusable(false);
		try {
			run.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/run.png")));
			pause.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/pause.png")));
			tab.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/add.png")));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		//add(tab);
		add(Box.createHorizontalGlue());
		add(run);
		add(pause);
	}
	
	public static BotToolbar getInstance() {
		return instance == null ? instance = new BotToolbar() : instance;
	}
	
	public void addTab(final Context context, final String name) {
		TabButton b = new TabButton(name);
		b.setActive(true);
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final TabButton tabButton = (TabButton) e.getSource();
				final Context context = environments.get(tabButton);
				for(final TabButton button : environments.keySet()) {
					button.setActive(false);
				}
				tabButton.setActive(true);
				if(!context.appletSet()) {
					return;
				}
				GamePanel.getInstance().setContext(context);
			}
			
		});
		//add(b, getComponentIndex(tab));
		add(b, 0);
		environments.put(b, context);
	}
	
	
	/**
	 * Tab button
	 * @author Clisprail
	 *
	 */
	private final class TabButton extends JButton {
		private static final long serialVersionUID = 1L;

		public TabButton(final String name) {
			super(name);
			setFocusable(false);
			setHorizontalTextPosition(SwingConstants.LEFT);
			setIcon(getBlackClose());
			final Component c = this;
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(final MouseEvent e) {
					if (e.getX() > getWidth() - getIcon().getIconWidth()
							&& e.getX() < getWidth() - getIconTextGap()) {
						setIcon(getRedClose());
					} else {
						setIcon(getBlackClose());
					}
				}
			});
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					@SuppressWarnings("unused")
					final int n = getComponentIndex(c);
					if (e.getX() > getWidth() - getIcon().getIconWidth()
							&& e.getX() < getWidth() - getIconTextGap()) {
						// close
					} else {
						// enable
					}
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					setIcon(getBlackClose());
				}
			});
		}
		
		private final ImageIcon getBlackClose() {
			try {
				return new ImageIcon(Images.getResource("/org/parabot/core/ui/images/close.png"));
			} catch (Throwable t) {
				throw new RuntimeException("Unable to load icon image: " + t.getMessage());
			}
		}
		
		private final ImageIcon getRedClose() {
			try {
				return new ImageIcon(Images.getResource("/org/parabot/core/ui/images/close_red.png"));
			} catch (Throwable t) {
				throw new RuntimeException("Unable to load icon image: " + t.getMessage());
			}
		}
		
		public void setActive(final boolean active) {
			setFont(getFont().deriveFont(active ? Font.BOLD : Font.PLAIN));
		}
	}

}

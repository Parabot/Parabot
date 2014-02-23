package org.parabot.core.ui.components;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.parabot.core.Context;
import org.parabot.core.ui.ScriptSelector;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.images.Images;
import org.parabot.environment.scripts.Script;

/**
 * Bot toolbar
 * 
 * @author Everel
 *
 */
public class BotToolbar extends JToolBar {
	private static final long serialVersionUID = 5373484845104212180L;
	private static BotToolbar instance;
	
	private JButton tab;
	private final JButton run;
	private final JButton stop;
	private boolean runScript;
	private boolean pauseScript;
	
	public BotToolbar() {
		this.run = new JButton();
		this.stop = new JButton();
		setFloatable(false);
		tab = new JButton();
		tab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ServerSelector.getInstance().setVisible(true);
			}
			
		});
		run.setFocusable(false);
		stop.setFocusable(false);
		tab.setFocusable(false);
		try {
			run.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/run.png")));
			stop.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/stop.png")));
			tab.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/add.png")));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		run.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				runButtonClicked();
			}
			
		});
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopButtonClicked();
			}
			
		});
		//add(tab);
		add(Box.createHorizontalGlue());
		add(run);
		add(stop);
	}
	
	protected void stopButtonClicked() {
		if(!runScript){
			// obviously do nothing ;d
			return;
		}
		setScriptState(Script.STATE_STOPPED);
	}

	protected void runButtonClicked() {
		if(runScript && pauseScript) {
			// unpause
			this.pauseScript = false;
			scriptRunning();
			setScriptState(Script.STATE_RUNNING);
			return;
		} else if(runScript) {
			// pause
			this.pauseScript = true;
			scriptStopped();
			setScriptState(Script.STATE_PAUSE);
		} else {
			new ScriptSelector().setVisible(true);
		}
	}
	
	private void setScriptState(int state) {
		Context.getInstance().getRunningScript().setState(state);
	}
	

	public void toggleRun() {
		runScript = !runScript;
		if(runScript) {
			scriptRunning();
		} else {
			scriptStopped();
		}
	}
	
	private void scriptRunning() {
		// sets pause icon
		run.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/pause.png")));
	}
	
	private void scriptStopped() {
		// sets start icon
		run.setIcon(new ImageIcon(Images.getResource("/org/parabot/core/ui/images/run.png")));
	}
	
	public static BotToolbar getInstance() {
		return instance == null ? instance = new BotToolbar() : instance;
	}
	
	public void addTab(final Context context, final String name) {
		TabButton b = new TabButton(name);
		b.setActive(true);
		add(b, 0);
		repaint();
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

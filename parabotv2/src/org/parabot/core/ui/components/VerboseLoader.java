package org.parabot.core.ui.components;

import org.parabot.core.Core;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.forum.AccountManagerAccess;
import org.parabot.core.io.ProgressListener;
import org.parabot.core.ui.ServerSelector;
import org.parabot.core.ui.images.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * An informative JPanel which tells the user what bot is doing
 * 
 * @author Everel
 *
 */
public class VerboseLoader extends JPanel implements ProgressListener {
	private static final long serialVersionUID = 7412412644921803896L;
	private static VerboseLoader current;
	private static String state = "Initializing loader...";
	
	public static final int STATE_AUTHENTICATION = 0;
	public static final int STATE_LOADING = 1;
	public static final int STATE_SERVER_SELECT = 2;
	private int currentState;
	
	private static AccountManager manager;
	
	private FontMetrics fontMetrics;
	private BufferedImage background;
	private ProgressBar progressBar;
	private JPanel loginPanel;
	
	public static final AccountManagerAccess MANAGER_FETCHER = new AccountManagerAccess() {

		@Override
		public final void setManager(AccountManager manager) {
			VerboseLoader.manager = manager;
		}

	};

	private VerboseLoader(String username, String password) {
		if(current != null) {
			throw new IllegalStateException("MainScreenComponent already made.");
		}
		current = this;
		this.background = Images.getResource("/org/parabot/core/ui/images/background.png");
		this.progressBar = new ProgressBar(400, 20);
		setLayout(new GridBagLayout());
		setSize(775, 510);
		setPreferredSize(new Dimension(775, 510));
		setDoubleBuffered(true);
		setOpaque(false);
		
		if(username != null && password != null) {
			if(Core.inDebugMode() || manager.login(username, password)) {
				currentState = STATE_SERVER_SELECT;
			}
		}
		
		if(currentState == STATE_AUTHENTICATION) {
			addLoginPanel();
		} else if(currentState == STATE_SERVER_SELECT) {
			addServerPanel();
		}
	}
	
	public void addServerPanel() {
		JPanel servers = ServerSelector.getInstance();
		add(servers, new GridBagConstraints());
	}
	
	public void addLoginPanel() {
		loginPanel = new JPanel();
		loginPanel.setOpaque(false);
		
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		
		Font labelFont = new Font("Times New Roman", Font.PLAIN, 11);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(labelFont);
		usernameLabel.setAlignmentX(Box.CENTER_ALIGNMENT);
		usernameLabel.setForeground(Color.white);
		
		final JTextField userInput = new JTextField(25);
		userInput.setFont(labelFont);
		userInput.setAlignmentX(Box.CENTER_ALIGNMENT);
		userInput.setMaximumSize(userInput.getPreferredSize());
		
		final JButton login = new JButton("Login");
		
		final JTextField passInput = new JPasswordField(25);
		passInput.setAlignmentX(Box.CENTER_ALIGNMENT);
		passInput.setMaximumSize(userInput.getPreferredSize());
        passInput.setPreferredSize(new Dimension(userInput.getWidth(), 20));
        passInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				login.doClick();
			}
        	
        });
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(labelFont);
		passwordLabel.setAlignmentX(Box.CENTER_ALIGNMENT);
		passwordLabel.setForeground(Color.white);
		
		
		login.setAlignmentX(Box.CENTER_ALIGNMENT);
		login.setOpaque(false);
		
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(manager.login(userInput.getText(), passInput.getText())) {
					switchState(STATE_SERVER_SELECT);
				}
			}
			
		});
		
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(usernameLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(userInput);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(passwordLabel);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(passInput);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		loginPanel.add(login);
		loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		
		add(loginPanel, new GridBagConstraints());
	}

	public void switchState(int state) {
		removeAll();
		if(state == STATE_AUTHENTICATION) {
			addLoginPanel();
		} else if(state == STATE_SERVER_SELECT) {
			addServerPanel();
		}
		this.currentState = state;
		revalidate();
	}

	/**
	 * Paints on this panel
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
	
		
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.drawImage(background, 0, 0, null);
		
		if (fontMetrics == null) {
			fontMetrics = g.getFontMetrics();
		}
		
		if(currentState == STATE_AUTHENTICATION) {
			g.setColor(new Color(74, 74, 72, 100));
			g.fillRect(loginPanel.getX() - 10, loginPanel.getY(), loginPanel.getWidth() + 20, loginPanel.getHeight());
			g.setColor(Color.black);
			g.drawRect(loginPanel.getX() - 10, loginPanel.getY(), loginPanel.getWidth() + 20, loginPanel.getHeight());
		}
		
		g.setColor(Color.white);
		
		if(currentState == STATE_LOADING) {
			progressBar.draw(g, (getWidth() / 2) - 200, 220);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			int x = (getWidth() / 2) - (fontMetrics.stringWidth(state) / 2);
			g.drawString(state, x, 200);
		}
		
		
		g.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		final String version = "v2.1";
		g.drawString(version,
				getWidth() - g.getFontMetrics().stringWidth(version) - 10,
				getHeight() - 12);
	}

	/**
	 * Gets instance of this panel
	 * @return instance of this panel
	 */
	public static VerboseLoader get(String username, String password) {
		return current == null ? new VerboseLoader(username, password) : current;
	}
	
	/**
	 * Gets instance of this panel
	 * @return instance of this panel
	 */
	public static VerboseLoader get() {
		return current == null ? new VerboseLoader(null, null) : current;
	}
	

	/**
	 * Updates the status message and repaints the panel
	 * @param message
	 */
	public static void setState(final String message) {
		state = message;
		current.repaint();
	}

	@Override
	public void onProgressUpdate(double value) {
		progressBar.setValue(value);
		this.repaint();
	}

	@Override
	public void updateDownloadSpeed(double mbPerSecond) {
		progressBar.setText(String.format("(%.2fMB/s)", mbPerSecond));
	}

}

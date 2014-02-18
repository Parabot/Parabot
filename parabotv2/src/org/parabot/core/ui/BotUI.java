package org.parabot.core.ui;

import org.parabot.core.Configuration;
import org.parabot.core.ui.components.BotToolbar;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.components.LogArea;
import org.parabot.core.ui.components.VerboseLoader;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * The bot user interface
 * 
 * @author Dane, Everel
 * 
 */
public class BotUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = -2126184292879805519L;
	private static BotUI instance;

	public static BotUI getInstance() {
		return instance == null ? instance = new BotUI() : instance;
	}

	private BotUI() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		this.setTitle("Parabot");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(Images.getResource("/org/parabot/core/ui/images/icon.png"));
		this.setLayout(new BorderLayout());

		int iToolbarHeight = 24;
		int iGameHeight = 503;
		int iLogHeight = 128;

		JPanel panel = new JPanel();
		panel.setLocation(0, 0);
		panel.setPreferredSize(new Dimension(765, iToolbarHeight + iGameHeight + iLogHeight));

		JMenuBar menubar = new JMenuBar();

		JMenu mnuFile = new JMenu("File");
		JMenuItem proxy = new JMenuItem("Network");
		JMenuItem exit = new JMenuItem("Exit");
		proxy.addActionListener(this);
		exit.addActionListener(this);

		mnuFile.add(proxy);
		mnuFile.add(exit);
		menubar.add(mnuFile);

		this.setJMenuBar(menubar);

		int x = 0;
		int y = 0;

		JToolBar toolbar = BotToolbar.getInstance();
		toolbar.setPreferredSize(new Dimension(765, iToolbarHeight));
		toolbar.setLocation(x, y);

		y += iToolbarHeight;

		GamePanel gamePanel = GamePanel.getInstance();
		gamePanel.setPreferredSize(new Dimension(765, iGameHeight));
		toolbar.setLocation(x, y);

		y += iGameHeight;

		JScrollPane scrlConsole = LogArea.getInstance();
		scrlConsole.setPreferredSize(new Dimension(765, iLogHeight - 15));
		toolbar.setLocation(x, y);

		panel.add(toolbar);
		panel.add(gamePanel);
		gamePanel.add(VerboseLoader.get());
		panel.add(scrlConsole);

		this.add(panel, BorderLayout.CENTER);

		SwingUtil.finalize(this);

		LogArea.log("parabot " + Configuration.BOT_VERSION +" started");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command) {
			case "Exit":
				System.exit(0);
				break;
            case "Network":
                NetworkUI UI = new NetworkUI();
                UI.frame.setVisible(true);
                break;
			default:
				System.out.println("Invalid command: " + command);
		}
	}
}

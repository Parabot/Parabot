package org.parabot.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import org.parabot.core.ui.components.BotToolbar;
import org.parabot.core.ui.components.GamePanel;
import org.parabot.core.ui.components.LogArea;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.AwtUtil;
import org.parabot.core.ui.utils.SwingUtil;

/**
 * Bot frame
 * 
 * @author Clisprail
 * 
 */
public class BotUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static BotUI instance = null;
	private JMenuBar bar = null;

	public static BotUI getInstance() {
		return instance == null ? instance = new BotUI() : instance;
	}

	public BotUI() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		setDefaultLookAndFeelDecorated(false);
		bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem screenshot = new JMenuItem("Screenshot");
		screenshot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					@Override
					public void run() {

					}
				}).start();
			}
		});
		file.add(screenshot);
		bar.add(file);

		JScrollPane textPane = LogArea.getInstance();
		GroupLayout layout = new GroupLayout(getContentPane());
		JToolBar tool = BotToolbar.getInstance();
		GamePanel pane = GamePanel.getInstance();
		pane.addLoader();
		getContentPane().setLayout(layout);
		setJMenuBar(bar);
		setTitle("Parabot");
		setIconImage(Images.getResource("/org/parabot/core/ui/images/icon.png"));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		SwingUtil.center(this);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))).addComponent(tool, 768, 768, 768).addComponent(pane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(textPane, 768, 768, 768));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(tool, 30, 30, 30).addComponent(pane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(textPane, 100, 100, 100).addContainerGap(58, Short.MAX_VALUE)));
		LogArea.log("Welcome to Parabot v2");
	}

	public JMenuBar getBar() {
		return bar;
	}

}

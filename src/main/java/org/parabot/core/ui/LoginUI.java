package org.parabot.core.ui;

import org.parabot.core.Configuration;
import org.parabot.core.Core;
import org.parabot.core.forum.AccountManager;
import org.parabot.core.ui.images.Images;
import org.parabot.core.ui.utils.SwingUtil;
import org.parabot.core.ui.utils.UILog;
import org.parabot.environment.input.Keyboard;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Users must login with their parabot account through this LoginUI class
 *
 * @author Everel
 */
public class LoginUI extends JFrame {
    private static final long serialVersionUID = 2032832552863466297L;
    private static LoginUI instance;
    private static AccountManager manager;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton cmdLogin;
    private JButton cmdRegister;

    public LoginUI(String username, String password) {
        instance = this;
        attempt(username, password);
    }

    public LoginUI() {
        instance = this;

        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        SwingUtil.setParabotIcons(this);

        int w = 250;
        int x = 8;
        int y = 64;

        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 2258761648532714183L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ((Graphics2D) g).setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.drawImage(Images
                                .getResource("/storage/images/para.png"),
                        0, 8, 250, 45, null);
            }
        };
        panel.setLayout(null);

        txtUsername = new JTextField("");
        txtUsername.setBounds(x, y, w - (x << 1), 26);
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == Keyboard.ENTER_KEYCODE) {
                    txtPassword.requestFocus();
                }
            }
        });

        y += 30;

        txtPassword = new JPasswordField("");
        txtPassword.setBounds(x, y, w - (x << 1), 26);
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == Keyboard.ENTER_KEYCODE) {
                    attemptLogin();
                }
            }
        });

        y += 30;

        cmdLogin = new JButton("Login");
        cmdLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                attemptLogin();
            }
        });
        cmdLogin.setBounds(x, y, (w - (x << 1)) / 2 - 8, 24);

        cmdRegister = new JButton("Register");
        cmdRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                URI uri = URI
                        .create(Configuration.REGISTRATION_PAGE);
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Connection Error",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });
        cmdRegister.setBounds(x + (w - (x << 1)) / 2 + 8, y,
                (w - (x << 1)) / 2 - 8, 24);

        panel.add(txtUsername);
        panel.add(txtPassword);
        panel.add(cmdLogin);
        panel.add(cmdRegister);

        this.add(panel, BorderLayout.CENTER);

        this.setVisible(true);
        this.requestFocus();

        this.setSize(255, 182);
        this.setLocationRelativeTo(null);

    }

    public void attemptLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.length() > 0 && password.length() > 0) {
            if (manager.login(username, password, false)) {
                Core.verbose("Logged in.");
                instance.dispose();
                Core.verbose("Running server selector.");
                ServerSelector.getInstance();
            } else {
                Core.verbose("Failed to log in.");
                UILog.log("Error", "Incorrect username or password. Have you tried logging into http://bdn.parabot.org/account/", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void attempt(String user, String pass) {
        Core.verbose("Logging in...");
        if (manager.login(user, pass, false)) {
            Core.verbose("Logged in.");
            instance.dispose();
            Core.verbose("Running server selector.");
            ServerSelector.getInstance();
        } else {
            Core.verbose("Failed to log in.");
            UILog.log("Error", "Incorrect username or password. Have you tried logging into http://bdn.parabot.org/account/", JOptionPane.ERROR_MESSAGE);
        }

    }

}

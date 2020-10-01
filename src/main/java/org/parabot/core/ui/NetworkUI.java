package org.parabot.core.ui;

import org.parabot.core.network.NetworkInterface;
import org.parabot.core.network.proxy.ProxySocket;
import org.parabot.core.network.proxy.ProxyType;
import org.parabot.core.ui.utils.UILog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NetworkUI extends JFrame implements KeyListener, ActionListener,
        DocumentListener {

    private static final long serialVersionUID = 1L;

    private static NetworkUI instance;

    private JComboBox<ProxyType> proxyType;
    private JTextField proxyHost;
    private IntTextField proxyPort;
    private JButton submitButton;

    private JList<String>[] macList;
    private JScrollPane[] macScrollList;

    private JCheckBox authCheckBox;
    private JTextField authUsername;
    private JPasswordField authPassword;
    private JButton randomize;

    private NetworkUI() {
        initGUI();
    }

    public static NetworkUI getInstance() {
        return instance == null ? instance = new NetworkUI() : instance;
    }

    @Override
    public void setVisible(boolean b) {
        BotUI.getInstance().setEnabled(!b);
        if (ProxySocket.getProxyAddress() != null) {
            proxyHost.setText(ProxySocket.getProxyAddress().getHostName());
        }
        proxyPort.setText("" + ProxySocket.getProxyPort());
        proxyType.setSelectedItem(ProxySocket.getProxyType());
        authCheckBox.setSelected(ProxySocket.auth);
        setLocationRelativeTo(BotUI.getInstance());
        super.setVisible(b);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object source = e.getSource();
        if (source == proxyPort || source == proxyHost) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                actionPerformed(null);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {

    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        if (proxyPort.isValid()) {
            proxyPort.setText("" + proxyPort.getValue());
        }
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        insertUpdate(arg0);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        boolean b = false;

        if (arg0.getSource() == proxyType) {
            Object o = proxyType.getSelectedItem();
            authCheckBox.setEnabled(o == ProxyType.SOCKS5);
            proxyHost.setEnabled(o != ProxyType.NONE);
            proxyPort.setEnabled(o != ProxyType.NONE);
            b = true;
        }

        if (b || arg0.getSource() == authCheckBox) {
            b = authCheckBox.isSelected() && authCheckBox.isEnabled();
            ProxySocket.auth = b;
            authUsername.setEnabled(b);
            authPassword.setEnabled(b);
            return;
        }

        if (proxyType.getSelectedItem() != ProxyType.NONE) {
            if (proxyPort.getText().equals("")
                    || proxyHost.getText().equals("")) {
                UILog.log("Error", "Please supply proxy information!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String username = authUsername.getText();
        char[] password = authPassword.getPassword();

        ProxySocket
                .setLogin(username, password);

        byte[] mac = new byte[macList.length];
        for (int i = 0; i < mac.length; i++) {
            mac[i] = (byte) Short.parseShort(
                    macList[i].getSelectedValue(), 16);
        }
        NetworkInterface.setMac(mac);

        try {
            if (ProxySocket.getConnectionCount() > 0) {
                try {
                    System.out.println("Closing Existing Connections...");
                    ProxySocket.closeConnections();
                } catch (Exception e) {

                }
            }
            ProxyType type = (ProxyType) proxyType.getSelectedItem();
            String host = proxyHost.getText();
            int port = proxyPort.getValue();

            ProxySocket.setProxy(type, host, port);
            UILog.log("Info", "Network settings have been set!");
        } catch (Exception e) {
            UILog.log("Error",
                    "Unable to set proxy info!\n\nReason:" + e.getMessage());
            e.printStackTrace();
        }
        setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void initGUI() {
        proxyType = new JComboBox<ProxyType>(ProxyType.values());
        proxyType.setSelectedItem(ProxySocket.getProxyType());

        proxyType.addActionListener(this);

        proxyHost = new JTextField();
        proxyHost.addKeyListener(this);

        proxyPort = new IntTextField(80, 5);
        proxyPort.setColumns(5);
        proxyPort.addKeyListener(this);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        byte[] mac = new byte[6];
        mac = NetworkInterface.mac;
        macList = new JList[mac.length];
        macScrollList = new JScrollPane[mac.length];
        for (int i = 0; i < mac.length; i++) {
            int value = mac[i] & 0xFF;
            macList[i] = createMacList();
            macList[i].setSelectedIndex(value);
            macScrollList[i] = new JScrollPane(macList[i]);
            macList[i].ensureIndexIsVisible(value > 0 ? value - 1 : value);
        }

        randomize = new JButton("Randomize MAC");
        randomize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                byte[] macAddr = new byte[6];
                rand.nextBytes(macAddr);
                macAddr[0] = (byte) (macAddr[0] & (byte) 254);
                for (int i = 0; i < macAddr.length; i++) {
                    int value = macAddr[i] & 0xFF;
                    macList[i].setSelectedIndex(value);
                    macList[i].ensureIndexIsVisible(value > 0 ? value - 1 : value);
                }
            }
        });

        authCheckBox = new JCheckBox("Auth");
        authCheckBox.setSelected(ProxySocket.auth);
        authCheckBox.addActionListener(this);

        authUsername = new JTextField();
        authPassword = new JPasswordField();

        authUsername.setEnabled(authCheckBox.isSelected());
        authPassword.setEnabled(authCheckBox.isSelected());

        JPanel p = createPanelUI();
        add(p);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setTitle("Network Settings");
    }

    private JPanel createPanelUI() {
        JPanel ret = new JPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.LINE_AXIS));
        Box main = Box.createVerticalBox();

        Box type = Box.createHorizontalBox();
        type.add(new JLabel("Proxy Type: "));
        type.add(proxyType);

        Box host = Box.createHorizontalBox();
        host.add(new JLabel("Proxy Host: "));
        host.add(proxyHost);

        Box port = Box.createHorizontalBox();
        port.add(new JLabel("Proxy Port: "));
        port.add(proxyPort);

        Box auth = Box.createHorizontalBox();
        auth.add(authCheckBox);

        auth.add(Box.createHorizontalStrut(3));

        auth.add(new JLabel("User:"));
        auth.add(authUsername);

        auth.add(Box.createHorizontalStrut(3));

        auth.add(new JLabel("Pass:"));
        auth.add(authPassword);

        Box macBox = Box.createHorizontalBox();
        macBox.add(new JLabel("MAC:"));
        for (int i = 0; i < macList.length; i++) {
            macBox.add(new JScrollPane(macList[i]));
            macBox.add(Box.createHorizontalStrut(5));
        }

        Box submit = Box.createHorizontalBox();
        main.add(Box.createVerticalStrut(5));
        submit.add(randomize);
        submit.add(submitButton);

        main.add(type);

        main.add(Box.createVerticalStrut(5));
        main.add(host);

        main.add(Box.createVerticalStrut(5));
        main.add(port);

        main.add(Box.createVerticalStrut(5));
        main.add(auth);

        main.add(Box.createVerticalStrut(5));
        main.add(macBox);

        main.add(Box.createVerticalStrut(5));
        main.add(submit);

        ret.add(main);
        ret.setBorder(new EmptyBorder(10, 10, 10, 10));
        return ret;
    }

    private JList<String> createMacList() {
        String[] hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            hexStrings[i] = String.format("%02X", i);
        }
        JList<String> ret = new JList<String>(hexStrings);
        ret.setVisibleRowCount(3);
        ret.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return ret;
    }

    class IntTextField extends JTextField {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public IntTextField(int defval, int size) {
            super("" + defval, size);
        }

        public boolean isValid() {
            try {
                int i = Integer.parseInt(getText());
                return i > 0 && i <= 25565;
            } catch (Exception e) {
                return false;
            }
        }

        public int getValue() {
            try {
                return Integer.parseInt(getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        protected Document createDefaultModel() {
            return new IntTextDocument();
        }

        class IntTextDocument extends PlainDocument {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                if (str == null) {
                    return;
                }
                String oldString = getText(0, getLength());
                String newString = oldString.substring(0, offs) + str
                        + oldString.substring(offs);
                try {
                    Integer.parseInt(newString.replace("-", "") + "0");
                    super.insertString(offs, str, a);
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
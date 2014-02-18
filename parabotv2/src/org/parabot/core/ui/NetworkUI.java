package org.parabot.core.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class NetworkUI {

    private JFrame frame;
    private JTextField proxyHostField;
    private JTextField proxyPortField;
    private HashMap<String, Integer> socksVersions = new HashMap<>();

    public NetworkUI() {
        initialize();
    }

    public static void main(String[] args){
        NetworkUI window = new NetworkUI();
        window.frame.setVisible(true);
    }

    private void initialize() {
        socksVersions.put("Socks 4", 4);
        socksVersions.put("Socks 5", 5);
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(6, 6, 438, 233);
        frame.getContentPane().add(tabbedPane);

        JPanel proxy = new JPanel();
        tabbedPane.addTab("Proxy", null, proxy, null);
        proxy.setLayout(null);

        JLabel socksOption = new JLabel("Socks version");
        socksOption.setBounds(6, 6, 87, 16);
        proxy.add(socksOption);

        final JComboBox socksOptions = new JComboBox();
        for (String key : socksVersions.keySet()) {
            socksOptions.addItem(key);
        }
        socksOptions.setBounds(105, 2, 127, 27);
        proxy.add(socksOptions);

        JLabel proxyHost = new JLabel("Proxy host");
        proxyHost.setBounds(6, 66, 87, 16);
        proxy.add(proxyHost);

        proxyHostField = new JTextField();
        proxyHostField.setBounds(105, 60, 127, 28);
        proxy.add(proxyHostField);
        proxyHostField.setColumns(10);

        JLabel proxyPort = new JLabel("Proxy port");
        proxyPort.setBounds(6, 126, 87, 16);
        proxy.add(proxyPort);

        proxyPortField = new JTextField();
        proxyPortField.setBounds(105, 120, 127, 28);
        proxy.add(proxyPortField);
        proxyPortField.setColumns(10);

        JPanel macAddress = new JPanel();
        tabbedPane.addTab("Mac address", null, macAddress, null);
        macAddress.setLayout(null);

        JLabel enableSpoofer = new JLabel("Enable spoofer");
        enableSpoofer.setBounds(6, 80, 99, 16);
        macAddress.add(enableSpoofer);

        final ButtonGroup bg = new ButtonGroup();
        JRadioButton spooferYes = new JRadioButton("Yes");
        spooferYes.setBounds(117, 76, 59, 23);
        macAddress.add(spooferYes);

        JRadioButton spooferNo = new JRadioButton("No");
        spooferNo.setBounds(188, 76, 59, 23);
        macAddress.add(spooferNo);
        spooferNo.setSelected(true);

        bg.add(spooferYes);
        bg.add(spooferNo);

        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(socksVersions.get(socksOptions.getSelectedItem()));
            }
        });
        submit.setBounds(327, 243, 117, 29);
        frame.getContentPane().add(submit);
    }
}

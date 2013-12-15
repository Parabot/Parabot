package org.parabot.core.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.matt123337.proxy.ProxySocket;
import org.matt123337.proxy.ProxyType;
import org.parabot.core.ui.utils.UILog;

public class ProxyGUI extends JFrame implements KeyListener, ActionListener,DocumentListener {

	private static ProxyGUI instance;
	
	private JComboBox<ProxyType> proxyType;
	private JTextField proxyHost;
	private IntTextField proxyPort;
	private JButton submitButton;

	private ProxyGUI() {
		initGUI();
	}
	
	public static ProxyGUI getInstance(){
		return instance == null ? instance = new ProxyGUI() : instance;
	}
	
	@Override
	public void setVisible(boolean b){
		BotUI.getInstance().setEnabled(!b);
		if (ProxySocket.getProxyAddress() != null)
			proxyHost.setText(ProxySocket.getProxyAddress().getHostName());
		proxyPort.setText("" + ProxySocket.getProxyPort());
		proxyType.setSelectedItem(ProxySocket.getProxyType());
		super.setVisible(b);
	}

	private void initGUI() {
		proxyType = new JComboBox<ProxyType>(ProxyType.values());
		proxyType.setSelectedItem(ProxySocket.getProxyType());
		
		proxyHost = new JTextField();
		proxyHost.addKeyListener(this);
		
		proxyPort = new IntTextField(80,5);
		proxyPort.setColumns(5);
		proxyPort.addKeyListener(this);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		JPanel p = createPanelUI();
		add(p);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
	}

	private JPanel createPanelUI() {
		JPanel ret = new JPanel();
		ret.setLayout(new BoxLayout(ret, BoxLayout.LINE_AXIS));
		Box main = Box.createVerticalBox();

		Box type = Box.createHorizontalBox();
		type.add(Box.createHorizontalGlue());
		type.add(new JLabel("Proxy Type: "));
		type.add(proxyType);

		Box host = Box.createHorizontalBox();
		host.add(new JLabel("Proxy Host: "));
		host.add(proxyHost);

		Box port = Box.createHorizontalBox();
		port.add(new JLabel("Proxy Port: "));
		port.add(proxyPort);
		
		Box submit = Box.createHorizontalBox();
		submit.add(submitButton);

		main.add(type);

		main.add(Box.createVerticalStrut(5));
		main.add(host);

		main.add(Box.createVerticalStrut(5));
		main.add(port);
		
		main.add(Box.createVerticalStrut(5));
		main.add(submit);

		ret.add(main);
		ret.setBorder(new EmptyBorder(10, 10, 10, 10));
		return ret;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Object source = e.getSource();
		if(source == proxyPort || source == proxyHost){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
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
		if(proxyPort.isValid()){
			proxyPort.setText("" + proxyPort.getValue());
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		insertUpdate(arg0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
		ProxySocket.setProxy((ProxyType)proxyType.getSelectedItem(), proxyHost.getText(), proxyPort.getValue());
		UILog.log("Info", "If you're logged in, please logoutand login again\n for proxy changes to take effect!");
		}catch(Exception e){
			UILog.log("Error", "Unable to set proxy info!");
		}
		setVisible(false);
	}

	class IntTextField extends JTextField {
		public IntTextField(int defval, int size) {
			super("" + defval, size);
		}

		protected Document createDefaultModel() {
			return new IntTextDocument();
		}

		public boolean isValid() {
			try {
				int i = Integer.parseInt(getText());
				return i > 0 && i <=25565;
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

		class IntTextDocument extends PlainDocument {
			public void insertString(int offs, String str, AttributeSet a)
					throws BadLocationException {
				if (str == null)
					return;
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

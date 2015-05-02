package org.parabot.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.parabot.core.Context;
import org.parabot.core.ui.components.GamePanel;

/**
 * @author JKetelaar
 */
public class Logger extends JPanel {
	private static final long serialVersionUID = 1L;
	private static Logger instance;
    private final DefaultListModel<String> model;

    private Logger(){
        setLayout(new BorderLayout());
        JList<String> list = new JList<>();

        JScrollPane pane = new JScrollPane(list);
        add(pane, BorderLayout.CENTER);

        list.setCellRenderer(getRenderer());

        model = new DefaultListModel<>();
        list.setModel(model);
        setPreferredSize(new Dimension((int) GamePanel.getInstance().getPreferredSize().getWidth(), 150));
        model.addElement("Logger started");
        
        setVisible(false);
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
			private static final long serialVersionUID = -3589192791360628745L;

			@Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,Color.BLACK));
                return listCellRendererComponent;
            }
        };
    }

    public static Logger getInstance() {
        return instance == null ? instance = new Logger() : instance;
    }

    public static void addMessage(String message){
        instance.model.addElement(message);

        if (Context.getInstance().getUlirathaClient() != null) {
            Context.getInstance().getUlirathaClient().sendMessage(message);
        }
    }
}

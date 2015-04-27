package org.parabot.core.ui;

import org.parabot.core.ui.components.GamePanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author JKetelaar
 */
public class Logger extends JPanel {
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
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
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
    }
}

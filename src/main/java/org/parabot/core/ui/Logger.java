package org.parabot.core.ui;

import org.parabot.core.ui.components.GamePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 * @author JKetelaar
 */
public class Logger extends JPanel {
    private static final long serialVersionUID = 1L;
    private static Logger instance;
    private final DefaultListModel<String> model;
    private final JList<String> list;

    private boolean clearable;

    private Logger() {
        setLayout(new BorderLayout());
        list = new JList<>();

        JScrollPane pane = new JScrollPane(list);
        add(pane, BorderLayout.CENTER);

        JButton button = new JButton("Clear Logger");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLogger();
                addMessage("Logger initialised", false);
            }
        });
        add(button, BorderLayout.SOUTH);

        list.setCellRenderer(getRenderer());

        model = new DefaultListModel<>();
        list.setModel(model);
        setPreferredSize(new Dimension((int) GamePanel.getInstance().getPreferredSize().getWidth(), 150));
        model.addElement("Logger initialised");

        setVisible(false);
    }

    public static Logger getInstance() {
        return instance == null ? instance = new Logger() : instance;
    }

    /**
     * Logs a message in the logger ui
     *
     * @param message
     * @param uliratha Determines if this should be sent to the uliratha server
     */
    public static void addMessage(String message, boolean uliratha) {
        instance.model.addElement(message);

        if (uliratha) {
            // TODO: Implement latest Uliratha
        }

        int last = instance.list.getModel().getSize() - 1;
        if (last >= 0) {
            instance.list.ensureIndexIsVisible(last);
        }
        if (instance.list.getModel().getSize() > 100 && instance.list.getModel().getElementAt(0) != null) {
            instance.model.remove(0);
        }
    }

    /**
     * @param message
     */
    public static void addMessage(String message) {
        addMessage(message, true);
    }

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable() {
        this.clearable = true;
    }

    protected static void clearLogger() {
        instance.model.clear();
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer() {
            private static final long serialVersionUID = -3589192791360628745L;

            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
                return listCellRendererComponent;
            }
        };
    }
}

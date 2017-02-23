package org.parabot.core.ui;

import com.google.inject.Singleton;
import org.parabot.core.Core;
import org.parabot.core.ui.components.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author JKetelaar
 */
@Singleton
public class Logger extends JPanel {
    private static final long serialVersionUID = 1L;
    private final DefaultListModel<String> model;
    private final JList<String>            list;

    private boolean clearable;

    public Logger() {
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
        setPreferredSize(new Dimension((int) Core.getInjector().getInstance(GamePanel.class).getPreferredSize().getWidth(), 150));
        model.addElement("Logger initialised");

        setVisible(false);
    }

    private static Logger getInstance() {
        return Core.getInjector().getInstance(Logger.class);
    }

    /**
     * Logs a message in the logger ui
     *
     * @param message
     * @param uliratha Determines if this should be sent to the uliratha server
     */
    public static void addMessage(String message, boolean uliratha) {
        getInstance().model.addElement(message);

        if (uliratha) {
            // TODO: Implement latest Uliratha
        }

        int last = getInstance().list.getModel().getSize() - 1;
        if (last >= 0) {
            getInstance().list.ensureIndexIsVisible(last);
        }
        if (getInstance().list.getModel().getSize() > 100 && getInstance().list.getModel().getElementAt(0) != null) {
            getInstance().model.remove(0);
        }
    }

    /**
     * @param message
     */
    public static void addMessage(String message) {
        addMessage(message, true);
    }

    protected static void clearLogger() {
        getInstance().model.clear();
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

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable() {
        this.clearable = true;
    }
}

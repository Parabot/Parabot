package org.parabot.core.ui;

import java.awt.*;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.environment.randoms.Random;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author JKetelaar
 */
public class RandomUI {

    private static RandomUI instance;

    private JFrame               frame;

    public static RandomUI getInstance() {
        return instance == null ? instance = new RandomUI() : instance;
    }

    public void openFrame(ArrayList<String> randoms) {
        frame = new JFrame();
        frame.setBounds(100, 100, 351, 100 + (randoms.size() * 35));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnSubmit = new JButton("Close");
        btnSubmit.setBounds(228, 35 + (randoms.size() * 35), 117, 29);
        frame.getContentPane().add(btnSubmit);

        JLabel lblRandoms = new JLabel("Randoms:");
        lblRandoms.setBounds(6, 6, 250, 16);
        frame.getContentPane().add(lblRandoms);

        if (randoms.size() > 0) {
            for (int i = 0; i < randoms.size(); i++) {
                final int yLine = 25 + (i * 35);
                final String randomName = randoms.get(i);
                final Random r = Context.getInstance().getRandomHandler().forName(randomName);

                Label lbl = new Label(randomName);
                lbl.setBounds(10, yLine, 50, 23);
                frame.getContentPane().add(lbl);

                Button btn = new Button("Run now");
                btn.setBounds((int) lbl.getBounds().getMaxX() + 5, yLine, 70, 23);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Core.verbose("Executing random '" + r.getName() + "'");
                        Context.getInstance().getRandomHandler().executeRandom(r);
                    }
                });
                frame.getContentPane().add(btn);

                final JCheckBox checkBox = new JCheckBox("Enabled");
                checkBox.setBounds((int) btn.getBounds().getMaxX() + 5, yLine, 80, 23);
                frame.getContentPane().add(checkBox);
                if (isActive(randomName)) {
                    checkBox.setSelected(true);
                }
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Core.verbose("Random '" + r.getName() + "' is now " + (checkBox.isSelected() ? "Active" : "Inactive"));
                        if (checkBox.isSelected()) {
                            Context.getInstance().getRandomHandler().setActive(r.getName());
                        } else {
                            Context.getInstance().getRandomHandler().removeActive(r.getName());
                        }
                    }
                });
            }
        } else {
            JLabel lblNone = new JLabel("None (yet).");
            lblNone.setBounds(6, 35, 120, 16);
            frame.getContentPane().add(lblNone);
        }

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.addMessage("Active randoms count: "+Context.getInstance().getRandomHandler().getActiveRandoms().size());
                frame.dispose();
            }
        });
        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
    }

    private boolean isActive(String random) {
        for (Random r : Context.getInstance().getRandomHandler().getActiveRandoms()) {
            if (r.getName().equalsIgnoreCase(random.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
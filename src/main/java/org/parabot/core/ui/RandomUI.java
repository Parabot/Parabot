package org.parabot.core.ui;

import org.parabot.core.Context;
import org.parabot.core.Core;
import org.parabot.environment.randoms.Random;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * @author JKetelaar
 */
public class RandomUI implements ActionListener {

    private static RandomUI instance;

    private JFrame frame;
    private ArrayList<JCheckBox> checkBoxes;

    public static RandomUI getInstance() {
        return instance == null ? instance = new RandomUI() : instance;
    }

    public void openFrame(ArrayList<String> randoms) {
        frame = new JFrame();
        frame.setBounds(100, 100, 351, 100 + (randoms.size() * 35));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setBounds(228, 35 + (randoms.size() * 35), 117, 29);
        frame.getContentPane().add(btnSubmit);

        JLabel lblRandoms = new JLabel("Randoms:");
        lblRandoms.setBounds(6, 6, 250, 16);
        frame.getContentPane().add(lblRandoms);

        if (randoms.size() > 0) {
            checkBoxes = new ArrayList<>();
            for (int i = 0; i < randoms.size(); i++) {
                JCheckBox checkBox = new JCheckBox(randoms.get(i));
                checkBox.setBounds(6, 35 + (i * 35), 250, 23);
                frame.getContentPane().add(checkBox);
                if (isActive(randoms.get(i))) {
                    checkBox.setSelected(true);
                }
                checkBoxes.add(checkBox);
            }
        } else {
            JLabel lblNone = new JLabel("None (yet).");
            lblNone.setBounds(6, 35, 120, 16);
            frame.getContentPane().add(lblNone);
        }

        btnSubmit.addActionListener(this);
        this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Context.getInstance().getRandomHandler().clearActiveRandoms();
        if (checkBoxes != null && checkBoxes.size() > 0) {
            for (JCheckBox checkBox : this.checkBoxes) {
                if (checkBox.isSelected()) {
                    for (Random r : Context.getInstance().getRandomHandler().getRandoms()) {
                        if (r.getName().equalsIgnoreCase(checkBox.getText().toLowerCase())) {
                            Core.verbose("Actived random '" + r.getName() + "'");
                            Context.getInstance().getRandomHandler().setActive(r.getName());
                        }
                    }
                }
            }
        }
        this.frame.dispose();
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
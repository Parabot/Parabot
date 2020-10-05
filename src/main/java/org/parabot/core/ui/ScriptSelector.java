package org.parabot.core.ui;

import org.parabot.core.Configuration;
import org.parabot.core.Context;
import org.parabot.core.Directories;
import org.parabot.core.desc.ScriptDescription;
import org.parabot.core.parsers.scripts.ScriptParser;
import org.parabot.environment.scripts.Category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

/**
 * Script Selector GUI, shows all scripts
 *
 * @author Everel
 */
public final class ScriptSelector extends JFrame {
    private static final long serialVersionUID = 1L;
    public static ScriptParser parser;
    private final int WIDTH;
    private final int HEIGHT;
    private final HashMap<String, DefaultMutableTreeNode> categories;
    private final HashMap<String, ScriptDescription> format;
    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel model;
    private final Font fontCategory = new Font("Arial", Font.BOLD, 12);
    private final Font fontScript = new Font("Arial", Font.PLAIN, 12);
    private JTree tree;
    private JEditorPane scriptInfo;

    public ScriptSelector() {
        this.categories = new HashMap<>();
        this.format = new HashMap<>();
        this.root = new DefaultMutableTreeNode("Scripts");
        this.WIDTH = 640;
        this.HEIGHT = 256 + 128;
        this.model = new DefaultTreeModel(root);
        putScripts();
        createUI();
    }

    private void runScript(ScriptDescription desc) {
        dispose();
        final ThreadGroup tg = Context.threadGroups.keySet().iterator()
                .next();
        ScriptParser.SCRIPT_CACHE.get(desc).run(tg);
    }

    private void putScripts() {
        final ScriptDescription[] descs = ScriptParser.getDescriptions();
        if (descs == null) {
            return;
        }
        for (final ScriptDescription scriptDesc : descs) {
            if (categories.get(scriptDesc.category) == null) {
                DefaultMutableTreeNode cat = new DefaultMutableTreeNode(Category.valueOf(scriptDesc.category.toUpperCase()));
                cat.add(new DefaultMutableTreeNode(scriptDesc.scriptName));
                root.add(cat);
                categories.put(scriptDesc.category, cat);
            } else {
                categories.get(scriptDesc.category).add(
                        new DefaultMutableTreeNode(scriptDesc.scriptName));
            }

            format.put(scriptDesc.scriptName, scriptDesc);
        }
    }

    private String getScriptName(String path) {
        return path.split(", ")[2].replaceAll("\\]", "");
    }

    private String getServerDesc(final String[] servers) {
        if (servers == null) {
            return "Unknown";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < servers.length; i++) {
            builder.append(servers[i]);
            if ((i + 1) < servers.length) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private void createUI() {

        this.setTitle("Script Selector");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        tree = new JTree();
        tree.setCellRenderer(new ScriptTreeCellRenderer());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setModel(model);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                String path = e.getPath().toString();
                if (path.split(",").length == 3) {

                    // local scripts
                    ScriptDescription def = format.get(getScriptName(e
                            .getPath().toString()));
                    if (def != null) {
                        StringBuilder html = new StringBuilder("<html>");
                        html.append("<h1><font color=\"black\">")
                                .append(def.scriptName)
                                .append("</font></h1><br/>");
                        html.append("<font color=\"black\"><b>Author: </b>")
                                .append(def.author).append("</font><br/>");
                        html.append("<font color=\"black\"><b>Servers: </b>")
                                .append(getServerDesc(def.servers))
                                .append("</font><br/>");
                        html.append("<font color=\"black\"><b>Version: </b>")
                                .append(def.version).append("</font><br/>");
                        html.append(
                                "<font color=\"black\"><b>Description: </b>")
                                .append(def.description).append("</font><br/>");
                        html.append("</html>");
                        scriptInfo.setText(new String(html));
                    }

                }
            }
        });

        scriptInfo = new JEditorPane();
        scriptInfo.setContentType("text/html");
        scriptInfo.setEditable(false);

        JScrollPane scrlScriptTree = new JScrollPane(tree);
        scrlScriptTree.setBounds(4, 4, WIDTH / 2 - 4 - 64, HEIGHT - 4 - 28);

        JScrollPane scrlScriptInfo = new JScrollPane(scriptInfo);
        scrlScriptInfo.setBounds(WIDTH / 2 + 4 - 64, 4, WIDTH / 2 - 8 + 64,
                HEIGHT - 4 - 28);

        JButton cmdStart = new JButton("Start");
        cmdStart.setBounds(WIDTH - 96 - 4, HEIGHT - 24 - 4, 96, 24);
        cmdStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = getScriptName(tree.getSelectionPath().toString());
                runScript(format.get(s));
            }
        });

        JButton cmdHome = new JButton("Open home");
        cmdHome.setBounds(WIDTH - (96 * 2) - 4 - 32, HEIGHT - 24 - 4, 96 + 32,
                24);
        cmdHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(Directories.getWorkspace());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton cmdScripts = new JButton("Add scripts");
        cmdScripts.setBounds(12, HEIGHT - 24 - 4, 96 + 32,
                24);
        cmdScripts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                URI uri = URI
                        .create(Configuration.BDN_PAGE);
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Connection Error",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });

        panel.add(scrlScriptTree);
        panel.add(scrlScriptInfo);
        panel.add(cmdStart);
        panel.add(cmdHome);
        panel.add(cmdScripts);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(getOwner());

    }

    private class ScriptTreeCellRenderer implements TreeCellRenderer {
        private final JLabel label;

        ScriptTreeCellRenderer() {
            label = new JLabel();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree list, Object value,
                                                      boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean focused) {
            Object o = ((DefaultMutableTreeNode) value).getUserObject();
            BufferedImage icon = (o instanceof Category ? ((Category) o)
                    .getIcon() : Category.getIcon("script"));
            label.setIcon(icon != null ? new ImageIcon(icon) : null);
            label.setFont(o instanceof Category ? fontCategory : fontScript);
            label.setForeground(selected ? Color.DARK_GRAY : Color.BLACK);
            label.setText(String.valueOf(value));
            return label;
        }
    }
}

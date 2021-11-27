package org.parabot.core.ui;

import org.parabot.core.Context;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.reflect.RefClass;
import org.parabot.core.reflect.RefField;
import org.parabot.environment.api.utils.StringUtils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A Reflection explorer
 *
 * @author Everel
 */
public class ReflectUI extends JFrame {
    private static final long serialVersionUID = 98565034137367257L;
    private final JTree tree;
    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel model;
    private final JEditorPane basicInfoPane;
    private final JEditorPane selectionInfoPane;

    private final Object instance;

    private final HashMap<DefaultMutableTreeNode, RefClass> classes;
    private final HashMap<DefaultMutableTreeNode, RefField> fields;

    public ReflectUI() {
        this.root = new DefaultMutableTreeNode("Classes");
        this.model = new DefaultTreeModel(root);
        this.basicInfoPane = new JEditorPane();
        this.selectionInfoPane = new JEditorPane();
        this.classes = new HashMap<>();
        this.fields = new HashMap<>();
        this.instance = Context.getInstance().getClient();

        fillModel();

        setTitle("Reflection explorer");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel exploreContent = new JPanel();
        exploreContent.setLayout(new BoxLayout(exploreContent, BoxLayout.X_AXIS));

        JPanel infoContent = new JPanel();
        infoContent.setLayout(new BoxLayout(infoContent, BoxLayout.Y_AXIS));

        JPanel searchContent = new JPanel();
        searchContent.setLayout(new BoxLayout(searchContent, BoxLayout.X_AXIS));

        final JTextField searchFunction = new JTextField(10);
        searchFunction.setHorizontalAlignment(JTextField.CENTER);
        searchFunction.setSize(30, 30);
        searchFunction.setColumns(1);
        searchFunction.setPreferredSize(new Dimension(30, 30));

        final JButton searchButton = new JButton("Search");
        searchFunction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButton.getActionListeners()[0].actionPerformed(e);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RefField result = null;
                String search = searchFunction.getText();
                for (RefField f : fields.values()) {
                    if (f != null && (f.asObject()) != null) {
                        String value;
                        if ((value = f.asObject().toString()).equalsIgnoreCase(search)) {
                            result = f;
                        } else if (value.toLowerCase().startsWith(search.toLowerCase())) {
                            result = f;
                        } else if (value.toLowerCase().endsWith(search.toLowerCase())) {
                            result = f;
                        } else if (value.toLowerCase().contains(search.toLowerCase())) {
                            result = f;
                        }
                    }
                }
                if (result != null) {
                    setFieldInfo(result);
                }
            }
        });

        final JButton adjustClasses = new JButton("Expand");
        adjustClasses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Enumeration<?> topLevelNodes
                        = ((TreeNode) tree.getModel().getRoot()).children();
                while (topLevelNodes.hasMoreElements()) {
                    DefaultMutableTreeNode node
                            = (DefaultMutableTreeNode) topLevelNodes.nextElement();
                    TreePath treePath = new TreePath(node.getPath());
                    if (adjustClasses.getText().equalsIgnoreCase("expand")) {
                        tree.expandPath(treePath);
                    } else {
                        tree.collapsePath(treePath);
                    }
                }

                adjustClasses.setText(adjustClasses.getText().equalsIgnoreCase("expand") ? "Collapse" : "Expand");
            }
        });

        searchContent.add(adjustClasses);
        searchContent.add(searchFunction);
        searchContent.setMaximumSize(new Dimension(500, (int) searchContent.getPreferredSize().getHeight()));
        searchContent.add(searchButton);

        tree = new JTree();
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setModel(model);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent event) {
                TreePath path = event.getPath();
                Object[] pathElements = path.getPath();
                Object element = pathElements[pathElements.length - 1];
                if (pathElements.length == 2) {
                    setClassInfo(classes.get(element));
                } else if (pathElements.length == 3) {
                    RefField field = fields.get(element);
                    setFieldInfo(field);
                    DefaultMutableTreeNode el = (DefaultMutableTreeNode) element;
                    el.setUserObject("Field: " + field.getName() + " [type: " + field.getASMType() + "] [value: " + field.asObject() + "]");
                }
            }

        });

        JScrollPane scrollTreePane = new JScrollPane(tree);
        scrollTreePane.setPreferredSize(new Dimension(400, 300));

        basicInfoPane.setContentType("text/html");
        basicInfoPane.setEditable(false);

        selectionInfoPane.setContentType("text/html");
        selectionInfoPane.setEditable(false);

        JScrollPane scrollBasicInfoPane = new JScrollPane(basicInfoPane);
        scrollBasicInfoPane.setPreferredSize(new Dimension(400, 90));

        JScrollPane scrollSelectInfoPane = new JScrollPane(selectionInfoPane);
        scrollSelectInfoPane.setPreferredSize(new Dimension(400, 200));

        infoContent.add(scrollBasicInfoPane);
        infoContent.add(Box.createRigidArea(new Dimension(0, 10)));
        infoContent.add(scrollSelectInfoPane);

        exploreContent.add(scrollTreePane);
        exploreContent.add(Box.createRigidArea(new Dimension(10, 0)));
        exploreContent.add(infoContent);

        content.add(exploreContent);
        content.add(searchContent);

        JScrollPane contentPane = new JScrollPane(content);
        Dimension prefSize = content.getPreferredSize();
        contentPane.setPreferredSize(new Dimension(prefSize.width + contentPane.getVerticalScrollBar().getPreferredSize().width, prefSize.height + contentPane.getHorizontalScrollBar().getPreferredSize().height));
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void fillModel() {
        Context context = Context.getInstance();
        ClassPath classPath = context.getClassPath();
        ASMClassLoader classLoader = context.getASMClassLoader();
        for (String className : classPath.classNames) {
            try {
                DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
                        "Class: " + className);
                DefaultMutableTreeNode fieldNode;

                Class<?> clazz = classLoader.loadClass(className);

                RefClass refClass = new RefClass(clazz);
                if (refClass.instanceOf(this.instance)) {
                    refClass.setInstance(this.instance);
                }

                for (RefField field : refClass.getFields()) {
                    fieldNode = new DefaultMutableTreeNode("Field: " + field.getName() + " [type: " + field.getASMType() + "] [value: " + field.asObject() + "]");
                    classNode.add(fieldNode);
                    fields.put(fieldNode, field);
                }
                classes.put(classNode, refClass);

                root.add(classNode);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private void fillBasicInfoPane() {
        Context context = Context.getInstance();
        ClassPath classPath = context.getClassPath();

        StringBuilder builder = new StringBuilder();

        builder.append("<b>Classes: </b>").append(classPath.classNames.size()).append("<br/>");
        builder.append("<b>Using instance: </b>").append(instance.toString()).append("<br/>");

        basicInfoPane.setText(builder.toString());
    }

    private void setFieldInfo(RefField refField) {
        StringBuilder builder = new StringBuilder();
        RefClass refClass = refField.getOwner();
        builder.append("<h1>").append(refClass.getClassName()).append(".").append(refField.getName()).append("</h1><br/>");
        builder.append("<b>Class: </b>").append(refClass.getClassName()).append("<br/>");
        builder.append("<b>Value: </b>").append(refField.asObject()).append("<br/>");
        builder.append("<b>Type: </b>").append(refField.getASMType().getClassName()).append("<br/>");
        builder.append("<b>Static: </b>").append(refField.isStatic() ? "yes" : "no").append("<br/>");
        builder.append("<b>Array: </b>").append(refField.isArray() ? refField.getArrayDimensions() + " dimension(s)" : "no").append("<br/>");

        if (refField.isArray()) {
            if (refField.getArrayDimensions() == 1) {
                if (refField.getASMType().getClassName().contains("int")) {
                    int[] ints = (int[]) refField.asObject();
                    String values = "";
                    for (int i = 0; i < ints.length; i++) {
                        values += (ints[i] + (i < ints.length - 1 ? ", " : ""));
                    }

                    builder.append("<b>Values: </b>").append(values).append("<br/>");
                } else if (refField.getASMType().getClassName().contains("String")) {
                    String[] strings = (String[]) refField.asObject();
                    String values = StringUtils.implode(", ", strings);

                    builder.append("<b>Values: </b>").append(values).append("<br/>");
                }
            }
        }
        selectionInfoPane.setText(builder.toString());

        fillBasicInfoPane();
    }

    private void setClassInfo(RefClass refClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>").append(refClass.getClassName()).append("</h1><br/>");
        if (refClass.getClassName().contains(".")) {
            builder.append("<b>Package: </b>").append(refClass.getClassName(), 0, refClass.getClassName().lastIndexOf(".")).append("<br/>");
        }
        builder.append("<b>Abstract: </b>").append(refClass.isAbstract() ? "yes" : "no").append("<br/>");
        builder.append("<b>Interface: </b>").append(refClass.isInterface() ? "yes" : "no").append("<br/>");
        builder.append("<b>Superclass: </b>").append(refClass.hasSuperclass() ? refClass.getSuperclass().getClassName() : "no").append("<br/>");
        builder.append("<b>Fields: </b>").append(refClass.getFields().length).append("<br/>");
        builder.append("<b>Methods: </b>").append(refClass.getMethods().length).append("<br/>");
        builder.append("<b>Constructors: </b>").append(refClass.getConstructors().length).append("<br/>");
        selectionInfoPane.setText(builder.toString());

        fillBasicInfoPane();
    }

}
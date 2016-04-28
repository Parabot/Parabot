package org.parabot.core.ui;

import org.parabot.core.Context;
import org.parabot.core.asm.ASMClassLoader;
import org.parabot.core.classpath.ClassPath;
import org.parabot.core.reflect.RefClass;
import org.parabot.core.reflect.RefField;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.HashMap;

/**
 * A Reflection explorer
 *
 * @author Everel
 */
public class ReflectUI extends JFrame {
    private static final long serialVersionUID = 98565034137367257L;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JEditorPane basicInfoPane;
    private JEditorPane selectionInfoPane;

    private Object instance;

    private HashMap<DefaultMutableTreeNode, RefClass> classes;
    private HashMap<DefaultMutableTreeNode, RefField> fields;

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

        JTree tree = new JTree();
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
                    // class
                    setClassInfo(classes.get(element));
                }
                if (pathElements.length == 3) {
                    // field
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
        selectionInfoPane.setText(builder.toString());

        fillBasicInfoPane();
    }

    private void setClassInfo(RefClass refClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>").append(refClass.getClassName()).append("</h1><br/>");
        if (refClass.getClassName().contains(".")) {
            builder.append("<b>Package: </b>").append(refClass.getClassName().substring(0, refClass.getClassName().lastIndexOf("."))).append("<br/>");
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
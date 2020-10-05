package org.parabot.core.classpath;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.Directories;
import org.parabot.core.asm.ClassRemapper;
import org.parabot.core.asm.RedirectClassAdapter;
import org.parabot.core.build.BuildPath;
import org.parabot.core.io.SizeInputStream;
import org.parabot.core.ui.components.VerboseLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Manages, parses and dumps class files & jars
 *
 * @author Everel
 * @author Matt
 */
public class ClassPath {
    public final ArrayList<String> classNames;
    public final HashMap<String, ClassNode> classes;
    public final Map<String, File> resources;
    private final ClassRemapper classRemapper;
    private final boolean isJar;
    private final ArrayList<URL> jarFiles;
    public URL lastParsed;
    private boolean parseJar;

    public ClassPath() {
        this(false);
    }

    public ClassPath(final boolean isJar) {
        this.classNames = new ArrayList<String>();
        this.classes = new HashMap<String, ClassNode>();
        this.resources = new HashMap<String, File>();
        this.classRemapper = new ClassRemapper();
        this.parseJar = true;
        this.jarFiles = new ArrayList<URL>();
        this.isJar = isJar;
    }

    public void addJar(final File file) {
        try {
            addJar(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addJar(final URL url) {
        this.lastParsed = url;
        try {
            addJar(url.openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a jar to this classpath
     *
     * @param connection
     */
    public void addJar(final URLConnection connection) {
        try {
            final int size = connection.getContentLength();
            final SizeInputStream sizeInputStream = new SizeInputStream(
                    connection.getInputStream(), size, VerboseLoader.get());
            final ZipInputStream zin = new ZipInputStream(sizeInputStream);
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                if (e.isDirectory()) {
                    continue;
                }
                if (e.getName().endsWith(".class")) {
                    loadClass(zin);
                } else {
                    loadResource(e.getName(), zin);
                }
                VerboseLoader.setState("Downloading: " + e.getName());
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        VerboseLoader.get().onProgressUpdate(100);
    }

    /**
     * Adds a jar to this classpath
     *
     * @param url - in string format
     */
    public void addJar(final String url) {
        try {
            addJar(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Whether jar files should be parsed or ignored
     *
     * @param enabled
     */
    public void parseJarFiles(final boolean enabled) {
        this.parseJar = enabled;
    }

    /**
     * Finds and loads all classes/jar files in folder
     *
     * @param directory
     */
    public void addClasses(final File directory) {
        if (directory == null || !directory.isDirectory()) {
            throw new IllegalArgumentException("Not a valid directory.");
        }
        addClasses(directory, null);
    }

    /**
     * Finds and loads all classes/jar files in folder
     *
     * @param f    to find class / jar files
     * @param root
     */
    public void addClasses(final File f, File root) {
        if (f == null) {
            return;
        }
        if (!f.exists()) {
            f.mkdirs();
        }
        if (root == null) {
            root = f;
        }
        for (File f1 : f.listFiles()) {
            if (f1 == null) {
                continue;
            } else if (f1.isDirectory()) {
                addClasses(f1, root);
            } else {
                try (FileInputStream fin = new FileInputStream(f1)) {
                    if (f1.getName().endsWith(".class")) {
                        loadClass(fin);
                    } else if (f.equals(root) && f1.getName().endsWith(".jar")) {
                        jarFiles.add(f1.toURI().toURL());
                        if (this.parseJar) {
                            // if enabled, there may be problem with duplicate
                            // class names.......
                            addJar(f1.toURI().toURL());
                        }
                    } else {
                        String path = f1.toURI().relativize(root.toURI())
                                .getPath();
                        loadResource(path, fin);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Determines if this classpath represents a jar file
     *
     * @return if this classpath represents a jar file
     */
    public boolean isJar() {
        return isJar;
    }

    /**
     * Gets all jar files in this classpath
     *
     * @return array of classpath
     */
    public ClassPath[] getJarFiles() {
        final ClassPath[] jars = new ClassPath[jarFiles.size()];
        for (int i = 0; i < jarFiles.size(); i++) {
            final ClassPath classPath = new ClassPath(true);
            classPath.addJar(jarFiles.get(i));
            jars[i] = classPath;
        }
        return jars;
    }

    /**
     * Adds this jar to buildpath
     */
    public void addToBuildPath() {
        BuildPath.add(lastParsed);
    }

    /**
     * Dump this classPath classes to a jar file
     *
     * @param fileName
     */
    public void dump(final String fileName) {
        dump(new File(fileName));
    }

    /**
     * Dump this classPath classes to a jar file
     *
     * @param file
     */
    public void dump(final File file) {
        try {
            dump(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dumps this classPath classes to a jar file
     *
     * @param stream
     */
    public void dump(final FileOutputStream stream) {
        try {
            JarOutputStream out = new JarOutputStream(stream);
            for (ClassNode cn : this.classes.values()) {
                JarEntry je = new JarEntry(cn.name + ".class");
                out.putNextEntry(je);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                cn.accept(cw);
                out.write(cw.toByteArray());
            }
            for (Entry<String, File> entry : this.resources.entrySet()) {
                JarEntry je = new JarEntry(entry.getKey());
                out.putNextEntry(je);
                out.write(Files.readAllBytes(entry.getValue().toPath()));
            }
            out.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads class from input stream
     *
     * @param in
     *
     * @throws IOException
     */
    protected void loadClass(InputStream in) throws IOException {
        ClassReader cr = new ClassReader(in);
        ClassNode cn = new ClassNode();
        RemappingClassAdapter rca = new RemappingClassAdapter(cn, classRemapper);
        RedirectClassAdapter redir = new RedirectClassAdapter(rca);
        cr.accept(redir, ClassReader.EXPAND_FRAMES);
        classNames.add(cn.name.replace('/', '.'));
        classes.put(cn.name, cn);
    }

    /**
     * Dumps a resource from a input stream
     *
     * @param classPath
     * @param name
     * @param inputstream
     *
     * @throws IOException
     */
    private void loadResource(final String name, final InputStream in)
            throws IOException {
        final File f = File.createTempFile("bot", ".tmp",
                Directories.getTempDirectory());
        f.deleteOnExit();
        try (OutputStream out = new FileOutputStream(f)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        this.resources.put(name, f);
    }

}

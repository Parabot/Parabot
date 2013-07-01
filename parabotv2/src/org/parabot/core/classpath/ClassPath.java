package org.parabot.core.classpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.parabot.core.build.BuildPath;

/**
 * 
 * @author Clisprail
 * @author Matt
 */
public class ClassPath {
	public final HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
	public final Map<String, URL> resources = new HashMap<String, URL>();
	
	private boolean isJar = false;
	private boolean parseJar = true;
	private ArrayList<URL> jarFiles = new ArrayList<URL>();
	
	public URL lastParsed = null;
	
	public ClassPath() {
		
	}
	
	public ClassPath(final boolean isJar) {
		this.isJar = isJar;
	}

	/**
	 * Adds jar to this classpath
	 * @param jarLocation
	 */
	public void addJar(final String jarLocation) {
		JarParser.parseJar(this, jarLocation);
	}
	
	/**
	 * Adds jar to this classpath
	 * @param jarLocation
	 */
	public void addJar(final URL jarLocation) {
		JarParser.parseJar(this, jarLocation.toString());
	}
	
	public void parseJarFiles(final boolean enabled) {
		this.parseJar = enabled;
	}

	/**
	 * Finds and loads all classes/jar files in folder
	 * 
	 * @param file to find class / jar files
	 * @param root
	 */
	public void loadClasses(final File f, File root) {
		if (f == null)
			return;
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
				loadClasses(f1, root);
			} else {
				try (FileInputStream fin = new FileInputStream(f1)) {
					if (f1.getName().endsWith(".class"))
						loadClass(fin);
					else if (f.equals(root) && f1.getName().endsWith(".jar")) {
						jarFiles.add(f1.toURI().toURL());
						if(this.parseJar) {
							// if enabled, there may be problem with duplicate class names.......
							loadClasses(f1.toURI().toURL());
						}
					} else {
						String path = f1.toURI().relativize(root.toURI())
								.getPath();
						Resources.loadResource(this, path, fin);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Loads classes from zip/jar files
	 * 
	 * @param url to file
	 */
	public void loadClasses(URL u) {
		this.lastParsed = u;
		try (ZipInputStream zin = new ZipInputStream(u.openStream())) {
			ZipEntry e;
			while ((e = zin.getNextEntry()) != null) {
				if (e.isDirectory())
					continue;
				if (e.getName().endsWith(".class")) {
					loadClass(zin);
				} else {
					Resources.loadResource(this, e.getName(), zin);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads class from input stream
	 * 
	 * @param inputstream
	 * @throws IOException
	 */
	private void loadClass(InputStream in) throws IOException {
		ClassReader cr = new ClassReader(in);
		ClassNode cn = new ClassNode();
		//cr.accept(cn, 0);
		cr.accept(cn, 0);
		//cr.accept(cn, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classes.put(cn.name, cn);
	}


	/**
	 * Dumps the classnodes into a jar
	 * 
	 * @param jarName
	 */
	public void dump(final String jarName) {
		JarDumper.dump(this, jarName);
	}
	
	public boolean isJar() {
		return isJar;
	}
	
	public ClassPath[] getJarFiles() {
		final ClassPath[] jars = new ClassPath[jarFiles.size()];
		for(int i = 0; i < jarFiles.size(); i++) {
			final ClassPath classPath = new ClassPath(true);
			classPath.loadClasses(jarFiles.get(i));
			jars[i] = classPath;
		}
		return jars;
	}
	
	public void addToBuildPath() {
		BuildPath.add(lastParsed);
	}


}

package org.parabot.core.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.parabot.core.Core;
import org.parabot.core.Directories;
import org.parabot.core.asm.redirect.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class RedirectClassAdapter extends ClassVisitor implements Opcodes {

    private static final Map<String, Class<?>> redirects = new HashMap<String, Class<?>>();
    private static PrintStream str_out, class_out;

    static {
        redirects.put("java/awt/Toolkit", ToolkitRedirect.class);
        redirects.put("java/lang/Class", ClassRedirect.class);
//        redirects.put("java/lang/ClassLoader", ClassLoaderRedirect.class);
        redirects.put("java/net/URLClassLoader", URLClassLoaderRedirect.class);
        redirects.put("java/lang/Runtime", RuntimeRedirect.class);
        redirects.put("java/lang/management/RuntimeMXBean", RuntimeMXBeanRedirect.class);
        redirects.put("java/lang/Thread", ThreadRedirect.class);
        redirects.put("java/lang/StackTraceElement", StackTraceElementRedirect.class);
        redirects.put("java/lang/ProcessBuilder", ProcessBuilderRedirect.class);
        redirects.put("java/lang/System", SystemRedirect.class);
    }

    private String className;

    public RedirectClassAdapter(ClassVisitor cv) {
        super(ASM5, cv);
        if (str_out == null && Core.shouldDump()) {
            try {
                str_out = new PrintStream(new FileOutputStream(new File(Directories.getWorkspace(), "strings.txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (class_out == null && Core.shouldDump()) {
            try {
                class_out = new PrintStream(new FileOutputStream(new File(Directories.getWorkspace(), "classes.txt")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Class<?>> getRedirects() {
        return redirects;
    }

    public static SecurityException createSecurityException() {
        Exception e = new Exception();
        StackTraceElement[] elements = e.getStackTrace();
        return new SecurityException("Unsafe operation blocked. Op:"
                + elements[1].getMethodName());
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
        if (class_out != null) {
            class_out.println(className + " References:");
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (class_out != null) {
            class_out.println();
            class_out.println();
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        return new ReflectionMethodVisitor(name, desc, super.visitMethod(
                access, name, desc, signature, exceptions));
    }

    private class ReflectionMethodVisitor extends MethodVisitor {

        public ReflectionMethodVisitor(String name, String desc,
                                       MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitLdcInsn(Object o) {
            if (o instanceof String && str_out != null) {
                str_out.println(className + " " + o);
            }
            super.visitLdcInsn(o);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name,
                                    String desc, boolean itf) {
            if (Core.isSecure()) {
                if (redirects.containsKey(owner) && !name.equals("<init>")
                        && !name.equals("<clinit>")) {
                    if (opcode != INVOKESTATIC) {
                        desc = "(L" + owner + ";" + desc.substring(1);
                    }
                    opcode = INVOKESTATIC;
                    owner = redirects.get(owner).getName()
                            .replaceAll("\\.", "/");
                }
            }

            if (class_out != null) {
                class_out.println(owner);
            }

            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name,
                                   String desc) {
            if (Core.isSecure() && (opcode == GETSTATIC || opcode == PUTSTATIC)) {
                if (redirects.containsKey(owner)) {
                    owner = redirects.get(owner).getName()
                            .replaceAll("\\.", "/");
                }
            }
            if (class_out != null) {
                class_out.println(owner);
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

    }

}

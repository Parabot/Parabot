package org.parabot.core.asm.redirect;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;

public class ToolkitRedirect {

    private static final Clipboard clipboard = new Clipboard("default");

    static {
        clipboard.setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[0];
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return false;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                throw new UnsupportedFlavorException(flavor);
            }
        }, null);
    }

    public static Toolkit getDefaultToolkit() {
        return Toolkit.getDefaultToolkit();
    }

    public static Dimension getScreenSize(Toolkit t) {
        return new Dimension(0, 0);
    }

    public static Image createImage(Toolkit t, byte[] b) {
        return t.createImage(b);
    }

    public static Image createImage(Toolkit t, String s) {
        return t.createImage(s);
    }

    public static Image createImage(Toolkit t, ImageProducer i) {
        return t.createImage(i);
    }

    public static Image getImage(Toolkit t, URL u) {
        return t.getImage(u);
    }

    public static Image getImage(Toolkit t, String str) {
        return t.getImage(str);
    }

    public static Cursor createCustomCursor(Toolkit t, Image i, Point p, String s) {
        return Cursor.getDefaultCursor();
    }

    public static Clipboard getSystemClipboard(Toolkit toolkit) {
        return clipboard;
    }

    public static void sync(Toolkit toolkit) {
        toolkit.sync();
    }

    public static void sync() {
        Toolkit.getDefaultToolkit().sync();
    }
}

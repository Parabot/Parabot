package org.parabot.environment.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author JKetelaar
 */
public class FileUtil {

    public static String getChecksum(File file) {
        if (file.isFile()) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                if (file.exists()) {
                    FileInputStream fis       = new FileInputStream(file);
                    byte[]          dataBytes = new byte[1024];

                    int nread;

                    while ((nread = fis.read(dataBytes)) != -1) {
                        md.update(dataBytes, 0, nread);
                    }

                    byte[] mdbytes = md.digest();

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mdbytes.length; i++) {
                        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    return sb.toString();
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] getChecksumBytes(File file) {
        if (file.isFile()) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                if (file.exists()) {
                    FileInputStream fis       = new FileInputStream(file);
                    byte[]          dataBytes = new byte[1024];

                    int nread;

                    while ((nread = fis.read(dataBytes)) != -1) {
                        md.update(dataBytes, 0, nread);
                    }

                    return md.digest();
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void copyFile(File sourceFile, File destFile)
            throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source      = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        destination.close();
    }
}

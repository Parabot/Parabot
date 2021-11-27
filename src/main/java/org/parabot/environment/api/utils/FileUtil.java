package org.parabot.environment.api.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
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
                    FileInputStream fis = new FileInputStream(file);
                    byte[] dataBytes = new byte[1024];

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
                    FileInputStream fis = new FileInputStream(file);
                    byte[] dataBytes = new byte[1024];

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
        FileChannel source = null;
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

    /**
     * Reads the contents of a text file
     *
     * @param file file to get contents from
     *
     * @return file contents
     *
     * @throws IOException when anything goes wrong
     */
    public static String getFileContents(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    /**
     * Writes a string to a file overwriting the existing contents if present
     *
     * @param file     file to write to
     * @param contents contents to write to given file
     *
     * @throws IOException when anything goes wrong
     */
    public static void writeFileContents(File file, String contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        writer.write(contents);

        writer.close();
    }
}

package org.parabot.environment.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author JKetelaar
 */
public class FileUtil {

    public static String getChecksum(File file){
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

                    StringBuilder sb = new StringBuilder("");
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
}

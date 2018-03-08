package com.steven.Smartglass.Baidutranslate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5() {
    }

    public static String md5(String input) {
        if(input == null) {
            return null;
        } else {
            try {
                MessageDigest e = MessageDigest.getInstance("MD5");
                byte[] inputByteArray = input.getBytes();
                e.update(inputByteArray);
                byte[] resultByteArray = e.digest();
                return byteArrayToHex(resultByteArray);
            } catch (NoSuchAlgorithmException var4) {
                return null;
            }
        }
    }

    public static String md5(File file) {
        try {
            if(!file.isFile()) {
                System.err.println("文件" + file.getAbsolutePath() + "不存在或者不是文件");
                return null;
            }

            FileInputStream e = new FileInputStream(file);
            String result = md5((InputStream)e);
            e.close();
            return result;
        } catch (FileNotFoundException var3) {
            var3.printStackTrace();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public static String md5(InputStream in) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            boolean read = false;

            int read1;
            while((read1 = in.read(buffer)) != -1) {
                e.update(buffer, 0, read1);
            }

            in.close();
            String result = byteArrayToHex(e.digest());
            return result;
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        byte[] var6 = byteArray;
        int var5 = byteArray.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            byte b = var6[var4];
            resultCharArray[index++] = hexDigits[b >>> 4 & 15];
            resultCharArray[index++] = hexDigits[b & 15];
        }

        return new String(resultCharArray);
    }
}

package com.ghostcat.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GhostEncryptUtils {

    public static String getMD5(byte[] bytes) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            md5.update(bytes);

            //digest()返回md5的hash值，返回8位字符。因为md5的hash值是16位的hex，实际就是8位字符
            byte[] md5Hash = md5.digest();

            //将btye转成16进制的字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : md5Hash) {
                int v = b & 0xFF;
                if (v < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toString(v, 16));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMD5(String str) {
        byte[] bytes = str.getBytes();
        return getMD5(bytes);
    }
}

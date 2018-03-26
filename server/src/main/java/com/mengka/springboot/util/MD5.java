package com.mengka.springboot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: yudongwang
 * Date: 13-8-20
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class MD5 {
    public static String crypt(String str) {
        if (str == null || str.length() == 0)
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte hash[] = md.digest();
            for (int i = 0; i < hash.length; i++)
                if ((0xff & hash[i]) < 16)
                    hexString.append((new StringBuilder("0")).append(Integer.toHexString(0xff & hash[i])).toString());
                else
                    hexString.append(Integer.toHexString(0xff & hash[i]));

        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        return hexString.toString();
    }
}

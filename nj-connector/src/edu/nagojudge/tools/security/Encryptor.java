/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nagojudge.tools.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author andresfelipegarciaduran
 */
public class Encryptor {

    private static final String ALGORITHM = "AES";

    private static final String defaultSecretKey = "NagoJudge";
    private Key secretKeySpec;

    public Encryptor() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        this(null);
    }

    public Encryptor(String secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException {
        this.secretKeySpec = generateKey(secretKey);
    }

    public String encrypt(String plainText) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        return asHexString(encrypted);
    }

    public String decrypt(String encryptedString) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] original = cipher.doFinal(toByteArray(encryptedString));
        return new String(original);
    }

    private Key generateKey(String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (secretKey == null) {
            secretKey = defaultSecretKey;
        }
        byte[] key = (secretKey).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only the first 128 bit

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available

        return new SecretKeySpec(key, ALGORITHM);
    }

    private String asHexString(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    private byte[] toByteArray(String hexString) {
        int arrLength = hexString.length() >> 1;
        byte buf[] = new byte[arrLength];
        for (int ii = 0; ii < arrLength; ii++) {
            int index = ii << 1;
            String l_digit = hexString.substring(index, index + 2);
            buf[ii] = (byte) Integer.parseInt(l_digit, 16);
        }
        return buf;
    }

    public static void main(String[] args) throws Exception {

        String plainText = "wrichard";
        String secretKey = "NagoJudge";
        Encryptor aes = new Encryptor(secretKey);
        String encryptedString = aes.encrypt(plainText);
        System.out.println(plainText + ":" + encryptedString);

    }
}

package ru.innopolis.deliveryhelper.model;

import android.provider.Settings;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.adorsys.android.securestoragelibrary.SecurePreferences;

public class SafeStorage {

    private final static String USERNAME = "username_key";
    private final static String PASSWORD = "password_key";
    private final static String TOKEN = "token_key";
    private final static String ADDRESS = "ip_address";

    static public void setUsername(String username) {
        SecurePreferences.setValue(USERNAME, username);
    }
    static public String getUsername() {
        return SecurePreferences.getStringValue(USERNAME, null);
    }

    static public void setPassword(String password) {
        SecurePreferences.setValue(PASSWORD, password);
    }
    static public String getPassword() {
        return SecurePreferences.getStringValue(PASSWORD, null);
    }

    static public void setToken(String token) {
        SecurePreferences.setValue(TOKEN, token);
    }
    static public String getToken() {
        return SecurePreferences.getStringValue(TOKEN,null);
    }

    static public void setAddress(String address) {
        SecurePreferences.setValue(ADDRESS, address);
    }
    static public String getAddress() {
        return SecurePreferences.getStringValue(ADDRESS, null);
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String hash(String input) throws NoSuchAlgorithmException {
        return input;
        //return bytesToHexString(MessageDigest.getInstance("SHA-256").digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    public static void clearCredentials() {
        setToken("");
        setUsername("");
        setPassword("");
    }
}

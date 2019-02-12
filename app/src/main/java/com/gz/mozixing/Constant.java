package com.gz.mozixing;

/**
 * @author Alex
 * @since 19/1/30
 */

public class Constant {

    final static public String API_Target_url = BuildConfig.request_Url + "/";

    final static public String md5Key = "C*mXgi19%dxs1_1^s&$~";
    public static final String GREEN_APP_KEY = "RUzvdqO5vqRyZuY19wkdhDJMCrCU9hrK";
    public static String AuthTokenHolder = "";

    //Save String
    final static public String defaultPreferenceName = "SaveData";
    final static public String userTheme = "ThemeBlue";
    final static public String authToken = "AuthToken";

    //Global Function
    static public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}

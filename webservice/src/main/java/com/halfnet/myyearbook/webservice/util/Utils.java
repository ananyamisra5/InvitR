package com.halfnet.myyearbook.webservice.util;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class Utils {
private static final Random rand = new Random();

    /**
     * Utils cannot be instantiated
     */
    private Utils() {
    }

    /**
     * Thanks Hannes R. from Stack Overflow equivalent to
     * {@code getHTTPDate(0);}
     *
     * @return the date time in HTTP format
     */
    public static String getHTTPDate() {
        return getHTTPDate(0);
    }

    /**
     * gets the date and time in HTTP Format, with an offset
     *
     * @param millisAdd the time to add, in milliseconds
     * @return the HTTP Formatted time
     */
    public static String getHTTPDate(long millisAdd) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date(calendar.getTimeInMillis() + millisAdd));
    }

    /**
     * returns the time in the Hour:Minute:Second format
     * @return the current time
     */
    public static String getTimeNoDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * creates a BCrypt password hash from a plaintext password and salt
     * @param password the plaintext password
     * @param salt the password salt
     * @return a generated hash
     */
    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    /**
     * generates a BCrypt Standard Salt
     *
     * @return the BCrypt salt
     */
    public static String genSalt() {
        return BCrypt.gensalt();
    }

    /**
     * checks if a plaintext password matches a hashed password
     * @param pass the plaintext password
     * @param hash the password hash
     * @return true if the password matches the hash
     */
    public static boolean checkPassword(String pass, String hash) {
        return BCrypt.checkpw(pass, hash);
    }

    /**
     * creates a random token, used for tracking user logins
     *
     * @return the 256 byte token in base 64
     */
    public static String generateToken() {
        byte[] byts = new byte[256];
        rand.nextBytes(byts);
        return Base64.getEncoder().encodeToString(byts).substring(0, 256);
    }

    public static String exceptionStackTraceToString(Throwable t) {
        StringBuilder buff = new StringBuilder(1024);
        buff.append("Exception in thread \"")
                .append(Thread.currentThread().getName())
                .append("\" ")
                .append(t.getClass().getName())
                .append(": ")
                .append(t.getMessage() == null ? "" : t.getMessage());
        for (StackTraceElement s : t.getStackTrace()) {
            buff.append("        at ")
                    .append(s.toString());
        }
        return buff.toString();
    }

    public static byte[] decodeBase64(String b64){
        return Base64.getDecoder().decode(b64);
    }
    
    public static String genPasswordReset(){
        return generateToken().substring(0,8);
    }
}

package com.Validation;
import java.util.regex.Pattern;
public class Regex {
    private static final String MAIL_REGEX = "^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String CELL_REGEX = "^\\+?[1-9]\\d{1,14}$";

    public static void checkMail(String m) throws Exception {
        if (!Pattern.matches(MAIL_REGEX, m)) throw new Exception("Invalid Email Format");
    }

    public static void checkPass(String p) throws Exception {
        if (p.length() < 8) throw new Exception("Password must be 8+ characters");
    }

    public static void checkCell(String c) throws Exception {
        if (!Pattern.matches(CELL_REGEX, c)) throw new Exception("Invalid Mobile Format");
    }
}

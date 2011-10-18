package utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.sun.org.apache.xerces.internal.util.XMLChar;


public class PlatinoEncodeRute {

    private static final Pattern ENCODE_PATTERN = Pattern.compile("_x\\p{XDigit}{4}_");
    private static final char[] PADDING = new char[] {'0', '0', '0'};
    private static final String HEX_DIGITS = "0123456789abcdefABCDEF";
    
    public static String encode(String name) {
        if (name.length() == 0) {
            return name;
        }
        if (XMLChar.isValidName(name) && name.indexOf("_x") < 0) {
            return name;
        } else {
            StringBuffer encoded = new StringBuffer();
            for (int i = 0; i < name.length(); i++) {
                if (i == 0) {
                    if (XMLChar.isNameStart(name.charAt(i))) {
                        if (needsEscaping(name, i)) {
                            encode('_', encoded);
                        } else {
                            encoded.append(name.charAt(i));
                        }
                    } else {
                        encode(name.charAt(i), encoded);
                    }
                } else if (!XMLChar.isName(name.charAt(i)) && (name.charAt(i) != '/') && (name.charAt(i) != '*')) {
                    encode(name.charAt(i), encoded);
                } else {
                    if (needsEscaping(name, i)) {
                        encode('_', encoded);
                    } else {
                        encoded.append(name.charAt(i));
                    }
                }
            }
            return encoded.toString();
        }
    }

    public static String decode(String name) {
        if (name.indexOf("_x") < 0) {
            return name;
        }
        StringBuffer decoded = new StringBuffer();
        Matcher m = ENCODE_PATTERN.matcher(name);
        while (m.find()) {
            m.appendReplacement(decoded, Character.toString((char) Integer.parseInt(m.group().substring(2, 6), 16)));
        }
        m.appendTail(decoded);
        return decoded.toString();
    }

    private static void encode(char c, StringBuffer b) {
        b.append("_x");
        String hex = Integer.toHexString(c);
        b.append(PADDING, 0, 4 - hex.length());
        b.append(hex);
        b.append("_");
    }

    private static boolean needsEscaping(String name, int location)
            throws ArrayIndexOutOfBoundsException {
        if (name.charAt(location) == '_' && name.length() >= location + 6) {
            return name.charAt(location + 1) == 'x'
                && HEX_DIGITS.indexOf(name.charAt(location + 2)) != -1
                && HEX_DIGITS.indexOf(name.charAt(location + 3)) != -1
                && HEX_DIGITS.indexOf(name.charAt(location + 4)) != -1
                && HEX_DIGITS.indexOf(name.charAt(location + 5)) != -1;
        } else {
            return false;
        }
    }
}
/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.internal;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public final class UriUtils {
    private static final BitSet ALLOWED;
    private static final char[] HEX;

    private UriUtils() {
    }

    private static boolean isHex(char ch) {
        return ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f';
    }

    private static String percentEncodeIllegal(String s) {
        StringBuilder out = new StringBuilder(s.length() + 16);
        int n = s.length();
        int i = 0;
        while (i < n) {
            int nextI;
            char ch = s.charAt(i);
            if (ch == '%' && i + 2 < n && UriUtils.isHex(s.charAt(i + 1)) && UriUtils.isHex(s.charAt(i + 2))) {
                out.append(s, i, i + 3);
                nextI = i + 3;
            } else if (ch < '\u0080' && ALLOWED.get(ch)) {
                out.append(ch);
                nextI = i + 1;
            } else {
                byte[] bytes;
                int cp = s.codePointAt(i);
                for (byte b : bytes = new String(Character.toChars(cp)).getBytes(StandardCharsets.UTF_8)) {
                    int v = b & 0xFF;
                    out.append('%').append(HEX[v >>> 4]).append(HEX[v & 0xF]);
                }
                nextI = i + Character.charCount(cp);
            }
            i = nextI;
        }
        return out.toString();
    }

    public static URI createUriSafely(String uriString) {
        if (uriString == null || uriString.trim().isEmpty()) {
            return null;
        }
        try {
            return URI.create(uriString);
        }
        catch (IllegalArgumentException ex) {
            String encoded = UriUtils.percentEncodeIllegal(uriString);
            try {
                return URI.create(encoded);
            }
            catch (IllegalArgumentException ex2) {
                return null;
            }
        }
    }

    static {
        int c;
        ALLOWED = new BitSet(128);
        HEX = "0123456789ABCDEF".toCharArray();
        for (c = 97; c <= 122; c = (int)((char)(c + 1))) {
            ALLOWED.set(c);
        }
        for (c = 65; c <= 90; c = (int)((char)(c + 1))) {
            ALLOWED.set(c);
        }
        for (c = 48; c <= 57; c = (int)((char)(c + 1))) {
            ALLOWED.set(c);
        }
        for (char c2 : new char[]{'-', '.', '_', '~'}) {
            ALLOWED.set(c2);
        }
        for (char c2 : new char[]{':', '/', '?', '#', '[', ']', '@', '!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '='}) {
            ALLOWED.set(c2);
        }
    }
}


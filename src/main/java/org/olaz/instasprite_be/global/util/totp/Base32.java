package org.olaz.instasprite_be.global.util.totp;

import java.util.Arrays;

/**
 * RFC-4648 Base32 (no padding) encoder/decoder for TOTP secrets.
 */
public final class Base32 {

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
    private static final int[] LOOKUP = new int[256];

    static {
        Arrays.fill(LOOKUP, -1);
        for (int i = 0; i < ALPHABET.length; i++) {
            LOOKUP[ALPHABET[i]] = i;
            LOOKUP[Character.toLowerCase(ALPHABET[i])] = i;
        }
    }

    private Base32() {}

    public static String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        StringBuilder out = new StringBuilder((data.length * 8 + 4) / 5);
        int buffer = data[0] & 0xff;
        int next = 1;
        int bitsLeft = 8;

        while (bitsLeft > 0 || next < data.length) {
            if (bitsLeft < 5) {
                if (next < data.length) {
                    buffer <<= 8;
                    buffer |= data[next++] & 0xff;
                    bitsLeft += 8;
                } else {
                    int pad = 5 - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }

            int index = (buffer >> (bitsLeft - 5)) & 0x1f;
            bitsLeft -= 5;
            out.append(ALPHABET[index]);
        }

        return out.toString();
    }

    public static byte[] decode(String base32) {
        if (base32 == null) {
            return new byte[0];
        }

        String s = base32.trim().replace("=", "");
        if (s.isEmpty()) {
            return new byte[0];
        }

        int buffer = 0;
        int bitsLeft = 0;
        byte[] out = new byte[s.length() * 5 / 8];
        int outPos = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int val = c < 256 ? LOOKUP[c] : -1;
            if (val == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }

            buffer <<= 5;
            buffer |= val & 0x1f;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                out[outPos++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xff);
                bitsLeft -= 8;
            }
        }

        if (outPos == out.length) {
            return out;
        }
        return Arrays.copyOf(out, outPos);
    }
}



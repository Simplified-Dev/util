package dev.simplified.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Null-safe utility methods for {@code char} primitives and {@link Character} objects.
 * <p>
 * Each method documents its specific null-handling behavior in detail.
 */
@UtilityClass
public final class CharUtil {

    /** Linefeed character LF ({@code '\n'}, Unicode 000a). */
    public static final char LF = '\n';

    /** Carriage return character CR ({@code '\r'}, Unicode 000d). */
    public static final char CR = '\r';

    /** Null control character NUL ({@code '\0'}, Unicode 0000). */
    public static final char NUL = '\0';

    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    static {
        for (char c = 0; c < CHAR_STRING_ARRAY.length; c++)
            CHAR_STRING_ARRAY[c] = String.valueOf(c);
    }

    /**
     * Converts the string to a {@link Character} using the first character, returning
     * {@code null} for null or empty strings.
     * <p>
     * For ASCII 7-bit characters, this uses a cache that returns the
     * same {@link Character} object each time.
     *
     * <pre>
     *   CharUtil.toCharacterObject(null) = null
     *   CharUtil.toCharacterObject("")   = null
     *   CharUtil.toCharacterObject("A")  = 'A'
     *   CharUtil.toCharacterObject("BA") = 'B'
     * </pre>
     *
     * @param str the string to convert
     * @return the {@link Character} value of the first letter of the string, or {@code null}
     */
    public static Character toCharacterObject(final String str) {
        if (StringUtil.isEmpty(str))
            return null;

        return str.charAt(0);
    }

    /**
     * Converts the {@link Character} to a {@code char}, throwing an exception for {@code null}.
     *
     * <pre>
     *   CharUtil.toChar(' ')  = ' '
     *   CharUtil.toChar('A')  = 'A'
     *   CharUtil.toChar(null) throws NullPointerException
     * </pre>
     *
     * @param ch the character to convert
     * @return the char value of the character
     * @throws NullPointerException if the character is null
     */
    public static char toChar(@NotNull final Character ch) {
        return ch;
    }

    /**
     * Converts the {@link Character} to a {@code char}, returning a default value for {@code null}.
     *
     * <pre>
     *   CharUtil.toChar(null, 'X') = 'X'
     *   CharUtil.toChar(' ', 'X')  = ' '
     *   CharUtil.toChar('A', 'X')  = 'A'
     * </pre>
     *
     * @param ch the character to convert
     * @param defaultValue the value to use if the character is null
     * @return the char value of the character, or the default if null
     */
    public static char toChar(final Character ch, final char defaultValue) {
        if (ch == null)
            return defaultValue;

        return ch;
    }

    /**
     * Converts the string to a {@code char} using the first character, throwing an exception
     * on empty strings.
     *
     * <pre>
     *   CharUtil.toChar("A")  = 'A'
     *   CharUtil.toChar("BA") = 'B'
     *   CharUtil.toChar(null) throws NullPointerException
     *   CharUtil.toChar("")   throws StringIndexOutOfBoundsException
     * </pre>
     *
     * @param str the string to convert
     * @return the char value of the first letter of the string
     * @throws NullPointerException          if the string is null
     * @throws StringIndexOutOfBoundsException if the string is empty
     */
    public static char toChar(@NotNull final String str) {
        return str.charAt(0);
    }

    /**
     * Converts the string to a {@code char} using the first character, returning a default value
     * for null or empty strings.
     *
     * <pre>
     *   CharUtil.toChar(null, 'X') = 'X'
     *   CharUtil.toChar("", 'X')   = 'X'
     *   CharUtil.toChar("A", 'X')  = 'A'
     *   CharUtil.toChar("BA", 'X') = 'B'
     * </pre>
     *
     * @param str the string to convert
     * @param defaultValue the value to use if the string is null or empty
     * @return the char value of the first letter of the string, or the default if null or empty
     */
    public static char toChar(final String str, final char defaultValue) {
        if (StringUtil.isEmpty(str))
            return defaultValue;

        return str.charAt(0);
    }

    /**
     * Converts the character to the integer it represents, throwing an exception if the
     * character is not numeric.
     * <p>
     * This method converts the char {@code '1'} to the int {@code 1} and so on.
     *
     * <pre>
     *   CharUtil.toIntValue('3')  = 3
     *   CharUtil.toIntValue('A')  throws IllegalArgumentException
     * </pre>
     *
     * @param ch the character to convert
     * @return the int value of the character
     * @throws IllegalArgumentException if the character is not ASCII numeric
     */
    public static int toIntValue(final char ch) {
        if (!isAsciiNumeric(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
        }
        return ch - 48;
    }

    /**
     * Converts the character to the integer it represents, returning a default value if the
     * character is not numeric.
     * <p>
     * This method converts the char {@code '1'} to the int {@code 1} and so on.
     *
     * <pre>
     *   CharUtil.toIntValue('3', -1)  = 3
     *   CharUtil.toIntValue('A', -1)  = -1
     * </pre>
     *
     * @param ch the character to convert
     * @param defaultValue the default value to use if the character is not numeric
     * @return the int value of the character, or the default if not numeric
     */
    public static int toIntValue(final char ch, final int defaultValue) {
        if (!isAsciiNumeric(ch)) {
            return defaultValue;
        }
        return ch - 48;
    }

    /**
     * Converts the {@link Character} to the integer it represents, throwing an exception if the
     * character is not numeric.
     * <p>
     * This method converts the char {@code '1'} to the int {@code 1} and so on.
     *
     * <pre>
     *   CharUtil.toIntValue('3')  = 3
     *   CharUtil.toIntValue(null) throws NullPointerException
     *   CharUtil.toIntValue('A')  throws IllegalArgumentException
     * </pre>
     *
     * @param ch the character to convert, not null
     * @return the int value of the character
     * @throws NullPointerException     if the character is null
     * @throws IllegalArgumentException if the character is not ASCII numeric
     */
    public static int toIntValue(@NotNull final Character ch) {
        return toIntValue(ch.charValue());
    }

    /**
     * Converts the {@link Character} to the integer it represents, returning a default value if the
     * character is null or not numeric.
     * <p>
     * This method converts the char {@code '1'} to the int {@code 1} and so on.
     *
     * <pre>
     *   CharUtil.toIntValue(null, -1) = -1
     *   CharUtil.toIntValue('3', -1)  = 3
     *   CharUtil.toIntValue('A', -1)  = -1
     * </pre>
     *
     * @param ch the character to convert
     * @param defaultValue the default value to use if the character is null or not numeric
     * @return the int value of the character, or the default if null or not numeric
     */
    public static int toIntValue(final Character ch, final int defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return toIntValue(ch.charValue(), defaultValue);
    }

    /**
     * Converts the character to a {@link String} containing that one character.
     * <p>
     * For ASCII 7-bit characters, this uses a cache that returns the
     * same {@link String} object each time.
     *
     * <pre>
     *   CharUtil.toString(' ')  = " "
     *   CharUtil.toString('A')  = "A"
     * </pre>
     *
     * @param ch the character to convert
     * @return a string containing the one specified character
     */
    public static String toString(final char ch) {
        if (ch < 128) {
            return CHAR_STRING_ARRAY[ch];
        }
        return String.valueOf(ch);
    }

    /**
     * Converts the {@link Character} to a {@link String} containing that one character,
     * returning {@code null} for a {@code null} input.
     * <p>
     * For ASCII 7-bit characters, this uses a cache that returns the
     * same {@link String} object each time.
     *
     * <pre>
     *   CharUtil.toString(null) = null
     *   CharUtil.toString(' ')  = " "
     *   CharUtil.toString('A')  = "A"
     * </pre>
     *
     * @param ch the character to convert, may be null
     * @return a string containing the one specified character, or {@code null}
     */
    public static String toString(final Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }

    /**
     * Converts the character to the Unicode escape format {@code '\u0020'}.
     * This format is the Java source code format.
     *
     * <pre>
     *   CharUtil.unicodeEscaped(' ') = "\u0020"
     *   CharUtil.unicodeEscaped('A') = "\u0041"
     * </pre>
     *
     * @param ch the character to convert
     * @return the escaped Unicode string
     */
    public static String unicodeEscaped(final char ch) {
        return ("\\u" +
            HEX_DIGITS[(ch >> 12) & 15] +
            HEX_DIGITS[(ch >> 8) & 15] +
            HEX_DIGITS[(ch >> 4) & 15] +
            HEX_DIGITS[(ch) & 15]);
    }

    /**
     * Converts the {@link Character} to the Unicode escape format {@code '\u0020'},
     * returning {@code null} for a {@code null} input.
     * This format is the Java source code format.
     *
     * <pre>
     *   CharUtil.unicodeEscaped(null) = null
     *   CharUtil.unicodeEscaped(' ')  = "\u0020"
     *   CharUtil.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch the character to convert, may be null
     * @return the escaped Unicode string, or {@code null} if null input
     */
    public static String unicodeEscaped(final Character ch) {
        if (ch == null)
            return null;

        return unicodeEscaped(ch.charValue());
    }

    /**
     * Checks whether the character is ASCII 7-bit.
     *
     * <pre>
     *   CharUtil.isAscii('a')  = true
     *   CharUtil.isAscii('A')  = true
     *   CharUtil.isAscii('3')  = true
     *   CharUtil.isAscii('-')  = true
     *   CharUtil.isAscii('\n') = true
     *   CharUtil.isAscii('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if less than 128
     */
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    /**
     * Checks whether the character is ASCII 7-bit printable.
     *
     * <pre>
     *   CharUtil.isAsciiPrintable('a')  = true
     *   CharUtil.isAsciiPrintable('A')  = true
     *   CharUtil.isAsciiPrintable('3')  = true
     *   CharUtil.isAsciiPrintable('-')  = true
     *   CharUtil.isAsciiPrintable('\n') = false
     *   CharUtil.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * Checks whether the character is ASCII 7-bit control.
     *
     * <pre>
     *   CharUtil.isAsciiControl('a')  = false
     *   CharUtil.isAsciiControl('A')  = false
     *   CharUtil.isAsciiControl('3')  = false
     *   CharUtil.isAsciiControl('-')  = false
     *   CharUtil.isAsciiControl('\n') = true
     *   CharUtil.isAsciiControl('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if less than 32 or equals 127
     */
    public static boolean isAsciiControl(final char ch) {
        return ch < 32 || ch == 127;
    }

    /**
     * Checks whether the character is ASCII 7-bit alphabetic.
     *
     * <pre>
     *   CharUtil.isAsciiAlpha('a')  = true
     *   CharUtil.isAsciiAlpha('A')  = true
     *   CharUtil.isAsciiAlpha('3')  = false
     *   CharUtil.isAsciiAlpha('-')  = false
     *   CharUtil.isAsciiAlpha('\n') = false
     *   CharUtil.isAsciiAlpha('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlpha(final char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }

    /**
     * Checks whether the character is ASCII 7-bit alphabetic upper case.
     *
     * <pre>
     *   CharUtil.isAsciiAlphaUpper('a')  = false
     *   CharUtil.isAsciiAlphaUpper('A')  = true
     *   CharUtil.isAsciiAlphaUpper('3')  = false
     *   CharUtil.isAsciiAlphaUpper('-')  = false
     *   CharUtil.isAsciiAlphaUpper('\n') = false
     *   CharUtil.isAsciiAlphaUpper('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 65 and 90 inclusive
     */
    public static boolean isAsciiAlphaUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * Checks whether the character is ASCII 7-bit alphabetic lower case.
     *
     * <pre>
     *   CharUtil.isAsciiAlphaLower('a')  = true
     *   CharUtil.isAsciiAlphaLower('A')  = false
     *   CharUtil.isAsciiAlphaLower('3')  = false
     *   CharUtil.isAsciiAlphaLower('-')  = false
     *   CharUtil.isAsciiAlphaLower('\n') = false
     *   CharUtil.isAsciiAlphaLower('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 97 and 122 inclusive
     */
    public static boolean isAsciiAlphaLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * Checks whether the character is ASCII 7-bit numeric.
     *
     * <pre>
     *   CharUtil.isAsciiNumeric('a')  = false
     *   CharUtil.isAsciiNumeric('A')  = false
     *   CharUtil.isAsciiNumeric('3')  = true
     *   CharUtil.isAsciiNumeric('-')  = false
     *   CharUtil.isAsciiNumeric('\n') = false
     *   CharUtil.isAsciiNumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 48 and 57 inclusive
     */
    public static boolean isAsciiNumeric(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * Checks whether the character is ASCII 7-bit alphanumeric.
     *
     * <pre>
     *   CharUtil.isAsciiAlphanumeric('a')  = true
     *   CharUtil.isAsciiAlphanumeric('A')  = true
     *   CharUtil.isAsciiAlphanumeric('3')  = true
     *   CharUtil.isAsciiAlphanumeric('-')  = false
     *   CharUtil.isAsciiAlphanumeric('\n') = false
     *   CharUtil.isAsciiAlphanumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return {@code true} if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlphanumeric(final char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }

    /**
     * Compares two {@code char} values numerically.
     *
     * @param x the first {@code char} to compare
     * @param y the second {@code char} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final char x, final char y) {
        return x - y;
    }

}

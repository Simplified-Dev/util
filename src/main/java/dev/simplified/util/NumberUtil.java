package dev.sbs.api.util;

import dev.sbs.api.reflection.Reflection;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * Utility methods for Java {@link Number} classes, including parsing, formatting,
 * comparison, and mathematical operations.
 */
@UtilityClass
public final class NumberUtil {

    private static final Pattern FLOATING_POINT_PATTERN = fpPattern();
    private static Pattern fpPattern() {
        /*
         * We use # instead of * for possessive quantifiers. This lets us strip them out when building
         * the regex for RE2 (which doesn't support them) but leave them in when building it for
         * java.util.regex (where we want them in order to avoid catastrophic backtracking).
         */
        String decimal = "\\d+#(?:\\.\\d*#)?|\\.\\d+#";
        String completeDec = decimal + "(?:[eE][+-]?\\d+#)?[fFdD]?";
        String hex = "[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#";
        String completeHex = "0[xX]" + hex + "[pP][+-]?\\d+#[fFdD]?";
        String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
        fpPattern = fpPattern.replace(
            "#",
            "+"
        );
        return Pattern.compile(fpPattern);
    }

    private final static TreeMap<Integer, String> TO_ROMAN_MAP = new TreeMap<>();
    private final static TreeMap<String, Integer> FROM_ROMAN_MAP = new TreeMap<>();
    private final static NavigableMap<Long, String> FORMAT_SUFFIX = new TreeMap<>();

    static {
        TO_ROMAN_MAP.put(1000, "M");
        TO_ROMAN_MAP.put(900, "CM");
        TO_ROMAN_MAP.put(500, "D");
        TO_ROMAN_MAP.put(400, "CD");
        TO_ROMAN_MAP.put(100, "C");
        TO_ROMAN_MAP.put(90, "XC");
        TO_ROMAN_MAP.put(50, "L");
        TO_ROMAN_MAP.put(40, "XL");
        TO_ROMAN_MAP.put(10, "X");
        TO_ROMAN_MAP.put(9, "IX");
        TO_ROMAN_MAP.put(5, "V");
        TO_ROMAN_MAP.put(4, "IV");
        TO_ROMAN_MAP.put(1, "I");
        TO_ROMAN_MAP.forEach((value, roman) -> FROM_ROMAN_MAP.put(roman, value));

        FORMAT_SUFFIX.put(1_000L, "k");
        FORMAT_SUFFIX.put(1_000_000L, "M");
        FORMAT_SUFFIX.put(1_000_000_000L, "B");
        FORMAT_SUFFIX.put(1_000_000_000_000L, "T");
        FORMAT_SUFFIX.put(1_000_000_000_000_000L, "P");
        FORMAT_SUFFIX.put(1_000_000_000_000_000_000L, "E");
    }

    /**
     * Returns the smallest integer greater than or equal to the given number,
     * using bit manipulation for performance.
     *
     * @param number the value to ceil
     * @return the ceiling of the given number as an {@code int}
     */
    public static int ceil(double number) {
        int floor = (int)number;
        return (((double)floor == number) ? floor : (floor + (int)(~Double.doubleToRawLongBits(number) >>> 63)));
    }

    /**
     * Returns the largest integer less than or equal to the given number,
     * using bit manipulation for performance.
     *
     * @param number the value to floor
     * @return the floor of the given number as an {@code int}
     */
    public static int floor(double number) {
        int floor = (int)number;
        return (((double)floor == number) ? floor : (floor - (int)(Double.doubleToRawLongBits(number) >>> 63)));
    }

    /**
     * Formats a number with a magnitude suffix (k, M, B, etc.).
     *
     * @param value the number to format
     * @return the formatted number with suffix
     */
    public static String format(double value) {
        return format((long)value);
    }

    /**
     * Formats a number with a magnitude suffix (k, M, B, etc.).
     *
     * @param value the number to format
     * @return the formatted number with suffix
     */
    @SuppressWarnings("all")
    public static String format(long value) {
        // Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = FORMAT_SUFFIX.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); // the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    /**
     * Converts a roman numeral string to its integer value.
     *
     * @param roman the roman numeral string to convert
     * @return the integer value of the roman numeral
     */
    public static int fromRoman(String roman) {
        int result = 0;

        for (int i = 0; i < StringUtil.length(roman); i++) {
            // Getting value of symbol s[i]
            int s1 = FROM_ROMAN_MAP.get(Character.toString(roman.charAt(i)));

            // Getting value of symbol s[i+1]
            if (i + 1 < StringUtil.length(roman)) {
                int s2 = FROM_ROMAN_MAP.get(Character.toString(roman.charAt(i + 1)));

                // Comparing both values
                if (s1 >= s2)
                    result += s1; // Value of current symbol is >= the next symbol
                else {
                    // Value of current symbol is < the next symbol
                    result += s2 - s1;
                    i++;
                }
            } else
                result += s1;
        }

        return result;
    }

    /**
     * Clamps a value to the specified range.
     *
     * @param value the value to clamp
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the clamped value within [{@code min}, {@code max}]
     */
    public static int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Checks whether a value falls within the specified inclusive range.
     *
     * @param value the value to check
     * @param min the minimum bound (inclusive)
     * @param max the maximum bound (inclusive)
     * @return {@code true} if the value is within [{@code min}, {@code max}]
     */
    public static boolean inRange(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }

    /**
     * Checks whether the given {@code double} is finite (not infinite and not NaN).
     *
     * @param d the value to check
     * @return {@code true} if the value is finite
     */
    public static boolean isFinite(double d) {
        return Math.abs(d) <= 1.7976931348623157E308D;
    }

    /**
     * Checks whether the given {@code float} is finite (not infinite and not NaN).
     *
     * @param f the value to check
     * @return {@code true} if the value is finite
     */
    public static boolean isFinite(float f) {
        return Math.abs(f) <= 3.4028235E38F;
    }

    /**
     * Formats a number with its ordinal suffix (e.g., 1st, 2nd, 3rd, 4th).
     *
     * @param value the number to format
     * @return the number with its ordinal suffix
     */
    public static String ordinal(int value) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };

        return switch (value % 100) {
            case 11, 12, 13 -> value + "th";
            default -> value + sufixes[value % 10];
        };
    }

    /**
     * Generates a random integer between the specified minimum and {@link Integer#MAX_VALUE}.
     *
     * @param minimum the lowest number allowed (inclusive)
     * @return a random integer between the specified minimum and {@code Integer.MAX_VALUE} (exclusive)
     */
    public static int rand(int minimum) {
        return rand(minimum, Integer.MAX_VALUE);
    }

    /**
     * Generates a random integer between the specified minimum and maximum.
     *
     * @param minimum the lowest number allowed (inclusive)
     * @param maximum the highest number allowed (exclusive)
     * @return a random integer between the specified boundaries
     */
    public static int rand(int minimum, int maximum) {
        return (int) ThreadLocalRandom.current().nextLong(minimum, maximum);
    }

    /**
     * Rounds a value to the nearest integer.
     *
     * @param value the number to round
     * @return the rounded number
     */
    public static double round(double value) {
        return round(value, 0);
    }

    /**
     * Rounds a value to the specified number of decimal places.
     *
     * @param value the number to round
     * @param precision the number of decimal places to keep
     * @return the rounded number
     */
    public static double round(double value, int precision) {
        int scale = (int)Math.pow(10, precision);
        return (double)Math.round(value * scale) / scale;
    }

    /**
     * Rounds a number up to the nearest multiple of the given value.
     *
     * @param number the number to round up
     * @param multipleOf the multiple to round to
     * @return the rounded up value
     */
    public static int roundUp(double number, int multipleOf) {
        return (int)(Math.ceil(number / multipleOf) * multipleOf);
    }

    /**
     * Rounds a number down to the nearest multiple of the given value.
     *
     * @param number the number to round down
     * @param multipleOf the multiple to round to
     * @return the rounded down value
     */
    public static int roundDown(double number, int multipleOf) {
        return (int)(Math.floor(number / multipleOf) * multipleOf);
    }

    /**
     * Returns the square of the given number.
     *
     * @param number the value to square
     * @return the square of the number
     */
    public static double square(double number) {
        return number * number;
    }

    /**
     * Converts the given integer to a roman numeral string.
     *
     * @param number the number to convert to roman numerals
     * @return the roman numeral representation
     */
    public static String toRoman(int number) {
        if (number == 0) return "";
        int highest = TO_ROMAN_MAP.floorKey(number);

        if (number == highest)
            return TO_ROMAN_MAP.get(number);

        return TO_ROMAN_MAP.get(highest) + toRoman(number - highest);
    }

    /**
     * Converts a value to the specified {@link Number} subclass, returning zero on failure.
     *
     * @param <N>  the target number type
     * @param value the value to convert
     * @param clazz the target number class
     * @return the converted number, or zero if conversion fails
     */
    public static <N extends Number> N to(Object value, Class<N> clazz) {
        return to(value, 0, clazz);
    }

    /**
     * Converts a value to the specified {@link Number} subclass, returning the
     * default value if the input is not a creatable number.
     *
     * @param <N>  the target number type
     * @param value the value to convert
     * @param defaultValue the fallback value if conversion fails
     * @param clazz the target number class
     * @return the converted number, or the default value if conversion fails
     */
    public static <N extends Number> N to(Object value, Number defaultValue, Class<N> clazz) {
        Reflection<N> number = new Reflection<>(clazz);
        String strValue = String.valueOf(value);
        return clazz.cast(number.newInstance(isCreatable(strValue) ? new BigDecimal(strValue).toPlainString() : String.valueOf(defaultValue)));
    }

    /**
     * Converts a number to its hexadecimal string representation.
     *
     * @param number the number to convert
     * @return the hexadecimal string representation
     */
    public static String toHexString(long number) {
        return Long.valueOf(String.valueOf(number), 16).toString();
    }

    /**
     * Converts a {@code String} to an {@code int}, returning
     * {@code zero} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code zero} is returned.
     *
     * <pre>
     *   NumberUtil.toInt(null) = 0
     *   NumberUtil.toInt("")   = 0
     *   NumberUtil.toInt("1")  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the int represented by the string, or {@code zero} if conversion fails
     */
    public static int toInt(final String str) {
        return toInt(str, 0);
    }

    /**
     * Converts a {@code String} to an {@code int}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toInt(null, 1) = 1
     *   NumberUtil.toInt("", 1)   = 1
     *   NumberUtil.toInt("1", 0)  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    public static int toInt(final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code String} to a {@code long}, returning
     * {@code zero} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code zero} is returned.
     *
     * <pre>
     *   NumberUtil.toLong(null) = 0L
     *   NumberUtil.toLong("")   = 0L
     *   NumberUtil.toLong("1")  = 1L
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the long represented by the string, or {@code 0} if conversion fails
     */
    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    /**
     * Converts a {@code String} to a {@code long}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toLong(null, 1L) = 1L
     *   NumberUtil.toLong("", 1L)   = 1L
     *   NumberUtil.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     */
    public static long toLong(final String str, final long defaultValue) {
        if (str == null)
            return defaultValue;

        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Parses a hexadecimal string to its base-10 {@code Long} representation.
     *
     * @param hexString the hexadecimal string to convert, may be null
     * @return the base-10 value, or {@code 0L} if conversion fails
     */
    public static Long toHexLong(String hexString) {
        return toHexLong(hexString, 0L);
    }

    /**
     * Parses a hexadecimal string to its base-10 {@code Long} representation,
     * returning a default value if the conversion fails.
     *
     * @param hexString the hexadecimal string to convert, may be null
     * @param defaultValue the default value to return on failure
     * @return the base-10 value, or the default value if conversion fails
     */
    public static Long toHexLong(String hexString, final long defaultValue) {
        if (hexString == null)
            return defaultValue;

        try {
            return Long.parseLong(hexString, 16);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code String} to a {@code float}, returning
     * {@code 0.0f} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code 0.0f} is returned.
     *
     * <pre>
     *   NumberUtil.toFloat(null)   = 0.0f
     *   NumberUtil.toFloat("")     = 0.0f
     *   NumberUtil.toFloat("1.5")  = 1.5f
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the float represented by the string, or {@code 0.0f} if conversion fails
     */
    public static float toFloat(final String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * Converts a {@code String} to a {@code float}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toFloat(null, 1.1f)   = 1.1f
     *   NumberUtil.toFloat("", 1.1f)     = 1.1f
     *   NumberUtil.toFloat("1.5", 0.0f)  = 1.5f
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the float represented by the string, or defaultValue if conversion fails
     */
    public static float toFloat(final String str, final float defaultValue) {
        if (str == null)
            return defaultValue;

        try {
            return Float.parseFloat(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code String} to a {@code double}, returning
     * {@code 0.0d} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code 0.0d} is returned.
     *
     * <pre>
     *   NumberUtil.toDouble(null)   = 0.0d
     *   NumberUtil.toDouble("")     = 0.0d
     *   NumberUtil.toDouble("1.5")  = 1.5d
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the double represented by the string, or {@code 0.0d} if conversion fails
     */
    public static double toDouble(final String str) {
        return toDouble(str, 0.0d);
    }

    /**
     * Converts a {@code String} to a {@code double}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toDouble(null, 1.1d)   = 1.1d
     *   NumberUtil.toDouble("", 1.1d)     = 1.1d
     *   NumberUtil.toDouble("1.5", 0.0d)  = 1.5d
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the double represented by the string, or defaultValue if conversion fails
     */
    public static double toDouble(final String str, final double defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code BigDecimal} to a {@code double}.
     *
     * <p>
     * If the {@code BigDecimal} is {@code null}, {@code 0.0d} is returned.
     *
     * <pre>
     *   NumberUtil.toDouble(null)                     = 0.0d
     *   NumberUtil.toDouble(BigDecimal.valueOf(8.5d)) = 8.5d
     * </pre>
     *
     * @param value the {@code BigDecimal} to convert, may be null
     * @return the double represented by the {@code BigDecimal}, or {@code 0.0d} if null
     */
    public static double toDouble(final BigDecimal value) {
        return toDouble(value, 0.0d);
    }

    /**
     * Converts a {@code BigDecimal} to a {@code double}, returning a
     * default value if the input is {@code null}.
     *
     * <pre>
     *   NumberUtil.toDouble(null, 1.1d)                     = 1.1d
     *   NumberUtil.toDouble(BigDecimal.valueOf(8.5d), 1.1d) = 8.5d
     * </pre>
     *
     * @param value the {@code BigDecimal} to convert, may be null
     * @param defaultValue the default value
     * @return the double represented by the {@code BigDecimal}, or the default if null
     */
    public static double toDouble(final BigDecimal value, final double defaultValue) {
        return value == null ? defaultValue : value.doubleValue();
    }

    /**
     * Converts a {@code String} to a {@code byte}, returning
     * {@code zero} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code zero} is returned.
     *
     * <pre>
     *   NumberUtil.toByte(null) = 0
     *   NumberUtil.toByte("")   = 0
     *   NumberUtil.toByte("1")  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the byte represented by the string, or {@code zero} if conversion fails
     */
    public static byte toByte(final String str) {
        return toByte(str, (byte) 0);
    }

    /**
     * Converts a {@code String} to a {@code byte}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toByte(null, 1) = 1
     *   NumberUtil.toByte("", 1)   = 1
     *   NumberUtil.toByte("1", 0)  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the byte represented by the string, or the default if conversion fails
     */
    public static byte toByte(final String str, final byte defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code String} to a {@code short}, returning
     * {@code zero} if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, {@code zero} is returned.
     *
     * <pre>
     *   NumberUtil.toShort(null) = 0
     *   NumberUtil.toShort("")   = 0
     *   NumberUtil.toShort("1")  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @return the short represented by the string, or {@code zero} if conversion fails
     */
    public static short toShort(final String str) {
        return toShort(str, (short) 0);
    }

    /**
     * Converts a {@code String} to a {@code short}, returning a
     * default value if the conversion fails.
     *
     * <p>
     * If the string is {@code null}, the default value is returned.
     *
     * <pre>
     *   NumberUtil.toShort(null, 1) = 1
     *   NumberUtil.toShort("", 1)   = 1
     *   NumberUtil.toShort("1", 0)  = 1
     * </pre>
     *
     * @param str the string to convert, may be null
     * @param defaultValue the default value
     * @return the short represented by the string, or the default if conversion fails
     */
    public static short toShort(final String str, final short defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * Converts a {@code BigDecimal} to a {@code BigDecimal} with a scale of
     * two that has been rounded using {@code RoundingMode.HALF_EVEN}. If the
     * supplied value is null, then {@code BigDecimal.ZERO} is returned.
     *
     * <p>
     * Note, the scale of a {@code BigDecimal} is the number of digits to the
     * right of the decimal point.
     *
     * @param value the {@code BigDecimal} to convert, may be null
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final BigDecimal value) {
        return toScaledBigDecimal(value, 2, RoundingMode.HALF_EVEN);
    }

    /**
     * Converts a {@code BigDecimal} to a {@code BigDecimal} whose scale is the
     * specified value with a {@code RoundingMode} applied. If the input value
     * is {@code null}, returns {@code BigDecimal.ZERO}.
     *
     * @param value the {@code BigDecimal} to convert, may be null
     * @param scale the number of digits to the right of the decimal point
     * @param roundingMode a rounding behavior for numerical operations capable of
     *  discarding precision
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final BigDecimal value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(
                scale,
                (roundingMode == null) ? RoundingMode.HALF_EVEN : roundingMode
        );
    }

    /**
     * Converts a {@code Float} to a {@code BigDecimal} with a scale of
     * two that has been rounded using {@code RoundingMode.HALF_EVEN}. If the
     * supplied value is null, then {@code BigDecimal.ZERO} is returned.
     *
     * <p>
     * Note, the scale of a {@code BigDecimal} is the number of digits to the
     * right of the decimal point.
     *
     * @param value the {@code Float} to convert, may be null
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final Float value) {
        return toScaledBigDecimal(value, 2, RoundingMode.HALF_EVEN);
    }

    /**
     * Converts a {@code Float} to a {@code BigDecimal} whose scale is the
     * specified value with a {@code RoundingMode} applied. If the input value
     * is {@code null}, returns {@code BigDecimal.ZERO}.
     *
     * @param value the {@code Float} to convert, may be null
     * @param scale the number of digits to the right of the decimal point
     * @param roundingMode a rounding behavior for numerical operations capable of
     *  discarding precision
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final Float value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                BigDecimal.valueOf(value),
                scale,
                roundingMode
        );
    }

    /**
     * Converts a {@code Double} to a {@code BigDecimal} with a scale of
     * two that has been rounded using {@code RoundingMode.HALF_EVEN}. If the
     * supplied value is null, then {@code BigDecimal.ZERO} is returned.
     *
     * <p>
     * Note, the scale of a {@code BigDecimal} is the number of digits to the
     * right of the decimal point.
     *
     * @param value the {@code Double} to convert, may be null
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final Double value) {
        return toScaledBigDecimal(value, 2, RoundingMode.HALF_EVEN);
    }

    /**
     * Converts a {@code Double} to a {@code BigDecimal} whose scale is the
     * specified value with a {@code RoundingMode} applied. If the input value
     * is {@code null}, returns {@code BigDecimal.ZERO}.
     *
     * @param value the {@code Double} to convert, may be null
     * @param scale the number of digits to the right of the decimal point
     * @param roundingMode a rounding behavior for numerical operations capable of
     *  discarding precision
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final Double value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                BigDecimal.valueOf(value),
                scale,
                roundingMode
        );
    }

    /**
     * Converts a {@code String} to a {@code BigDecimal} with a scale of
     * two that has been rounded using {@code RoundingMode.HALF_EVEN}. If the
     * supplied value is null, then {@code BigDecimal.ZERO} is returned.
     *
     * <p>
     * Note, the scale of a {@code BigDecimal} is the number of digits to the
     * right of the decimal point.
     *
     * @param value the {@code String} to convert, may be null
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final String value) {
        return toScaledBigDecimal(value, 2, RoundingMode.HALF_EVEN);
    }

    /**
     * Converts a {@code String} to a {@code BigDecimal} whose scale is the
     * specified value with a {@code RoundingMode} applied. If the input value
     * is {@code null}, returns {@code BigDecimal.ZERO}.
     *
     * @param value the {@code String} to convert, may be null
     * @param scale the number of digits to the right of the decimal point
     * @param roundingMode a rounding behavior for numerical operations capable of
     *  discarding precision
     * @return the scaled, with appropriate rounding, {@code BigDecimal}
     */
    public static BigDecimal toScaledBigDecimal(final String value, final int scale, final RoundingMode roundingMode) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return toScaledBigDecimal(
                createBigDecimal(value),
                scale,
                roundingMode
        );
    }

    /**
     * Attempts to parse the input string as a {@code Double}.
     *
     * @param input the string to parse
     * @return the parsed {@code Double}, or {@code null} if parsing fails
     */
    public static Double tryParseDouble(String input) {
        if (FLOATING_POINT_PATTERN.matcher(input).matches()) {
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ignore) { }
        }

        return null;
    }

    /**
     * Attempts to parse the input string as a {@code Float}.
     *
     * @param input the string to parse
     * @return the parsed {@code Float}, or {@code null} if parsing fails
     */
    public static Float tryParseFloat(String input) {
        if (FLOATING_POINT_PATTERN.matcher(input).matches()) {
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException ignore) { }
        }

        return null;
    }

    /**
     * Attempts to parse the input string as an {@code Integer} in base 10.
     *
     * @param input the string to parse
     * @return the parsed {@code Integer}, or {@code null} if parsing fails
     */
    public static Integer tryParseInt(String input) {
        return tryParseInt(input, 10);
    }

    /**
     * Attempts to parse the input string as an {@code Integer} in the given radix.
     *
     * @param input the string to parse
     * @param radix the radix to use for parsing
     * @return the parsed {@code Integer}, or {@code null} if parsing fails
     */
    public static Integer tryParseInt(String input, int radix) {
        try {
            return Integer.parseInt(input, radix);
        } catch (NumberFormatException ignore) { }

        return null;
    }

    /**
     * Attempts to parse the input string as a {@code Long} in base 10.
     *
     * @param input the string to parse
     * @return the parsed {@code Long}, or {@code null} if parsing fails
     */
    public static Long tryParseLong(String input) {
        return tryParseLong(input, 10);
    }

    /**
     * Attempts to parse the input string as a {@code Long} in the given radix.
     *
     * @param input the string to parse
     * @param radix the radix to use for parsing
     * @return the parsed {@code Long}, or {@code null} if parsing fails
     */
    public static Long tryParseLong(String input, int radix) {
        try {
            return Long.parseLong(input, radix);
        } catch (NumberFormatException ignore) { }

        return null;
    }

    /**
     * Attempts to parse the input string as a {@code Short} in base 10.
     *
     * @param input the string to parse
     * @return the parsed {@code Short}, or {@code null} if parsing fails
     */
    public static Short tryParseShort(String input) {
        return tryParseShort(input, 10);
    }

    /**
     * Attempts to parse the input string as a {@code Short} in the given radix.
     *
     * @param input the string to parse
     * @param radix the radix to use for parsing
     * @return the parsed {@code Short}, or {@code null} if parsing fails
     */
    public static Short tryParseShort(String input, int radix) {
        try {
            return Short.parseShort(input, radix);
        } catch (NumberFormatException ignore) { }

        return null;
    }

    /**
     * Converts a string value into a {@link Number}.
     *
     * <p>
     * If the string starts with {@code 0x} or {@code -0x} (lower or upper case)
     * or {@code #} or {@code -#}, it will be interpreted as a hexadecimal
     * Integer - or Long, if the number of digits after the prefix is more than
     * 8 - or BigInteger if there are more than 16 digits.
     *
     * <p>
     * Then, the value is examined for a type qualifier on the end, i.e. one of
     * {@code 'f', 'F', 'd', 'D', 'l', 'L'}. If it is found, it starts
     * trying to create successively larger types from the type specified
     * until one is found that can represent the value.
     *
     * <p>
     * If a type specifier is not found, it will check for a decimal point
     * and then try successively larger types from {@code Integer} to
     * {@code BigInteger} and from {@code Float} to {@code BigDecimal}.
     *
     * <p>
     * Integral values with a leading {@code 0} will be interpreted as octal;
     * the returned number will be Integer, Long or BigDecimal as appropriate.
     *
     * <p>
     * Returns {@code null} if the string is {@code null}. This method does
     * not trim the input string, i.e., strings with leading or trailing spaces
     * will generate NumberFormatExceptions.
     *
     * @param str the string containing a number, may be null
     * @return the number created from the string, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Number createNumber(final String str) {
        if (str == null) {
            return null;
        }
        if (StringUtil.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        // Need to deal with all possible hex prefixes here
        final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
        int pfxLen = 0;
        for (final String pfx : hex_prefixes) {
            if (str.startsWith(pfx)) {
                pfxLen += pfx.length();
                break;
            }
        }
        if (pfxLen > 0) { // we have a hex number
            char firstSigDigit = 0; // strip leading zeroes
            for (int i = pfxLen; i < str.length(); i++) {
                firstSigDigit = str.charAt(i);
                if (firstSigDigit == '0') { // count leading zeroes
                    pfxLen++;
                } else {
                    break;
                }
            }
            final int hexDigits = str.length() - pfxLen;
            if (hexDigits > 16 || hexDigits == 16 && firstSigDigit > '7') { // too many for Long
                return createBigInteger(str);
            }
            if (hexDigits > 8 || hexDigits == 8 && firstSigDigit > '7') { // too many for an int
                return createLong(str);
            }
            return createInteger(str);
        }
        final char lastChar = str.charAt(str.length() - 1);
        String mant;
        @Nullable String dec;
        @Nullable String exp;
        final int decPos = str.indexOf('.');
        final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
        // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
        // and the parsing which will detect if e or E appear in a number due to using the wrong offset

        if (decPos > -1) { // there is a decimal point
            if (expPos > -1) { // there is an exponent
                if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number");
                }
                dec = str.substring(decPos + 1, expPos);
            } else {
                dec = str.substring(decPos + 1);
            }
            mant = getMantissa(str, decPos);
        } else {
            if (expPos > -1) {
                if (expPos > str.length()) { // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number");
                }
                mant = getMantissa(str, expPos);
            } else {
                mant = getMantissa(str);
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar) && lastChar != '.') {
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring(expPos + 1, str.length() - 1);
            } else {
                exp = null;
            }
            //Requesting a specific type..
            final String numeric = str.substring(0, str.length() - 1);
            final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'l' :
                case 'L' :
                    if (dec == null
                            && exp == null
                            && (!numeric.isEmpty() && numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                        try {
                            return createLong(numeric);
                        } catch (final NumberFormatException nfe) { // NOPMD
                            // Too big for a long
                        }
                        return createBigInteger(numeric);

                    }
                    throw new NumberFormatException(str + " is not a valid number");
                case 'f' :
                case 'F' :
                    try {
                        final Float f = createFloat(str);
                        if (!(f.isInfinite() || f == 0.0F && !allZeros)) {
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float does not have the precision we want
                            return f;
                        }

                    } catch (final NumberFormatException nfe) { // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                case 'd' :
                case 'D' :
                    try {
                        final Double d = createDouble(str);
                        if (!(d.isInfinite() || d.floatValue() == 0.0D && !allZeros)) {
                            return d;
                        }
                    } catch (final NumberFormatException nfe) { // NOPMD
                        // ignore the bad number
                    }
                    try {
                        return createBigDecimal(numeric);
                    } catch (final NumberFormatException e) { // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                default :
                    throw new NumberFormatException(str + " is not a valid number");

            }
        }
        //User doesn't have a preference on the return type, so let's start
        //small and go from there...
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1);
        } else {
            exp = null;
        }
        if (dec == null && exp == null) { // no decimal point and no exponent
            //Must be an Integer, Long, Biginteger
            try {
                return createInteger(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            try {
                return createLong(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            return createBigInteger(str);
        }

        //Must be a Float, Double, BigDecimal
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        try {
            final Float f = createFloat(str);
            final Double d = createDouble(str);
            if (!f.isInfinite()
                    && !(f == 0.0F && !allZeros)
                    && f.toString().equals(d.toString())) {
                return f;
            }
            if (!d.isInfinite() && !(d == 0.0D && !allZeros)) {
                final BigDecimal b = createBigDecimal(str);
                if (b.compareTo(BigDecimal.valueOf(d)) == 0) {
                    return d;
                }
                return b;
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        return createBigDecimal(str);
    }

    /**
     * Returns the mantissa of the given number string.
     *
     * @param str the string representation of the number
     * @return the mantissa portion of the number
     * @see #createNumber(String)
     */
    private static String getMantissa(final String str) {
        return getMantissa(str, str.length());
    }

    /**
     * Returns the mantissa of the given number string up to the specified stop position.
     *
     * @param str the string representation of the number
     * @param stopPos the position of the exponent or decimal point
     * @return the mantissa portion of the number
     * @see #createNumber(String)
     */
    private static String getMantissa(final String str, final int stopPos) {
        final char firstChar = str.charAt(0);
        final boolean hasSign = firstChar == '-' || firstChar == '+';

        return hasSign ? str.substring(1, stopPos) : str.substring(0, stopPos);
    }

    /**
     * Checks whether the string contains only zero characters, or is {@code null}.
     *
     * @param str the string to check
     * @return {@code true} if the string is null or contains only zeros
     * @see #createNumber(String)
     */
    private static boolean isAllZeros(final String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return !str.isEmpty();
    }

    /**
     * Converts a {@code String} to a {@code Float}.
     * Returns {@code null} if the string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code Float}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Float createFloat(final String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }

    /**
     * Converts a {@code String} to a {@code Double}.
     * Returns {@code null} if the string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code Double}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Double createDouble(final String str) {
        if (str == null) {
            return null;
        }
        return Double.valueOf(str);
    }

    /**
     * Converts a {@code String} to an {@code Integer}, handling hex
     * ({@code 0xhhhh}) and octal ({@code 0dddd}) notations. A leading zero
     * means octal; spaces are not trimmed. Returns {@code null} if the
     * string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code Integer}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Integer createInteger(final String str) {
        if (str == null) {
            return null;
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return Integer.decode(str);
    }

    /**
     * Converts a {@code String} to a {@code Long}, handling hex
     * ({@code 0Xhhhh}) and octal ({@code 0ddd}) notations. A leading zero
     * means octal; spaces are not trimmed. Returns {@code null} if the
     * string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code Long}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Long createLong(final String str) {
        if (str == null) {
            return null;
        }
        return Long.decode(str);
    }

    /**
     * Converts a {@code String} to a {@code BigInteger}, handling hex
     * ({@code 0x} or {@code #}) and octal ({@code 0}) notations. Returns
     * {@code null} if the string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code BigInteger}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigInteger createBigInteger(final String str) {
        if (str == null) {
            return null;
        }
        int pos = 0; // offset within string
        int radix = 10;
        boolean negate = false; // need to negate later?
        if (str.startsWith("-")) {
            negate = true;
            pos = 1;
        }
        if (str.startsWith("0x", pos) || str.startsWith("0X", pos)) { // hex
            radix = 16;
            pos += 2;
        } else if (str.startsWith("#", pos)) { // alternative hex (allowed by Long/Integer)
            radix = 16;
            pos++;
        } else if (str.startsWith("0", pos) && str.length() > pos + 1) { // octal; so long as there are additional digits
            radix = 8;
            pos++;
        } // default is to treat as decimal

        final BigInteger value = new BigInteger(str.substring(pos), radix);
        return negate ? value.negate() : value;
    }

    /**
     * Converts a {@code String} to a {@code BigDecimal}. Returns
     * {@code null} if the string is {@code null}.
     *
     * @param str a {@code String} to convert, may be null
     * @return converted {@code BigDecimal}, or null if the input is null
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(final String str) {
        if (str == null) {
            return null;
        }
        if (StringUtil.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(str);
    }

    /**
     * Returns the minimum value in an array.
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is null or empty
     */
    public static long min(final long... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is null or empty
     */
    public static int min(final int... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is null or empty
     */
    public static short min(final short... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is null or empty
     */
    public static byte min(final byte... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * Returns the minimum value in an array.
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is null or empty
     */
    public static double min(final double... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.isNaN(array[i])) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    /**
     * <p>Returns the minimum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static float min(final float... array) {
        // Validates input
        validateArray(array);

        // Finds and returns min
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    // Max in array
    //--------------------------------------------------------------------
    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty Changed signature from max(long[]) to max(long...)
     */
    public static long max(final long... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty Changed signature from max(int[]) to max(int...)
     */
    public static int max(final int... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty Changed signature from max(short[]) to max(short...)
     */
    public static short max(final short... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty Changed signature from max(byte[]) to max(byte...)
     */
    public static byte max(final byte... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static double max(final double... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     *
     * @param array an array, must not be null or empty
     * @return the maximum value in the array
     * @throws IllegalArgumentException if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array} is empty
     */
    public static float max(final float... array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Checks if the specified array is neither null nor empty.
     *
     * @param array the array to check
     * @throws IllegalArgumentException if {@code array} is either {@code null} or empty
     */
    private static void validateArray(final Object array) {
        Preconditions.checkNotNull(array, "The Array must not be null.");
        Preconditions.checkArgument(Array.getLength(array) != 0, "Array cannot be empty.");
    }

    // 3 param min
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the minimum of three {@code long} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static long min(long a, final long b, final long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three {@code int} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static int min(int a, final int b, final int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three {@code short} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static short min(short a, final short b, final short c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three {@code byte} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static byte min(byte a, final byte b, final byte c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three {@code double} values.</p>
     *
     * <p>If any value is {@code NaN}, {@code NaN} is
     * returned. Infinity is handled.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static double min(final double a, final double b, final double c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * <p>Gets the minimum of three {@code float} values.</p>
     *
     * <p>If any value is {@code NaN}, {@code NaN} is
     * returned. Infinity is handled.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the smallest of the values
     */
    public static float min(final float a, final float b, final float c) {
        return Math.min(Math.min(a, b), c);
    }

    // 3 param max
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the maximum of three {@code long} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static long max(long a, final long b, final long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three {@code int} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static int max(int a, final int b, final int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three {@code short} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static short max(short a, final short b, final short c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three {@code byte} values.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static byte max(byte a, final byte b, final byte c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three {@code double} values.</p>
     *
     * <p>If any value is {@code NaN}, {@code NaN} is
     * returned. Infinity is handled.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static double max(final double a, final double b, final double c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * <p>Gets the maximum of three {@code float} values.</p>
     *
     * <p>If any value is {@code NaN}, {@code NaN} is
     * returned. Infinity is handled.</p>
     *
     * @param a value 1
     * @param b value 2
     * @param c value 3
     * @return  the largest of the values
     */
    public static float max(final float a, final float b, final float c) {
        return Math.max(Math.max(a, b), c);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks whether the {@code String} contains only
     * digit characters.</p>
     *
     * <p>{@code Null} and empty String will return
     * {@code false}.</p>
     *
     * @param str the {@code String} to check
     * @return {@code true} if str contains only Unicode numeric
     */
    public static boolean isDigits(final String str) {
        return StringUtil.isNumeric(str);
    }

    /**
     * <p>Checks whether the String a valid Java number.</p>
     *
     * <p>Valid numbers include hexadecimal marked with the {@code 0x} or
     * {@code 0X} qualifier, octal numbers, scientific notation and
     * numbers marked with a type qualifier (e.g. 123L).</p>
     *
     * <p>Non-hexadecimal strings beginning with a leading zero are
     * treated as octal values. Thus the string {@code 09} will return
     * {@code false}, since {@code 9} is not a valid octal value.
     * However, numbers beginning with {@code 0.} are treated as decimal.</p>
     *
     * <p>{@code null} and empty/blank {@code String} will return
     * {@code false}.</p>
     *
     * <p>Note, {@link #createNumber(String)} should return a number for every
     * input resulting in {@code true}.</p>
     *
     * @param str the {@code String} to check
     * @return {@code true} if the string is a correctly formatted number
     */
    public static boolean isCreatable(final String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && !StringUtil.contains(str, '.')) { // leading 0, skip if is a decimal number
            if (chars[start + 1] == 'x' || chars[start + 1] == 'X') { // leading 0x/0X
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            } else if (Character.isDigit(chars[start + 1])) {
                // leading 0, but not hex, must be octal
                int i = start + 1;
                for (; i < chars.length; i++) {
                    if (chars[i] < '0' || chars[i] > '7') {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

    /**
     * <p>Checks whether the given String is a parsable number.</p>
     *
     * <p>Parsable numbers include those Strings understood by {@link Integer#parseInt(String)},
     * {@link Long#parseLong(String)}, {@link Float#parseFloat(String)} or
     * {@link Double#parseDouble(String)}. This method can be used instead of catching {@link java.text.ParseException}
     * when calling one of those methods.</p>
     *
     * <p>Hexadecimal and scientific notations are <strong>not</strong> considered parsable.
     * See {@link #isCreatable(String)} on those cases.</p>
     *
     * <p>{@code Null} and empty String will return {@code false}.</p>
     *
     * @param str the String to check.
     * @return {@code true} if the string is a parsable number.
     */
    public static boolean isParsable(final String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        if (str.charAt(str.length() - 1) == '.') {
            return false;
        }
        if (str.charAt(0) == '-') {
            if (str.length() == 1) {
                return false;
            }
            return withDecimalsParsing(str, 1);
        }
        return withDecimalsParsing(str, 0);
    }

    private static boolean withDecimalsParsing(final String str, final int beginIdx) {
        int decimalPoints = 0;
        for (int i = beginIdx; i < str.length(); i++) {
            final boolean isDecimalPoint = str.charAt(i) == '.';
            if (isDecimalPoint) {
                decimalPoints++;
            }
            if (decimalPoints > 1) {
                return false;
            }
            if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Compares two {@code int} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code int} to compare
     * @param y the second {@code int} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final int x, final int y) {
        if (x == y) {
            return 0;
        }
        return x < y ? -1 : 1;
    }

    /**
     * <p>Compares to {@code long} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code long} to compare
     * @param y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final long x, final long y) {
        if (x == y) {
            return 0;
        }
        return x < y ? -1 : 1;
    }

    /**
     * <p>Compares to {@code short} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code short} to compare
     * @param y the second {@code short} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final short x, final short y) {
        if (x == y) {
            return 0;
        }
        return x < y ? -1 : 1;
    }

    /**
     * <p>Compares two {@code byte} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code byte} to compare
     * @param y the second {@code byte} to compare
     * @return the value {@code 0} if {@code x == y};
     *         a value less than {@code 0} if {@code x < y}; and
     *         a value greater than {@code 0} if {@code x > y}
     */
    public static int compare(final byte x, final byte y) {
        return x - y;
    }
}

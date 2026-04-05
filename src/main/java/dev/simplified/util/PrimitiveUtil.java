package dev.simplified.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility methods for primitive types, their corresponding wrapper types, and array conversions
 * between the two.
 */
@SuppressWarnings("all")
@UtilityClass
public final class PrimitiveUtil {

    /** Immutable map from each primitive type to its corresponding wrapper type. */
    @Getter private static final Map<Class<?>, Class<?>> primitiveToWrapperTypes;

    /** Immutable map from each wrapper type to its corresponding primitive type. */
    @Getter private static final Map<Class<?>, Class<?>> wrapperToPrimitiveTypes;

    /** Map from primitive class name to its corresponding abbreviation used in array class names. */
    @Getter private static final Map<String, String> abbreviationMap = new HashMap<>();

    /** Map from array class name abbreviation to its corresponding primitive class name. */
    @Getter private static final Map<String, String> reverseAbbreviationMap = new HashMap<>();

    /**
     * Registers a primitive type abbreviation into both the forward and reverse abbreviation maps.
     *
     * @param primitive the canonical name of the primitive type
     * @param abbreviation the corresponding abbreviation of the primitive type
     */
    private static void addAbbreviation(String primitive, String abbreviation) {
        abbreviationMap.put(primitive, abbreviation);
        reverseAbbreviationMap.put(abbreviation, primitive);
    }

    static {
        Map<Class<?>, Class<?>> primToWrap = new LinkedHashMap<>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new LinkedHashMap<>(16);

        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        primitiveToWrapperTypes = Collections.unmodifiableMap(primToWrap);
        wrapperToPrimitiveTypes = Collections.unmodifiableMap(wrapToPrim);

        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }

    private static void add(
            Map<Class<?>, Class<?>> forward,
            Map<Class<?>, Class<?>> backward,
            Class<?> key,
            Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    /**
     * Checks whether {@code type} is one of the nine primitive-wrapper types, such as {@link Integer}.
     *
     * @param type the class to check
     * @return {@code true} if the type is a primitive wrapper
     * @see Class#isPrimitive
     */
    public static boolean isWrapperType(@NotNull Class<?> type) {
        return wrapperToPrimitiveTypes.containsKey(type);
    }

    /**
     * Returns the corresponding wrapper type of {@code type} if it is a primitive type; otherwise
     * returns {@code type} itself. Idempotent.
     *
     * <pre>
     *     wrap(int.class) == Integer.class
     *     wrap(Integer.class) == Integer.class
     *     wrap(String.class) == String.class
     * </pre>
     *
     * @param <T>  the type
     * @param type the class to wrap
     * @return the wrapper type, or the original type if not primitive
     */
    public static <T> Class<T> wrap(@NotNull Class<T> type) {
        Class<T> wrapped = (Class<T>) primitiveToWrapperTypes.get(type);
        return (wrapped == null) ? type : wrapped;
    }

    /**
     * Returns the corresponding primitive type of {@code type} if it is a wrapper type; otherwise
     * returns {@code type} itself. Idempotent.
     *
     * <pre>
     *     unwrap(Integer.class) == int.class
     *     unwrap(int.class) == int.class
     *     unwrap(String.class) == String.class
     * </pre>
     *
     * @param <T>  the type
     * @param type the class to unwrap
     * @return the primitive type, or the original type if not a wrapper
     */
    public static <T> Class<T> unwrap(@NotNull Class<T> type) {
        Class<T> unwrapped = (Class<T>) wrapperToPrimitiveTypes.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }

    /**
     * Converts an array of primitive booleans to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code boolean} array
     * @return a {@code Boolean} array, {@code null} if null array input
     */
    public static Boolean[] wrap(final boolean[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BOOLEAN_OBJECT_ARRAY;
        
        final Boolean[] result = new Boolean[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = (array[i] ? Boolean.TRUE : Boolean.FALSE);
        
        return result;
    }

    /**
     * Converts an array of primitive bytes to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code byte} array
     * @return a {@code Byte} array, {@code null} if null array input
     */
    public static Byte[] wrap(final byte[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BYTE_OBJECT_ARRAY;
        
        final Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive chars to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code char} array
     * @return a {@code Character} array, {@code null} if null array input
     */
    public static Character[] wrap(final char[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_CHARACTER_OBJECT_ARRAY;
        
        final Character[] result = new Character[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive doubles to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code double} array
     * @return a {@code Double} array, {@code null} if null array input
     */
    public static Double[] wrap(final double[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_DOUBLE_OBJECT_ARRAY;
        
        final Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive floats to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code float} array
     * @return a {@code Float} array, {@code null} if null array input
     */
    public static Float[] wrap(final float[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_FLOAT_OBJECT_ARRAY;
        
        final Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive ints to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array an {@code int} array
     * @return an {@code Integer} array, {@code null} if null array input
     */
    public static Integer[] wrap(final int[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_INTEGER_OBJECT_ARRAY;
        
        final Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive longs to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code long} array
     * @return a {@code Long} array, {@code null} if null array input
     */
    public static Long[] wrap(final long[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_LONG_OBJECT_ARRAY;
        
        final Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of primitive shorts to objects.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code short} array
     * @return a {@code Short} array, {@code null} if null array input
     */
    public static Short[] wrap(final short[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_SHORT_OBJECT_ARRAY;
        
        final Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        
        return result;
    }

    /**
     * Converts an array of {@link Boolean} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Boolean} array, may be {@code null}
     * @return a {@code boolean} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static boolean[] unwrap(final Boolean[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BOOLEAN_ARRAY;

        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].booleanValue();

        return result;
    }

    /**
     * Converts an array of {@link Boolean} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Boolean} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code boolean} array, {@code null} if null array input
     */
    public static boolean[] unwrap(final Boolean[] array, final boolean valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BOOLEAN_ARRAY;

        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            final Boolean b = array[i];
            result[i] = (b == null ? valueForNull : b.booleanValue());
        }
        return result;
    }

    /**
     * Converts an array of {@link Byte} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Byte} array, may be {@code null}
     * @return a {@code byte} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static byte[] unwrap(final Byte[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BYTE_ARRAY;

        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].byteValue();

        return result;
    }

    /**
     * Converts an array of {@link Byte} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Byte} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code byte} array, {@code null} if null array input
     */
    public static byte[] unwrap(final Byte[] array, final byte valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_BYTE_ARRAY;

        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            final Byte b = array[i];
            result[i] = (b == null ? valueForNull : b.byteValue());
        }

        return result;
    }

    /**
     * Converts an array of {@link Character} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Character} array, may be {@code null}
     * @return a {@code char} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static char[] unwrap(final Character[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_CHAR_ARRAY;

        final char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].charValue();

        return result;
    }

    /**
     * Converts an array of {@link Character} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Character} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code char} array, {@code null} if null array input
     */
    public static char[] unwrap(final Character[] array, final char valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_CHAR_ARRAY;

        final char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            final Character b = array[i];
            result[i] = (b == null ? valueForNull : b.charValue());
        }

        return result;
    }

    /**
     * Converts an array of {@link Double} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Double} array, may be {@code null}
     * @return a {@code double} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static double[] unwrap(final Double[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_DOUBLE_ARRAY;

        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].doubleValue();

        return result;
    }

    /**
     * Converts an array of {@link Double} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Double} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code double} array, {@code null} if null array input
     */
    public static double[] unwrap(final Double[] array, final double valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_DOUBLE_ARRAY;

        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            final Double b = array[i];
            result[i] = (b == null ? valueForNull : b.doubleValue());
        }

        return result;
    }

    /**
     * Converts an array of {@link Float} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Float} array, may be {@code null}
     * @return a {@code float} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static float[] unwrap(final Float[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_FLOAT_ARRAY;

        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].floatValue();

        return result;
    }

    /**
     * Converts an array of {@link Float} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Float} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code float} array, {@code null} if null array input
     */
    public static float[] unwrap(final Float[] array, final float valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_FLOAT_ARRAY;

        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            final Float b = array[i];
            result[i] = (b == null ? valueForNull : b.floatValue());
        }

        return result;
    }

    /**
     * Converts an array of {@link Integer} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array an {@code Integer} array, may be {@code null}
     * @return an {@code int} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static int[] unwrap(final Integer[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_INT_ARRAY;

        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].intValue();

        return result;
    }

    /**
     * Converts an array of {@link Integer} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array an {@code Integer} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return an {@code int} array, {@code null} if null array input
     */
    public static int[] unwrap(final Integer[] array, final int valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_INT_ARRAY;

        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            Integer b = array[i];
            result[i] = (b == null ? valueForNull : b.intValue());
        }

        return result;
    }

    /**
     * Converts an array of {@link Long} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Long} array, may be {@code null}
     * @return a {@code long} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static long[] unwrap(final Long[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_LONG_ARRAY;

        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].longValue();

        return result;
    }

    /**
     * Converts an array of {@link Long} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Long} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code long} array, {@code null} if null array input
     */
    public static long[] unwrap(final Long[] array, final long valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_LONG_ARRAY;

        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            final Long b = array[i];
            result[i] = (b == null ? valueForNull : b.longValue());
        }

        return result;
    }

    /**
     * Creates an array of the corresponding primitive type from an array of wrapper objects.
     * Returns {@code null} for a {@code null} input array. If the component type is not a
     * recognized wrapper type, the original array is returned unchanged.
     *
     * @param array an array of wrapper objects
     * @return an array of the corresponding primitive type, or the original array
     */
    public static Object unwrap(final Object array) {
        if (array == null)
            return null;

        final Class<?> ct = array.getClass().getComponentType();
        final Class<?> pt = unwrap(ct);

        if (Integer.TYPE.equals(pt))
            return unwrap((Integer[]) array);
        else if (Long.TYPE.equals(pt))
            return unwrap((Long[]) array);
        else if (Short.TYPE.equals(pt))
            return unwrap((Short[]) array);
        else if (Double.TYPE.equals(pt))
            return unwrap((Double[]) array);
        else if (Float.TYPE.equals(pt))
            return unwrap((Float[]) array);

        return array;
    }

    /**
     * Converts an array of {@link Short} objects to primitives.
     * Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Short} array, may be {@code null}
     * @return a {@code short} array, {@code null} if null array input
     * @throws NullPointerException if any array element is {@code null}
     */
    public static short[] unwrap(final Short[] array) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_SHORT_ARRAY;

        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].shortValue();

        return result;
    }

    /**
     * Converts an array of {@link Short} objects to primitives, substituting a default value
     * for {@code null} elements. Returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Short} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return a {@code short} array, {@code null} if null array input
     */
    public static short[] unwrap(final Short[] array, final short valueForNull) {
        if (array == null)
            return null;
        else if (array.length == 0)
            return ArrayUtil.EMPTY_SHORT_ARRAY;

        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            final Short b = array[i];
            result[i] = (b == null ? valueForNull : b.shortValue());
        }

        return result;
    }

}

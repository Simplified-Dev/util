package dev.sbs.api.util;

import dev.sbs.api.builder.HashCodeBuilder;
import dev.sbs.api.util.mutable.MutableInt;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * A utility class for operations on arrays, primitive arrays (like {@code int[]}) and
 * primitive wrapper arrays (like {@code Integer[]}).
 *
 * <p>
 * This class tries to handle {@code null} input gracefully. An exception will not be thrown for a
 * {@code null} array input. However, an Object array that contains a {@code null} element may throw
 * an exception. Each method documents its behavior.
 */
@SuppressWarnings("unchecked")
@UtilityClass
public final class ArrayUtil {

    /**
     * An empty immutable {@code boolean} array.
     */
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    /**
     * An empty immutable {@code Boolean} array.
     */
    public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

    /**
     * An empty immutable {@code byte} array.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * An empty immutable {@code Byte} array.
     */
    public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

    /**
     * An empty immutable {@code char} array.
     */
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    /**
     * An empty immutable {@code Character} array.
     */
    public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

    /**
     * An empty immutable {@code Class} array.
     */
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    /**
     * An empty immutable {@code double} array.
     */
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    /**
     * An empty immutable {@code Double} array.
     */
    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];

    /**
     * An empty immutable {@code Field} array.
     */
    public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

    /**
     * An empty immutable {@code float} array.
     */
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    /**
     * An empty immutable {@code Float} array.
     */
    public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

    /**
     * An empty immutable {@code int} array.
     */
    public static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
     * An empty immutable {@code Integer} array.
     */
    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

    /**
     * An empty immutable {@code long} array.
     */
    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    /**
     * An empty immutable {@code Long} array.
     */
    public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

    /**
     * An empty immutable {@code Method} array.
     */
    public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

    /**
     * An empty immutable {@code Object} array.
     */
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * An empty immutable {@code short} array.
     */
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];

    /**
     * An empty immutable {@code Short} array.
     */
    public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

    /**
     * An empty immutable {@code String} array.
     */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * An empty immutable {@code Throwable} array.
     */
    public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

    /**
     * An empty immutable {@code Type} array.
     */
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    /**
     * The index value when an element is not found in a list or array: {@code -1}.
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static boolean[] add(final boolean[] array, final boolean element) {
        final boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, boolean[], boolean...)}
     */
    @Deprecated
    public static boolean[] add(final boolean[] array, final int index, final boolean element) {
        return (boolean[]) add(array, index, element, Boolean.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static byte[] add(final byte[] array, final byte element) {
        final byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, byte[], byte...)}
     */
    @Deprecated
    public static byte[] add(final byte[] array, final int index, final byte element) {
        return (byte[]) add(array, index, element, Byte.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static char[] add(final char[] array, final char element) {
        final char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, char[], char...)}
     */
    @Deprecated
    public static char[] add(final char[] array, final int index, final char element) {
        return (char[]) add(array, index, element, Character.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static double[] add(final double[] array, final double element) {
        final double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, double[], double...)}
     */
    @Deprecated
    public static double[] add(final double[] array, final int index, final double element) {
        return (double[]) add(array, index, element, Double.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static float[] add(final float[] array, final float element) {
        final float[] newArray = (float[]) copyArrayGrow1(array, Float.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, float[], float...)}
     */
    @Deprecated
    public static float[] add(final float[] array, final int index, final float element) {
        return (float[]) add(array, index, element, Float.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static int[] add(final int[] array, final int element) {
        final int[] newArray = (int[]) copyArrayGrow1(array, Integer.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, int[], int...)}
     */
    @Deprecated
    public static int[] add(final int[] array, final int index, final int element) {
        return (int[]) add(array, index, element, Integer.TYPE);
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, long[], long...)}
     */
    @Deprecated
    public static long[] add(final long[] array, final int index, final long element) {
        return (long[]) add(array, index, element, Long.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static long[] add(final long[] array, final long element) {
        final long[] newArray = (long[]) copyArrayGrow1(array, Long.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @param clss the type of the element being added
     * @return a new array containing the existing elements and the new element
     */
    @SuppressWarnings("all")
    private static Object add(final Object array, final int index, final Object element, final Class<?> clss) {
        if (array == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
            }
            final Object joinedArray = Array.newInstance(clss, 1);
            Array.set(joinedArray, 0, element);
            return joinedArray;
        }
        final int length = Array.getLength(array);
        if (index > length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        final Object result = Array.newInstance(clss, length + 1);
        System.arraycopy(array, 0, result, 0, index);
        Array.set(result, index, element);
        if (index < length) {
            System.arraycopy(array, index, result, index + 1, length - index);
        }
        return result;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, short[], short...)}
     */
    @Deprecated
    public static short[] add(final short[] array, final int index, final short element) {
        return (short[]) add(array, index, element, Short.TYPE);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add at the last index of the new array
     * @return a new array containing the existing elements plus the new element
     */
    public static short[] add(final short[] array, final short element) {
        final short[] newArray = (short[]) copyArrayGrow1(array, Short.TYPE);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Inserts the specified element at the specified position in the array.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param array the array to add the element to, may be {@code null}
     * @param index the position of the new object
     * @param element the object to add
     * @return a new array containing the existing elements and the new element
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > array.length})
     * @deprecated superseded by {@link #insert(int, Object[], Object...)}
     */
    @Deprecated
    public static <T> T[] add(final T[] array, final int index, final T element) {
        Class<?> clss;
        if (array != null) {
            clss = array.getClass().getComponentType();
        } else if (element != null) {
            clss = element.getClass();
        } else {
            throw new IllegalArgumentException("Array and element cannot both be null");
        }
        // the add method creates an array of type clss, which is type T
        return (T[]) add(array, index, element, clss);
    }

    /**
     * Copies the given array and adds the given element at the end of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the last position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param <T> the component type of the array
     * @param array the array to copy and add the element to, may be {@code null}
     * @param element the object to add, may be {@code null}
     * @return a new array containing the existing elements plus the new element
     * @throws IllegalArgumentException if both arguments are null
     */
    public static <T> T[] add(final T[] array, final T element) {
        Class<?> type;
        if (array != null) {
            type = array.getClass().getComponentType();
        } else if (element != null) {
            type = element.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        // type must be T
        final T[] newArray = (T[]) copyArrayGrow1(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code boolean[]} array
     */
    public static boolean[] addAll(final boolean[] array1, final boolean... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final boolean[] joinedArray = new boolean[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code byte[]} array
     */
    public static byte[] addAll(final byte[] array1, final byte... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code char[]} array
     */
    public static char[] addAll(final char[] array1, final char... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final char[] joinedArray = new char[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code double[]} array
     */
    public static double[] addAll(final double[] array1, final double... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final double[] joinedArray = new double[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code float[]} array
     */
    public static float[] addAll(final float[] array1, final float... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final float[] joinedArray = new float[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code int[]} array
     */
    public static int[] addAll(final int[] array1, final int... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final int[] joinedArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code long[]} array
     */
    public static long[] addAll(final long[] array1, final long... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final long[] joinedArray = new long[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param array1 the first array whose elements are added to the new array
     * @param array2 the second array whose elements are added to the new array
     * @return the new {@code short[]} array
     */
    public static short[] addAll(final short[] array1, final short... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final short[] joinedArray = new short[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    /**
     * Adds all the elements of the given arrays into a new array.
     *
     * <p>
     * The new array contains all of the elements of {@code array1} followed by all of the
     * elements of {@code array2}. When an array is returned, it is always a new array.
     *
     * @param <T> the component type of the array
     * @param array1 the first array whose elements are added to the new array, may be {@code null}
     * @param array2 the second array whose elements are added to the new array, may be {@code null}
     * @return the new array, {@code null} if both arrays are {@code null}
     * @throws IllegalArgumentException if the array types are incompatible
     */
    public static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        // OK, because array is of type T
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                        + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static boolean[] addFirst(final boolean[] array, final boolean element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static byte[] addFirst(final byte[] array, final byte element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static char[] addFirst(final char[] array, final char element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static double[] addFirst(final double[] array, final double element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static float[] addFirst(final float[] array, final float element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static int[] addFirst(final int[] array, final int element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static long[] addFirst(final long[] array, final long element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add
     * @return a new array containing the existing elements plus the new element
     */
    public static short[] addFirst(final short[] array, final short element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Copies the given array and adds the given element at the beginning of the new array.
     *
     * <p>
     * The new array contains the same elements of the input array plus the given element
     * in the first position. The component type of the new array is the same as that of
     * the input array.
     *
     * @param <T> the component type of the array
     * @param array the array to add the element to, may be {@code null}
     * @param element the object to add, may be {@code null}
     * @return a new array containing the existing elements plus the new element
     * @throws IllegalArgumentException if both arguments are null
     */
    public static <T> T[] addFirst(final T[] array, final T element) {
        return array == null ? add(array, element) : insert(0, array, element);
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static boolean[] clone(final boolean[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static char[] clone(final char[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static double[] clone(final double[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static float[] clone(final float[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static int[] clone(final int[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static long[] clone(final long[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Clones an array returning a typecast result and handling {@code null}.
     *
     * @param array the array to clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static short[] clone(final short[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Shallow clones an array returning a typecast result and handling {@code null}.
     *
     * <p>
     * The objects in the array are not cloned, thus there is no special handling for
     * multi-dimensional arrays.
     *
     * @param <T> the component type of the array
     * @param array the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final boolean[] array, final boolean valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final byte[] array, final byte valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final char[] array, final char valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final double[] array, final double valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if a value falling within the given tolerance is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search
     * @param valueToFind the value to find
     * @param tolerance the array contains the tolerance of the search
     * @return {@code true} if value falling within tolerance is in array
     */
    public static boolean contains(final double[] array, final double valueToFind, final double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final float[] array, final float valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final int[] array, final int valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final long[] array, final long valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the object is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param objectToFind the object to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final Object[] array, final Object objectToFind) {
        return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Checks if the value is in the given array.
     *
     * <p>
     * The method returns {@code false} if a {@code null} array is passed in.
     *
     * @param array the array to search through
     * @param valueToFind the value to find
     * @return {@code true} if the array contains the object
     */
    public static boolean contains(final short[] array, final short valueToFind) {
        return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
    }

    /**
     * Returns a copy of the given array of size 1 greater than the argument.
     * The last value of the array is left to the default value.
     *
     * @param array the array to copy, must not be {@code null}
     * @param newArrayComponentType if {@code array} is {@code null}, create a size 1 array of this type
     * @return a new copy of the array of size 1 greater than the input
     */
    @SuppressWarnings("all")
    private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
        if (array != null) {
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    /**
     * Gets the n-th element of an array or {@code null} if the index is out of bounds or the array is
     * {@code null}.
     *
     * @param <T> the type of array elements
     * @param array the array to index
     * @param index the index
     * @return the n-th element of the array or {@code null} if the index is out of bounds or the
     * array is {@code null}
     */
    public static <T> T get(final T[] array, final int index) {
        return get(array, index, null);
    }

    /**
     * Gets the n-th element of an array or a default value if the index is out of bounds.
     *
     * @param <T> the type of array elements
     * @param array the array to index
     * @param index the index
     * @param defaultValue the return value if the given index is out of bounds
     * @return the n-th element of the array or the default value if the index is out of bounds
     */
    public static <T> T get(final T[] array, final int index, final T defaultValue) {
        return isArrayIndexValid(array, index) ? array[index] : defaultValue;
    }

    /**
     * Returns the length of the specified array. This method can deal with {@code Object} arrays
     * and with primitive arrays.
     *
     * <p>
     * If the input array is {@code null}, {@code 0} is returned.
     *
     * @param array the array to retrieve the length from, may be {@code null}
     * @return the length of the array, or {@code 0} if the array is {@code null}
     * @throws IllegalArgumentException if the object argument is not an array
     */
    public static int getLength(final Object array) {
        if (array == null)
            return 0;

        return Array.getLength(array);
    }

    /**
     * Gets a hash code for an array handling multi-dimensional arrays correctly.
     *
     * @param array the array to get a hash code for, {@code null} returns zero
     * @return a hash code for the array
     */
    public static int hashCode(final Object array) {
        return new HashCodeBuilder().append(array).toHashCode();
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final boolean[] array, final boolean valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final boolean[] array, final boolean valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array, final byte valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final byte[] array, final byte valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final char[] array, final char valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final char[] array, final char valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value within a given tolerance in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param tolerance tolerance of the search
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, final double tolerance) {
        return indexOf(array, valueToFind, 0, tolerance);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array starting at the given index,
     * within a given tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @param tolerance tolerance of the search
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final double[] array, final double valueToFind, int startIndex, final double tolerance) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        final double min = valueToFind - tolerance;
        final double max = valueToFind + tolerance;
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array, final float valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final float[] array, final float valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array, final int valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final int[] array, final int valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array, final long valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final long[] array, final long valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given object in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @return the index of the object within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final Object[] array, final Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    /**
     * Finds the index of the given object in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @param startIndex the index to start searching at
     * @return the index of the object within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the index of the given value in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array, final short valueToFind) {
        return indexOf(array, valueToFind, 0);
    }

    /**
     * Finds the index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return {@link #INDEX_NOT_FOUND} ({@code -1}).
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return the index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int indexOf(final short[] array, final short valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final boolean[] array, final boolean valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final boolean[] array, final boolean valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final byte[] array, final byte valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final byte[] array, final byte valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final char[] array, final char valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final char[] array, final char valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final double[] array, final double valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value within a given tolerance in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param tolerance tolerance of the search
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final double[] array, final double valueToFind, final double tolerance) {
        return indexesOf(array, valueToFind, 0, tolerance);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final double[] array, final double valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array starting at the given index,
     * within a given tolerance.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @param tolerance tolerance of the search
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final double[] array, final double valueToFind, int startIndex, final double tolerance) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex, tolerance);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final float[] array, final float valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final float[] array, final float valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final int[] array, final int valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final int[] array, final int valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final long[] array, final long valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final long[] array, final long valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given object in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final Object[] array, final Object objectToFind) {
        return indexesOf(array, objectToFind, 0);
    }

    /**
     * Finds the indices of the given object in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final Object[] array, final Object objectToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, objectToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Finds the indices of the given value in the array.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final short[] array, final short valueToFind) {
        return indexesOf(array, valueToFind, 0);
    }

    /**
     * Finds the indices of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns an empty {@link BitSet} for a {@code null} input array.
     *
     * <p>
     * A negative startIndex is treated as zero. A startIndex larger than the array
     * length will return an empty {@link BitSet}.
     *
     * @param array the array to search through for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the index to start searching at
     * @return a {@link BitSet} of all the indices of the value within the array,
     * an empty {@link BitSet} if not found or {@code null} array input
     */
    public static BitSet indexesOf(final short[] array, final short valueToFind, int startIndex) {
        final BitSet bitSet = new BitSet();

        if (array == null) {
            return bitSet;
        }

        while (startIndex < array.length) {
            startIndex = indexOf(array, valueToFind, startIndex);

            if (startIndex == INDEX_NOT_FOUND) {
                break;
            }

            bitSet.set(startIndex);
            ++startIndex;
        }

        return bitSet;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static boolean[] insert(final int index, final boolean[] array, final boolean... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final boolean[] result = new boolean[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static byte[] insert(final int index, final byte[] array, final byte... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final byte[] result = new byte[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static char[] insert(final int index, final char[] array, final char... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final char[] result = new char[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static double[] insert(final int index, final double[] array, final double... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final double[] result = new double[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static float[] insert(final int index, final float[] array, final float... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final float[] result = new float[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static int[] insert(final int index, final int[] array, final int... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final int[] result = new int[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static long[] insert(final int index, final long[] array, final long... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final long[] result = new long[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    public static short[] insert(final int index, final short[] array, final short... values) {
        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final short[] result = new short[array.length + values.length];

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Inserts elements into an array at the given index (starting from zero).
     *
     * <p>
     * When an array is returned, it is always a new array.
     *
     * @param <T> the type of elements in {@code array} and {@code values}
     * @param index the position within {@code array} to insert the new values
     * @param array the array to insert the values into, may be {@code null}
     * @param values the new values to insert, may be {@code null}
     * @return the new array
     * @throws IndexOutOfBoundsException if {@code array} is provided
     * and either {@code index < 0} or {@code index > array.length}
     */
    @SafeVarargs
    public static <T> T[] insert(final int index, final T[] array, final T... values) {
        /*
         * Note on use of @SafeVarargs:
         *
         * By returning null when 'array' is null, we avoid returning the vararg
         * array to the caller. We also avoid relying on the type of the vararg
         * array, by inspecting the component type of 'array'.
         */

        if (array == null) {
            return null;
        }
        if (isEmpty(values)) {
            return clone(array);
        }
        if (index < 0 || index > array.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
        }

        final Class<?> type = array.getClass().getComponentType();
        // OK, because array and values are of type T
        final
        T[] result = (T[]) Array.newInstance(type, array.length + values.length);

        System.arraycopy(values, 0, result, index, values.length);
        if (index > 0) {
            System.arraycopy(array, 0, result, 0, index);
        }
        if (index < array.length) {
            System.arraycopy(array, index, result, index + values.length, array.length - index);
        }
        return result;
    }

    /**
     * Checks whether a given array can safely be accessed at the given index.
     *
     * @param <T> the component type of the array
     * @param array the array to inspect, may be {@code null}
     * @param index the index of the array to be inspected
     * @return whether the given index is safely-accessible in the given array
     */
    public static <T> boolean isArrayIndexValid(final T[] array, final int index) {
        return index >= 0 && getLength(array) > index;
    }

    /**
     * Checks if an array of primitive booleans is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final boolean[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive bytes is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final byte[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive chars is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final char[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive doubles is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final double[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive floats is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final float[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive ints is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final int[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive longs is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final long[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of Objects is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    /**
     * Checks if an array of primitive shorts is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    public static boolean isEmpty(final short[] array) {
        return getLength(array) == 0;
    }

    /**
     * Compares two arrays, using equals(), handling multi-dimensional arrays correctly.
     *
     * @param array1 the left hand array to compare, may be {@code null}
     * @param array2 the right hand array to compare, may be {@code null}
     * @return {@code true} if the arrays are equal
     */
    public static boolean isEquals(final Object array1, final Object array2) {
        return Objects.deepEquals(array1, array2);
    }

    /**
     * Checks if an array of primitive booleans is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final boolean[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive bytes is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final byte[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive chars is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final char[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive doubles is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final double[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive floats is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final float[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive ints is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final int[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive longs is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final long[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of primitive shorts is not empty and not {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static boolean isNotEmpty(final short[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks if an array of Objects is not empty and not {@code null}.
     *
     * @param <T> the component type of the array
     * @param array the array to test
     * @return {@code true} if the array is not empty and not {@code null}
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final boolean[] array1, final boolean[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final byte[] array1, final byte[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final char[] array1, final char[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final double[] array1, final double[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final float[] array1, final float[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final int[] array1, final int[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final long[] array1, final long[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * <p>
     * Any multi-dimensional aspects of the arrays are ignored.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final Object array1, final Object array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * <p>
     * Any multi-dimensional aspects of the arrays are ignored.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final Object[] array1, final Object[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same length, treating {@code null} arrays as length {@code 0}.
     *
     * @param array1 the first array, may be {@code null}
     * @param array2 the second array, may be {@code null}
     * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
     */
    public static boolean isSameLength(final short[] array1, final short[] array2) {
        return getLength(array1) == getLength(array2);
    }

    /**
     * Checks whether two arrays are the same type taking into account multi-dimensional arrays.
     *
     * @param array1 the first array, must not be {@code null}
     * @param array2 the second array, must not be {@code null}
     * @return {@code true} if type of arrays matches
     * @throws IllegalArgumentException if either array is {@code null}
     */
    public static boolean isSameType(final Object array1, final Object array2) {
        if (array1 == null || array2 == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        return array1.getClass().getName().equals(array2.getClass().getName());
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering
     * ({@code false} before {@code true}).
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final boolean[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        boolean previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final boolean current = array[i];
            if (Boolean.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final byte[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        byte previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final byte current = array[i];
            if (NumberUtil.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final char[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        char previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final char current = array[i];
            if (CharUtil.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final double[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        double previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final double current = array[i];
            if (Double.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final float[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        float previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final float current = array[i];
            if (Float.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final int[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        int previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final int current = array[i];
            if (NumberUtil.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final long[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        long previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final long current = array[i];
            if (NumberUtil.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to natural ordering.
     *
     * @param array the array to check
     * @return whether the array is sorted according to natural ordering
     */
    public static boolean isSorted(final short[] array) {
        if (array == null || array.length < 2) {
            return true;
        }

        short previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final short current = array[i];
            if (NumberUtil.compare(previous, current) > 0) {
                return false;
            }

            previous = current;
        }
        return true;
    }

    /**
     * Checks whether the provided array is sorted according to the class's
     * {@code compareTo} method.
     *
     * @param <T> the datatype of the array to check, it must implement {@link Comparable}
     * @param array the array to check
     * @return whether the array is sorted
     */
    public static <T extends Comparable<? super T>> boolean isSorted(final T[] array) {
        return isSorted(array, Comparable::compareTo);
    }

    /**
     * Checks whether the provided array is sorted according to the provided {@link Comparator}.
     *
     * @param <T> the datatype of the array
     * @param array the array to check
     * @param comparator the {@link Comparator} to compare over
     * @return whether the array is sorted
     */
    public static <T> boolean isSorted(final T[] array, final Comparator<T> comparator) {
        if (comparator == null)
            throw new IllegalArgumentException("Comparator should not be null");

        if (array == null || array.length < 2)
            return true;

        T previous = array[0];
        final int n = array.length;
        for (int i = 1; i < n; i++) {
            final T current = array[i];

            if (comparator.compare(previous, current) > 0)
                return false;

            previous = current;
        }

        return true;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final boolean[] array, final boolean valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final boolean[] array, final boolean valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final byte[] array, final byte valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final byte[] array, final byte valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final char[] array, final char valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final char[] array, final char valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array, final double valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value within a given tolerance in the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param tolerance tolerance of the search
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array, final double valueToFind, final double tolerance) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE, tolerance);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array, final double valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value in the array starting at the given index,
     * within a given tolerance.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @param tolerance tolerance of the search
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final double[] array, final double valueToFind, int startIndex, final double tolerance) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        final double min = valueToFind - tolerance;
        final double max = valueToFind + tolerance;
        for (int i = startIndex; i >= 0; i--) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final float[] array, final float valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final float[] array, final float valueToFind, int startIndex) {
        if (isEmpty(array)) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final int[] array, final int valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final int[] array, final int valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final long[] array, final long valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final long[] array, final long valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given object within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @return the last index of the object within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final Object[] array, final Object objectToFind) {
        return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given object in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param objectToFind the object to find, may be {@code null}
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the object within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final Object[] array, final Object objectToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
            for (int i = startIndex; i >= 0; i--) {
                if (objectToFind.equals(array[i])) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Finds the last index of the given value within the array.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final short[] array, final short valueToFind) {
        return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
    }

    /**
     * Finds the last index of the given value in the array starting at the given index.
     *
     * <p>
     * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
     *
     * <p>
     * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex
     * larger than the array length will search from the end of the array.
     *
     * @param array the array to traverse backwards looking for the object, may be {@code null}
     * @param valueToFind the value to find
     * @param startIndex the start index to traverse backwards from
     * @return the last index of the value within the array,
     * {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
     */
    public static int lastIndexOf(final short[] array, final short valueToFind, int startIndex) {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else if (startIndex >= array.length) {
            startIndex = array.length - 1;
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static boolean[] nullToEmpty(final boolean[] array) {
        if (isEmpty(array)) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Boolean[] nullToEmpty(final Boolean[] array) {
        if (isEmpty(array)) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static byte[] nullToEmpty(final byte[] array) {
        if (isEmpty(array)) {
            return EMPTY_BYTE_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Byte[] nullToEmpty(final Byte[] array) {
        if (isEmpty(array)) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static char[] nullToEmpty(final char[] array) {
        if (isEmpty(array)) {
            return EMPTY_CHAR_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Character[] nullToEmpty(final Character[] array) {
        if (isEmpty(array)) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Class<?>[] nullToEmpty(final Class<?>[] array) {
        if (isEmpty(array)) {
            return EMPTY_CLASS_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static double[] nullToEmpty(final double[] array) {
        if (isEmpty(array)) {
            return EMPTY_DOUBLE_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Double[] nullToEmpty(final Double[] array) {
        if (isEmpty(array)) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static float[] nullToEmpty(final float[] array) {
        if (isEmpty(array)) {
            return EMPTY_FLOAT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Float[] nullToEmpty(final Float[] array) {
        if (isEmpty(array)) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static int[] nullToEmpty(final int[] array) {
        if (isEmpty(array)) {
            return EMPTY_INT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Integer[] nullToEmpty(final Integer[] array) {
        if (isEmpty(array)) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static long[] nullToEmpty(final long[] array) {
        if (isEmpty(array)) {
            return EMPTY_LONG_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Long[] nullToEmpty(final Long[] array) {
        if (isEmpty(array)) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Object[] nullToEmpty(final Object[] array) {
        if (isEmpty(array)) {
            return EMPTY_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static short[] nullToEmpty(final short[] array) {
        if (isEmpty(array)) {
            return EMPTY_SHORT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static Short[] nullToEmpty(final Short[] array) {
        if (isEmpty(array)) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array.
     *
     * @param array the array to check for {@code null} or empty, may be {@code null}
     * @return the same array, empty array if {@code null} or empty input
     */
    public static String[] nullToEmpty(final String[] array) {
        if (isEmpty(array)) {
            return EMPTY_STRING_ARRAY;
        }
        return array;
    }

    /**
     * Converts a {@code null} or empty array to an empty array of the specified type.
     *
     * @param <T> the component type of the array
     * @param array the array to check, may be {@code null}
     * @param type the class type of the array
     * @return the same array, an empty array if {@code null}
     */
    public static <T> T[] nullToEmpty(final T[] array, final Class<T[]> type) {
        if (type == null) {
            throw new IllegalArgumentException("The type must not be null");
        }

        if (array == null) {
            return type.cast(Array.newInstance(type.getComponentType(), 0));
        }
        return array;
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static boolean[] remove(final boolean[] array, final int index) {
        return (boolean[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static byte[] remove(final byte[] array, final int index) {
        return (byte[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static char[] remove(final char[] array, final int index) {
        return (char[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static double[] remove(final double[] array, final int index) {
        return (double[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static float[] remove(final float[] array, final int index) {
        return (float[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static int[] remove(final int[] array, final int index) {
        return (int[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static long[] remove(final long[] array, final int index) {
        return (long[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    @SuppressWarnings("all")
    private static Object remove(final Object array, final int index) {
        final int length = getLength(array);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }

        final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }

        return result;
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static short[] remove(final short[] array, final int index) {
        return (short[]) remove((Object) array, index);
    }

    /**
     * Removes the element at the specified position from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param <T> the component type of the array
     * @param array the array to remove the element from, may not be {@code null}
     * @param index the position of the element to be removed
     * @return a new array containing the existing elements except the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    // remove() always creates an array of the same type as its input
    public static <T> T[] remove(final T[] array, final int index) {
        return (T[]) remove((Object) array, index);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static boolean[] removeAll(final boolean[] array, final int... indices) {
        return (boolean[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static byte[] removeAll(final byte[] array, final int... indices) {
        return (byte[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static char[] removeAll(final char[] array, final int... indices) {
        return (char[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static double[] removeAll(final double[] array, final int... indices) {
        return (double[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static float[] removeAll(final float[] array, final int... indices) {
        return (float[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static int[] removeAll(final int[] array, final int... indices) {
        return (int[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static long[] removeAll(final long[] array, final int... indices) {
        return (long[]) removeAll((Object) array, indices);
    }

    /**
     * Removes multiple array elements specified by indices.
     *
     * @param array source
     * @param indices to remove
     * @return new array of same type minus elements specified by the set bits in {@code indices}
     */
    // package protected for access by unit tests
    @SuppressWarnings("all")
    static Object removeAll(final Object array, final BitSet indices) {
        if (array == null) {
            return null;
        }

        final int srcLength = getLength(array);
        // No need to check maxIndex here, because method only currently called from removeElements()
        // which guarantee to generate only valid bit entries.
//        final int maxIndex = indices.length();
//        if (maxIndex > srcLength) {
//            throw new IndexOutOfBoundsException("Index: " + (maxIndex-1) + ", Length: " + srcLength);
//        }
        final int removals = indices.cardinality(); // true bits are items to remove
        final Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
        int srcIndex = 0;
        int destIndex = 0;
        int count;
        int set;
        while ((set = indices.nextSetBit(srcIndex)) != -1) {
            count = set - srcIndex;
            if (count > 0) {
                System.arraycopy(array, srcIndex, result, destIndex, count);
                destIndex += count;
            }
            srcIndex = indices.nextClearBit(set);
        }
        count = srcLength - srcIndex;
        if (count > 0) {
            System.arraycopy(array, srcIndex, result, destIndex, count);
        }
        return result;
    }

    /**
     * Removes multiple array elements specified by index.
     *
     * @param array source
     * @param indices to remove
     * @return new array of same type minus elements specified by unique values of {@code indices}
     */
    // package protected for access by unit tests
    @SuppressWarnings("all")
    static Object removeAll(final Object array, final int... indices) {
        final int length = getLength(array);
        int diff = 0; // number of distinct indexes, i.e. number of entries that will be removed
        final int[] clonedIndices = clone(indices);
        Arrays.sort(clonedIndices);

        // identify length of result array
        if (isNotEmpty(clonedIndices)) {
            int i = clonedIndices.length;
            int prevIndex = length;
            while (--i >= 0) {
                final int index = clonedIndices[i];
                if (index < 0 || index >= length) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
                }
                if (index >= prevIndex) {
                    continue;
                }
                diff++;
                prevIndex = index;
            }
        }

        // create result array
        final Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
        if (diff < length) {
            int end = length; // index just after last copy
            int dest = length - diff; // number of entries so far not copied
            for (int i = clonedIndices.length - 1; i >= 0; i--) {
                final int index = clonedIndices[i];
                if (end - index > 1) { // same as (cp > 0)
                    final int cp = end - index - 1;
                    dest -= cp;
                    System.arraycopy(array, index + 1, result, dest, cp);
                    // After this copy, we still have room for dest items.
                }
                end = index;
            }
            if (end > 0) {
                System.arraycopy(array, 0, result, 0, end);
            }
        }
        return result;
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    public static short[] removeAll(final short[] array, final int... indices) {
        return (short[]) removeAll((Object) array, indices);
    }

    /**
     * Removes the elements at the specified positions from the specified array.
     * All remaining elements are shifted to the left.
     *
     * @param <T> the component type of the array
     * @param array the array to remove the element from, may not be {@code null}
     * @param indices the positions of the elements to be removed
     * @return a new array containing the existing elements except those at the specified positions
     * @throws IndexOutOfBoundsException if any index is out of range
     * ({@code index < 0 || index >= array.length}), or if the array is {@code null}
     */
    // removeAll() always creates an array of the same type as its input
    public static <T> T[] removeAll(final T[] array, final int... indices) {
        return (T[]) removeAll((Object) array, indices);
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(boolean[], boolean)}
     */
    @Deprecated
    public static boolean[] removeAllOccurences(final boolean[] array, final boolean element) {
        return (boolean[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(byte[], byte)}
     */
    @Deprecated
    public static byte[] removeAllOccurences(final byte[] array, final byte element) {
        return (byte[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(char[], char)}
     */
    @Deprecated
    public static char[] removeAllOccurences(final char[] array, final char element) {
        return (char[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(double[], double)}
     */
    @Deprecated
    public static double[] removeAllOccurences(final double[] array, final double element) {
        return (double[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(float[], float)}
     */
    @Deprecated
    public static float[] removeAllOccurences(final float[] array, final float element) {
        return (float[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(int[], int)}
     */
    @Deprecated
    public static int[] removeAllOccurences(final int[] array, final int element) {
        return (int[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(long[], long)}
     */
    @Deprecated
    public static long[] removeAllOccurences(final long[] array, final long element) {
        return (long[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     * @deprecated Use {@link #removeAllOccurrences(short[], short)}
     */
    @Deprecated
    public static short[] removeAllOccurences(final short[] array, final short element) {
        return (short[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static boolean[] removeAllOccurrences(final boolean[] array, final boolean element) {
        return (boolean[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static byte[] removeAllOccurrences(final byte[] array, final byte element) {
        return (byte[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static char[] removeAllOccurrences(final char[] array, final char element) {
        return (char[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static double[] removeAllOccurrences(final double[] array, final double element) {
        return (double[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static float[] removeAllOccurrences(final float[] array, final float element) {
        return (float[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static int[] removeAllOccurrences(final int[] array, final int element) {
        return (int[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static long[] removeAllOccurrences(final long[] array, final long element) {
        return (long[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static short[] removeAllOccurrences(final short[] array, final short element) {
        return (short[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes all occurrences of the specified element from the specified array.
     *
     * @param <T> the component type of the array
     * @param array the input array, may be {@code null}
     * @param element the element to remove
     * @return a new array containing the existing elements except the occurrences of the specified element
     */
    public static <T> T[] removeAllOccurrences(final T[] array, final T element) {
        return (T[]) removeAll(array, indexesOf(array, element));
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static boolean[] removeElement(final boolean[] array, final boolean element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static byte[] removeElement(final byte[] array, final byte element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static char[] removeElement(final char[] array, final char element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static double[] removeElement(final double[] array, final double element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static float[] removeElement(final float[] array, final float element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static int[] removeElement(final int[] array, final int element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static long[] removeElement(final long[] array, final long element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static short[] removeElement(final short[] array, final short element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes the first occurrence of the specified element from the specified array.
     * All subsequent elements are shifted to the left (subtracts one from their indices).
     *
     * @param <T> the component type of the array
     * @param array the array to remove the element from, may be {@code null}
     * @param element the element to be removed
     * @return a new array containing the existing elements except the first occurrence
     * of the specified element
     */
    public static <T> T[] removeElement(final T[] array, final Object element) {
        final int index = indexOf(array, element);
        if (index == INDEX_NOT_FOUND) {
            return clone(array);
        }
        return remove(array, index);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static boolean[] removeElements(final boolean[] array, final boolean... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Boolean, MutableInt> occurrences = new HashMap<>(2); // only two possible values here
        for (final boolean v : values) {
            final Boolean boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final boolean key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (boolean[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static byte[] removeElements(final byte[] array, final byte... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final Map<Byte, MutableInt> occurrences = new HashMap<>(values.length);
        for (final byte v : values) {
            final Byte boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final byte key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (byte[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static char[] removeElements(final char[] array, final char... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Character, MutableInt> occurrences = new HashMap<>(values.length);
        for (final char v : values) {
            final Character boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final char key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (char[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static double[] removeElements(final double[] array, final double... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Double, MutableInt> occurrences = new HashMap<>(values.length);
        for (final double v : values) {
            final Double boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final double key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (double[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static float[] removeElements(final float[] array, final float... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Float, MutableInt> occurrences = new HashMap<>(values.length);
        for (final float v : values) {
            final Float boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final float key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (float[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static int[] removeElements(final int[] array, final int... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Integer, MutableInt> occurrences = new HashMap<>(values.length);
        for (final int v : values) {
            final Integer boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final int key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (int[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static long[] removeElements(final long[] array, final long... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Long, MutableInt> occurrences = new HashMap<>(values.length);
        for (final long v : values) {
            final Long boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final long key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (long[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    public static short[] removeElements(final short[] array, final short... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<Short, MutableInt> occurrences = new HashMap<>(values.length);
        for (final short v : values) {
            final Short boxed = v;
            final MutableInt count = occurrences.get(boxed);
            if (count == null) {
                occurrences.put(boxed, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final short key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        return (short[]) removeAll(array, toRemove);
    }

    /**
     * Removes occurrences of specified elements, in specified quantities, from the specified array.
     * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
     * quantities than contained in the original array, no change occurs beyond the removal of
     * the existing matching items.
     *
     * @param <T> the component type of the array
     * @param array the array to remove the element from, may be {@code null}
     * @param values the elements to be removed
     * @return a new array containing the existing elements except the earliest-encountered
     * occurrences of the specified elements
     */
    @SafeVarargs
    public static <T> T[] removeElements(final T[] array, final T... values) {
        if (isEmpty(array) || isEmpty(values)) {
            return clone(array);
        }
        final HashMap<T, MutableInt> occurrences = new HashMap<>(values.length);
        for (final T v : values) {
            final MutableInt count = occurrences.get(v);
            if (count == null) {
                occurrences.put(v, new MutableInt(1));
            } else {
                count.increment();
            }
        }
        final BitSet toRemove = new BitSet();
        for (int i = 0; i < array.length; i++) {
            final T key = array[i];
            final MutableInt count = occurrences.get(key);
            if (count != null) {
                if (count.decrementAndGet() == 0) {
                    occurrences.remove(key);
                }
                toRemove.set(i);
            }
        }
        // removeAll() always creates an array of the same type as its input
        return (T[]) removeAll(array, toRemove);
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final boolean[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final byte[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final char[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final double[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final float[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final int[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final long[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final Object[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final Object[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Reverses the order of the given array.
     *
     * @param array the array to reverse, may be {@code null}
     */
    public static void reverse(final short[] array) {
        if (array == null) {
            return;
        }
        reverse(array, 0, array.length);
    }

    /**
     * Reverses the order of the given array in the given range.
     *
     * @param array the array to reverse, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     */
    public static void reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if (array == null) {
            return;
        }
        int i = Math.max(startIndexInclusive, 0);
        int j = Math.min(array.length, endIndexExclusive) - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final boolean[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final boolean[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final byte[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final byte[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final char[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final char[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final double[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final double[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final float[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final float[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final int[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final int[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final long[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final long[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final Object[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final Object[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Shifts the order of the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final short[] array, final int offset) {
        if (array == null) {
            return;
        }
        shift(array, 0, array.length, offset);
    }

    /**
     * Shifts the order of a series of elements in the given array.
     *
     * @param array the array to shift, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @param offset the number of positions to rotate the elements; positive rotates right,
     * negative rotates left
     */
    public static void shift(final short[] array, int startIndexInclusive, int endIndexExclusive, int offset) {
        if (array == null) {
            return;
        }
        if (startIndexInclusive >= array.length - 1 || endIndexExclusive <= 0) {
            return;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive >= array.length) {
            endIndexExclusive = array.length;
        }
        int n = endIndexExclusive - startIndexInclusive;
        if (n <= 1) {
            return;
        }
        offset %= n;
        if (offset < 0) {
            offset += n;
        }
        // For algorithm explanations and proof of O(n) time complexity and O(1) space complexity
        // see https://beradrian.wordpress.com/2015/04/07/shift-an-array-in-on-in-place/
        while (n > 1 && offset > 0) {
            final int n_offset = n - offset;

            if (offset > n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n - n_offset,  n_offset);
                n = offset;
                offset -= n_offset;
            } else if (offset < n_offset) {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset,  offset);
                startIndexInclusive += offset;
                n = n_offset;
            } else {
                swap(array, startIndexInclusive, startIndexInclusive + n_offset, offset);
                break;
            }
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final boolean[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final boolean[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final byte[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final byte[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final char[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final char[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final double[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final double[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final float[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final float[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final int[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final int[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final long[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final long[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final Object[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final Object[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Randomly permutes the elements of the specified array using the default source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     */
    public static void shuffle(final short[] array) {
        shuffle(array, new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the specified source of randomness.
     *
     * @param array the array to shuffle, may be {@code null}
     * @param random the source of randomness used to permute the elements
     */
    public static void shuffle(final short[] array, final Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i), 1);
        }
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static boolean[] subarray(final boolean[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }

        final boolean[] subarray = new boolean[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_BYTE_ARRAY;
        }

        final byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static char[] subarray(final char[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_CHAR_ARRAY;
        }

        final char[] subarray = new char[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static double[] subarray(final double[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }

        final double[] subarray = new double[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static float[] subarray(final float[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }

        final float[] subarray = new float[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static int[] subarray(final int[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_INT_ARRAY;
        }

        final int[] subarray = new int[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static long[] subarray(final long[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_LONG_ARRAY;
        }

        final long[] subarray = new long[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static short[] subarray(final short[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_SHORT_ARRAY;
        }

        final short[] subarray = new short[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Produces a new array containing the elements between the start and end indices.
     *
     * <p>
     * The start index is inclusive, the end index is exclusive. A {@code null} array input
     * produces {@code null} output.
     *
     * @param <T> the component type of the array
     * @param array the array, may be {@code null}
     * @param startIndexInclusive the starting index (inclusive)
     * @param endIndexExclusive the ending index (exclusive)
     * @return a new array containing the elements between the start and end indices,
     * {@code null} if {@code null} array input
     */
    public static <T> T[] subarray(final T[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        final Class<?> type = array.getClass().getComponentType();
        if (newSize <= 0) {
            // OK, because array is of type T
            return (T[]) Array.newInstance(type, 0);
        }
        // OK, because array is of type T
        final
        T[] subarray = (T[]) Array.newInstance(type, newSize);
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final boolean[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final boolean[] array, int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final boolean aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final byte[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final byte[] array, int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final byte aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final char[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final char[] array, int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final char aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final double[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final double[] array,  int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final double aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final float[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final float[] array, int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final float aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }

    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final int[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final int[] array,  int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final int aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final long[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final long[] array,  int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final long aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final Object[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final Object[] array,  int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final Object aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Swaps two elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element to swap
     * @param offset2 the index of the second element to swap
     */
    public static void swap(final short[] array, final int offset1, final int offset2) {
        if (isEmpty(array)) {
            return;
        }
        swap(array, offset1, offset2, 1);
    }

    /**
     * Swaps a series of elements in the given array.
     *
     * @param array the array to swap, may be {@code null}
     * @param offset1 the index of the first element in the series to swap
     * @param offset2 the index of the second element in the series to swap
     * @param len the number of elements to swap starting with the given indices
     */
    public static void swap(final short[] array,  int offset1, int offset2, int len) {
        if (isEmpty(array) || offset1 >= array.length || offset2 >= array.length) {
            return;
        }
        if (offset1 < 0) {
            offset1 = 0;
        }
        if (offset2 < 0) {
            offset2 = 0;
        }
        if (offset1 == offset2) {
            return;
        }
        len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
        for (int i = 0; i < len; i++, offset1++, offset2++) {
            final short aux = array[offset1];
            array[offset1] = array[offset2];
            array[offset2] = aux;
        }
    }

    /**
     * Creates a type-safe generic array.
     *
     * <p>
     * The Java language does not allow an array to be created from a generic type.
     * This method provides a workaround by using varargs.
     *
     * @param <T> the array's element type
     * @param items the varargs array items, null allowed
     * @return the array, not null unless a null array is passed in
     */
    public static <T> T[] toArray(final T... items) {
        return items;
    }

    /**
     * Converts the given array into a {@link java.util.Map}. Each element of the array must be
     * either a {@link java.util.Map.Entry} or an Array, containing at least two elements, where
     * the first element is used as key and the second as value.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array an array whose elements are either a {@link java.util.Map.Entry} or an Array
     * containing at least two elements, may be {@code null}
     * @return a {@code Map} that was created from the array
     * @throws IllegalArgumentException if one element of this array is itself an Array containing
     * less than two elements
     * @throws IllegalArgumentException if the array contains elements other than
     * {@link java.util.Map.Entry} and an Array
     */
    public static Map<Object, Object> toMap(final Object[] array) {
        if (array == null) {
            return null;
        }
        final Map<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            if (object instanceof Map.Entry<?, ?>) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
                map.put(entry.getKey(), entry.getValue());
            } else if (object instanceof Object[]) {
                final Object[] entry = (Object[]) object;
                if (entry.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '"
                            + object
                            + "', has a length less than 2");
                }
                map.put(entry[0], entry[1]);
            } else {
                throw new IllegalArgumentException("Array element " + i + ", '"
                        + object
                        + "', is neither of type Map.Entry nor an Array");
            }
        }
        return map;
    }

    /**
     * Converts the given Collection into an array of Strings without {@code null} entries.
     *
     * @param collection the collection to convert
     * @return a new array of Strings
     */
    public static String[] toNoNullStringArray(final Collection<String> collection) {
        if (collection == null)
            return EMPTY_STRING_ARRAY;

        return toNoNullStringArray(collection.toArray());
    }

    /**
     * Returns a new array of Strings without {@code null} elements.
     *
     * @param array the array to check
     * @return the given array or a new array without null
     */
    public static String[] toNoNullStringArray(final Object[] array) {
        final List<String> list = new ArrayList<>(array.length);

        for (final Object e : array) {
            if (e != null)
                list.add(e.toString());
        }

        return list.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Outputs an array as a String, treating {@code null} as an empty array.
     *
     * <p>
     * Multi-dimensional arrays are handled correctly, including multi-dimensional primitive arrays.
     * The format is that of Java source code, for example {@code {a,b}}.
     *
     * @param array the array to get a toString for, may be {@code null}
     * @return a String representation of the array, '{}' if null array input
     */
    public static String toString(final Object array) {
        return toString(array, "{}");
    }

    /**
     * Outputs an array as a String handling {@code null}s.
     *
     * <p>
     * Multi-dimensional arrays are handled correctly, including multi-dimensional primitive arrays.
     * The format is that of Java source code, for example {@code {a,b}}.
     *
     * @param array the array to get a toString for, may be {@code null}
     * @param stringIfNull the String to return if the array is {@code null}
     * @return a String representation of the array
     */
    public static String toString(final Object array, final String stringIfNull) {
        if (array == null)
            return stringIfNull;

        return Arrays.deepToString((Object[])array);
    }

    /**
     * Returns an array containing the string representation of each element in the argument array.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array the {@code Object[]} to be processed, may be {@code null}
     * @return {@code String[]} of the same size as the source with its element's string representation,
     * {@code null} if null array input
     * @throws NullPointerException if array contains {@code null}
     */
    public static String[] toStringArray(final Object[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_STRING_ARRAY;
        }

        final String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].toString();
        }

        return result;
    }

    /**
     * Returns an array containing the string representation of each element in the argument
     * array handling {@code null} elements.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array the {@code Object[]} to be processed, may be {@code null}
     * @param valueForNullElements the value to insert if {@code null} is found
     * @return a {@code String} array, {@code null} if null array input
     */
    public static String[] toStringArray(final Object[] array, final String valueForNullElements) {
        if (null == array) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_STRING_ARRAY;
        }

        final String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            final Object object = array[i];
            result[i] = (object == null ? valueForNullElements : object.toString());
        }

        return result;
    }

}

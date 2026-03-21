package dev.sbs.api.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Null-safe utility methods for operating on {@link Class} objects without reflection.
 *
 * <p>
 * This class handles invalid {@code null} inputs as best it can. Each method
 * documents its behavior in more detail.
 *
 * <p>
 * The notion of a {@code canonical name} includes the human-readable name for the
 * type, for example {@code int[]}. The non-canonical method variants work with the
 * JVM names, such as {@code [I}.
 */
@UtilityClass
public final class ClassUtil {

    /**
     * The package separator character: <code>'&#x2e;' == {@value}</code>.
     */
    public static final char PACKAGE_SEPARATOR_CHAR = '.';

    /**
     * The package separator string: {@code "&#x2e;"}.
     */
    public static final String PACKAGE_SEPARATOR = String.valueOf(PACKAGE_SEPARATOR_CHAR);

    /**
     * The inner class separator character: <code>'$' == {@value}</code>.
     */
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';

    /**
     * The inner class separator string: {@code "$"}.
     */
    public static final String INNER_CLASS_SEPARATOR = String.valueOf(INNER_CLASS_SEPARATOR_CHAR);

    /**
     * Returns the class name minus the package name for an {@code Object}.
     *
     * @param object  the class to get the short name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the class name of the object without the package name, or the null value
     */
    public static String getShortClassName(Object object, String valueIfNull) {
        if (object == null)
            return valueIfNull;

        return getShortClassName(object.getClass());
    }

    /**
     * Returns the class name minus the package name from a {@code Class}.
     *
     * <p>
     * Consider using {@link Class#getSimpleName()} instead. The one known
     * difference is that this method will return {@code "Map.Entry"} while
     * the {@code java.lang.Class} variant will simply return {@code "Entry"}.
     *
     * @param cls  the class to get the short name for
     * @return the class name without the package name or an empty string
     */
    public static String getShortClassName(Class<?> cls) {
        if (cls == null)
            return StringUtil.EMPTY;

        return getShortClassName(cls.getName());
    }

    /**
     * Returns the class name minus the package name from a string.
     *
     * <p>
     * The string passed in is assumed to be a class name - it is not checked.
     *
     * <p>
     * Note that this method differs from {@link Class#getSimpleName()} in that this will
     * return {@code "Map.Entry"} whilst the {@code java.lang.Class} variant will simply
     * return {@code "Entry"}.
     *
     * @param className  the className to get the short name for
     * @return the class name of the class without the package name or an empty string
     */
    public static String getShortClassName(String className) {
        if (className == null)
            return StringUtil.EMPTY;

        if (className.isEmpty())
            return StringUtil.EMPTY;

        StringBuilder arrayPrefix = new StringBuilder();

        // Handle array encoding
        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }

            // Strip Object type encoding
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';')
                className = className.substring(1, className.length() - 1);
        }

        if (PrimitiveUtil.getReverseAbbreviationMap().containsKey(className))
            className = PrimitiveUtil.getReverseAbbreviationMap().get(className);

        int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        int innerIdx = className.indexOf(INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
        String out = className.substring(lastDotIdx + 1);

        if (innerIdx != -1)
            out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);

        return out + arrayPrefix;
    }

    /**
     * Returns the simple name of a {@code Class} as would be returned by {@link Class#getSimpleName()},
     * or an empty string if the class is {@code null}.
     *
     * @param cls  the class for which to get the simple name
     * @return the simple class name
     * @see Class#getSimpleName()
     */
    public static String getSimpleName(Class<?> cls) {
        if (cls == null)
            return StringUtil.EMPTY;

        return cls.getSimpleName();
    }

    /**
     * Returns the simple name of an object's {@code Class} as would be returned by
     * {@link Class#getSimpleName()}, or the given default value if the object is {@code null}.
     *
     * @param object  the object for which to get the simple class name
     * @param valueIfNull  the value to return if {@code object} is {@code null}
     * @return the simple class name
     * @see Class#getSimpleName()
     */
    public static String getSimpleName(Object object, String valueIfNull) {
        if (object == null)
            return valueIfNull;

        return getSimpleName(object.getClass());
    }

    /**
     * Returns the package name of an {@code Object}.
     *
     * @param object  the class to get the package name for, may be null
     * @param valueIfNull  the value to return if null
     * @return the package name of the object, or the null value
     */
    public static String getPackageName(Object object, String valueIfNull) {
        if (object == null)
            return valueIfNull;

        return getPackageName(object.getClass());
    }

    /**
     * Returns the package name of a {@code Class}.
     *
     * @param cls  the class to get the package name for, may be {@code null}
     * @return the package name or an empty string
     */
    public static String getPackageName(Class<?> cls) {
        if (cls == null)
            return StringUtil.EMPTY;

        return getPackageName(cls.getName());
    }

    /**
     * Returns the package name from a {@code String}.
     *
     * <p>
     * The string passed in is assumed to be a class name - it is not checked.
     * If the class is unpackaged, returns an empty string.
     *
     * @param className  the className to get the package name for, may be {@code null}
     * @return the package name or an empty string
     */
    public static String getPackageName(String className) {
        if (className == null || className.isEmpty())
            return StringUtil.EMPTY;

        // Strip array encoding
        while (className.charAt(0) == '[')
            className = className.substring(1);

        // Strip Object type encoding
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';')
            className = className.substring(1);

        int i = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (i == -1)
            return StringUtil.EMPTY;

        return className.substring(0, i);
    }

    /**
     * Returns a {@code List} of superclasses for the given class.
     *
     * @param cls  the class to look up, may be {@code null}
     * @return the {@code List} of superclasses in order going up from this one,
     *  {@code null} if null input
     */
    public static List<Class<?>> getAllSuperclasses(Class<?> cls) {
        if (cls == null)
            return null;

        List<Class<?>> classes = new ArrayList<>();
        Class<?> superclass = cls.getSuperclass();

        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }

        return classes;
    }

    /**
     * Returns a {@code List} of all interfaces implemented by the given
     * class and its superclasses.
     *
     * <p>
     * The order is determined by looking through each interface in turn as
     * declared in the source file and following its hierarchy up. Then each
     * superclass is considered in the same way. Later duplicates are ignored,
     * so the order is maintained.
     *
     * @param cls  the class to look up, may be {@code null}
     * @return the {@code List} of interfaces in order,
     *  {@code null} if null input
     */
    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null)
            return null;

        LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
        getAllInterfaces(cls, interfacesFound);

        return new ArrayList<>(interfacesFound);
    }

    /**
     * Collects the interfaces for the specified class into the given set.
     *
     * @param cls  the class to look up, may be {@code null}
     * @param interfacesFound  the {@code Set} of interfaces for the class
     */
    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();

            for (Class<?> i : interfaces) {
                if (interfacesFound.add(i))
                    getAllInterfaces(i, interfacesFound);
            }

            cls = cls.getSuperclass();
         }
     }

    /**
     * Converts a {@code List} of class names into their corresponding {@code Class} objects.
     *
     * <p>
     * A new {@code List} is returned. If the class name cannot be found, {@code null}
     * is stored in the {@code List}. If the class name in the {@code List} is
     * {@code null}, {@code null} is stored in the output {@code List}.
     *
     * @param classNames  the classNames to change
     * @return a {@code List} of Class objects corresponding to the class names,
     *  {@code null} if null input
     * @throws ClassCastException if classNames contains a non String entry
     */
    public static List<Class<?>> convertClassNamesToClasses(List<String> classNames) {
        if (classNames == null)
            return null;

        List<Class<?>> classes = new ArrayList<>(classNames.size());

        for (String className : classNames) {
            try {
                classes.add(Class.forName(className));
            } catch (Exception ex) {
                classes.add(null);
            }
        }

        return classes;
    }

    /**
     * Converts a {@code List} of {@code Class} objects into their fully qualified class names.
     *
     * <p>
     * A new {@code List} is returned. {@code null} objects will be copied into
     * the returned list as {@code null}.
     *
     * @param classes  the classes to change
     * @return a {@code List} of class names corresponding to the Class objects,
     *  {@code null} if null input
     * @throws ClassCastException if {@code classes} contains a non-{@code Class} entry
     */
    public static List<String> convertClassesToClassNames(List<Class<?>> classes) {
        if (classes == null)
            return null;

        List<String> classNames = new ArrayList<>(classes.size());

        for (Class<?> cls : classes) {
            if (cls == null)
                classNames.add(null);
            else
                classNames.add(cls.getName());
        }

        return classNames;
    }

    /**
     * Checks if an array of Classes can be assigned to another array of Classes.
     *
     * <p>
     * This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatible with a set of method parameter types
     * (the second parameter).
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * {@code null}s.
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a {@code long},
     * {@code float} or {@code double}. This method returns the correct
     * result for these cases.
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method will
     * return {@code true} if {@code null} is passed in and the toClass is
     * non-primitive.
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     *
     * <p>
     * Autoboxing is used by default when calculating assignability between
     * primitive and wrapper types.
     *
     * @param classArray  the array of Classes to check, may be {@code null}
     * @param toClassArray  the array of Classes to try to assign into, may be {@code null}
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?>[] classArray, Class<?>... toClassArray) {
        return isAssignable(classArray, toClassArray, true);
    }

    /**
     * Checks if an array of Classes can be assigned to another array of Classes.
     *
     * <p>
     * This method calls {@link #isAssignable(Class, Class) isAssignable} for each
     * Class pair in the input arrays. It can be used to check if a set of arguments
     * (the first parameter) are suitably compatible with a set of method parameter types
     * (the second parameter).
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method, this
     * method takes into account widenings of primitive classes and
     * {@code null}s.
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a {@code long},
     * {@code float} or {@code double}. This method returns the correct
     * result for these cases.
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method will
     * return {@code true} if {@code null} is passed in and the toClass is
     * non-primitive.
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     *
     * @param classArray  the array of Classes to check, may be {@code null}
     * @param toClassArray  the array of Classes to try to assign into, may be {@code null}
     * @param autoboxing  whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, boolean autoboxing) {
        if (!ArrayUtil.isSameLength(classArray, toClassArray))
            return false;

        if (classArray == null)
            classArray = ArrayUtil.EMPTY_CLASS_ARRAY;

        if (toClassArray == null)
            toClassArray = ArrayUtil.EMPTY_CLASS_ARRAY;

        for (int i = 0; i < classArray.length; i++) {
            if (!isAssignable(classArray[i], toClassArray[i], autoboxing))
                return false;
        }

        return true;
    }

    /**
     * Checks if one {@code Class} can be assigned to a variable of another {@code Class}.
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * {@code null}s.
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method
     * will return {@code true} if {@code null} is passed in and the
     * toClass is non-primitive.
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     *
     * <p>
     * Autoboxing is used by default when calculating assignability between
     * primitive and wrapper types.
     *
     * @param cls  the Class to check, may be null
     * @param toClass  the Class to try to assign into, returns false if null
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?> cls, Class<?> toClass) {
        return isAssignable(cls, toClass, true);
    }

    /**
     * Checks if one {@code Class} can be assigned to a variable of another {@code Class}.
     *
     * <p>
     * Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * {@code null}s.
     *
     * <p>
     * Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.
     *
     * <p>
     * {@code Null} may be assigned to any reference type. This method
     * will return {@code true} if {@code null} is passed in and the
     * toClass is non-primitive.
     *
     * <p>
     * Specifically, this method tests whether the type represented by the
     * specified {@code Class} parameter can be converted to the type
     * represented by this {@code Class} object via an identity conversion
     * widening primitive or widening reference conversion. See
     * <em><a href="http://java.sun.com/docs/books/jls/">The Java Language Specification</a></em>,
     * sections 5.1.1, 5.1.2 and 5.1.4 for details.
     *
     * @param cls  the Class to check, may be null
     * @param toClass  the Class to try to assign into, returns false if null
     * @param autoboxing  whether to use implicit autoboxing/unboxing between primitives and wrappers
     * @return {@code true} if assignment possible
     */
    public static boolean isAssignable(Class<?> cls, Class<?> toClass, boolean autoboxing) {
        if (toClass == null)
            return false;

        // have to check for null, as isAssignableFrom doesn't
        if (cls == null)
            return !(toClass.isPrimitive());

        //autoboxing:
        if (autoboxing) {
            if (cls.isPrimitive() && !toClass.isPrimitive()) {
                cls = PrimitiveUtil.wrap(cls);

                if (cls == null)
                    return false;
            }

            if (toClass.isPrimitive() && !cls.isPrimitive()) {
                cls = PrimitiveUtil.unwrap(cls);

                if (cls == null)
                    return false;
            }
        }

        if (cls.equals(toClass))
            return true;

        if (cls.isPrimitive()) {
            if (!toClass.isPrimitive())
                return false;

            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass)
                    || Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }

            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass)
                    || Double.TYPE.equals(toClass);
            }

            if (Boolean.TYPE.equals(cls))
                return false;

            if (Double.TYPE.equals(cls))
                return false;

            if (Float.TYPE.equals(cls))
                return Double.TYPE.equals(toClass);

            if (Character.TYPE.equals(cls))
                return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);

            if (Short.TYPE.equals(cls))
                return Integer.TYPE.equals(toClass)  || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass)  || Double.TYPE.equals(toClass);

            if (Byte.TYPE.equals(cls))
                return Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);

            // should never get here
            return false;
        }

        return toClass.isAssignableFrom(cls);
    }

    /**
     * Checks whether the specified class is an inner class or static nested class.
     *
     * @param cls  the class to check, may be null
     * @return {@code true} if the class is an inner or static nested class,
     *  false if not or {@code null}
     */
    public static boolean isInnerClass(Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }

    /**
     * Returns the class represented by {@code className} using the given
     * {@code classLoader}. This implementation supports the syntaxes
     * "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}",
     * "{@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param classLoader the class loader to use to load the class
     * @param className the class name
     * @param initialize whether the class must be initialized
     * @return the class represented by {@code className} using the {@code classLoader}
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(
            ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        try {
            Class<?> clazz;
            if (PrimitiveUtil.getReverseAbbreviationMap().containsKey(className)) {
                String clsName = "[" + PrimitiveUtil.getReverseAbbreviationMap().get(className);
                clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
            } else {
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        } catch (ClassNotFoundException ex) {
            // allow path separators (.) as inner class name separators
            int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);

            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex) +
                            INNER_CLASS_SEPARATOR_CHAR + className.substring(lastDotIndex + 1),
                            initialize);
                } catch (ClassNotFoundException ex2) { // NOPMD
                    // ignore exception
                }
            }

            throw ex;
        }
    }

    /**
     * Returns the (initialized) class represented by {@code className}
     * using the {@code classLoader}.  This implementation supports
     * the syntaxes "{@code java.util.Map.Entry[]}",
     * "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}",
     * and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param classLoader  the class loader to use to load the class
     * @param className  the class name
     * @return the class represented by {@code className} using the {@code classLoader}
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    /**
     * Returns the (initialized) class represented by {@code className}
     * using the current thread's context class loader. This implementation
     * supports the syntaxes "{@code java.util.Map.Entry[]}",
     * "{@code java.util.Map$Entry[]}", "{@code [Ljava.util.Map.Entry;}",
     * and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param className  the class name
     * @return the class represented by {@code className} using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return getClass(className, true);
    }

    /**
     * Returns the class represented by {@code className} using the
     * current thread's context class loader. This implementation supports the
     * syntaxes "{@code java.util.Map.Entry[]}", "{@code java.util.Map$Entry[]}",
     * "{@code [Ljava.util.Map.Entry;}", and "{@code [Ljava.util.Map$Entry;}".
     *
     * @param className  the class name
     * @param initialize  whether the class must be initialized
     * @return the class represented by {@code className} using the current thread's context class loader
     * @throws ClassNotFoundException if the class is not found
     */
    public static Class<?> getClass(String className, boolean initialize) throws ClassNotFoundException {
        return getClass(getClassLoader(), className, initialize);
    }

    /**
     * Returns the context class loader of the current thread, falling back to the
     * class loader of {@link ClassUtil} if the context class loader is {@code null}.
     *
     * @return the class loader to use
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassUtil.class);
    }

    /**
     * Returns the context class loader of the current thread, falling back to the
     * class loader of the given class if the context class loader is {@code null}.
     *
     * @param tClass the class whose class loader is used as a fallback
     * @return the class loader to use
     */
    public static ClassLoader getClassLoader(@NotNull Class<?> tClass) {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        return contextCL == null ? tClass.getClassLoader() : contextCL;
    }

    /**
     * Returns the desired {@link Method} much like {@link Class#getMethod(String, Class[])},
     * however it ensures that the returned method is from a public class or interface and
     * not from an anonymous inner class. This means that the method is invokable and
     * does not fall foul of Java bug
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957">4071957</a>.
     *
     * <pre>{@code
     * Set set = Collections.unmodifiableSet(...);
     * Method method = ClassUtil.getPublicMethod(set.getClass(), "isEmpty", new Class[0]);
     * Object result = method.invoke(set, new Object[]);
     * }</pre>
     *
     * @param cls the class to check, not null
     * @param methodName the name of the method
     * @param parameterTypes the list of parameters
     * @return the method
     * @throws NullPointerException if the class is null
     * @throws SecurityException if a security violation occurred
     * @throws NoSuchMethodException if the method is not found in the given class
     *  or if the method does not conform with the requirements
     */
    public static Method getPublicMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers()))
            return declaredMethod;

        List<Class<?>> candidateClasses = new ArrayList<>();
        candidateClasses.addAll(getAllInterfaces(cls));
        candidateClasses.addAll(getAllSuperclasses(cls));

        for (Class<?> candidateClass : candidateClasses) {
            if (!Modifier.isPublic(candidateClass.getModifiers()))
                continue;

            Method candidateMethod;
            try {
                candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException ex) {
                continue;
            }

            if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers()))
                return candidateMethod;
        }

        throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtil.toString(parameterTypes));
    }

    /**
     * Converts a class name to a JLS style class name.
     *
     * @param className the class name
     * @return the converted name
     */
    private static String toCanonicalName(String className) {
        className = StringUtil.deleteWhitespace(className);
        if (className == null) {
            throw new NullPointerException("className must not be null");
        } else if (className.endsWith("[]")) {
            StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            String abbreviation = PrimitiveUtil.getReverseAbbreviationMap().get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            } else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }

    /**
     * Converts an array of {@code Object} into an array of {@code Class} objects.
     * If any of these objects is null, a null element will be inserted into the array.
     *
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array an {@code Object} array
     * @return a {@code Class} array, {@code null} if null array input
     */
    public static Class<?>[] toClass(Object... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return ArrayUtil.EMPTY_CLASS_ARRAY;
        }
        Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    /**
     * Returns the canonical name minus the package name for an {@code Object}.
     *
     * @param object the class to get the short name for, may be null
     * @param valueIfNull the value to return if null
     * @return the canonical name of the object without the package name, or the null value
     */
    public static String getShortCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortCanonicalName(object.getClass().getName());
    }

    /**
     * Returns the canonical name minus the package name from a {@code Class}.
     *
     * @param cls the class to get the short name for
     * @return the canonical name without the package name or an empty string
     */
    public static String getShortCanonicalName(Class<?> cls) {
        if (cls == null) {
            return StringUtil.EMPTY;
        }
        return getShortCanonicalName(cls.getName());
    }

    /**
     * Returns the canonical name minus the package name from a String.
     *
     * <p>
     * The string passed in is assumed to be a canonical name - it is not checked.
     *
     * @param canonicalName the class name to get the short name for
     * @return the canonical name of the class without the package name or an empty string
     */
    public static String getShortCanonicalName(String canonicalName) {
        return ClassUtil.getShortClassName(getCanonicalName(canonicalName));
    }

    /**
     * Returns the package name from the canonical name of an {@code Object}.
     *
     * @param object the class to get the package name for, may be null
     * @param valueIfNull the value to return if null
     * @return the package name of the object, or the null value
     */
    public static String getPackageCanonicalName(Object object, String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageCanonicalName(object.getClass().getName());
    }

    /**
     * Returns the package name from the canonical name of a {@code Class}.
     *
     * @param cls the class to get the package name for, may be {@code null}
     * @return the package name or an empty string
     */
    public static String getPackageCanonicalName(Class<?> cls) {
        if (cls == null) {
            return StringUtil.EMPTY;
        }
        return getPackageCanonicalName(cls.getName());
    }

    /**
     * Returns the package name from the canonical name.
     *
     * <p>
     * The string passed in is assumed to be a canonical name - it is not checked.
     * If the class is unpackaged, returns an empty string.
     *
     * @param canonicalName the canonical name to get the package name for, may be {@code null}
     * @return the package name or an empty string
     */
    public static String getPackageCanonicalName(String canonicalName) {
        return ClassUtil.getPackageName(getCanonicalName(canonicalName));
    }

    /**
     * Converts a given name of class into canonical format. If name of class is
     * not a name of array class it returns the unchanged name.
     *
     * <p>
     * Example:
     * <ul>
     * <li>{@code getCanonicalName("[I") = "int[]"}</li>
     * <li>{@code getCanonicalName("[Ljava.lang.String;") = "java.lang.String[]"}</li>
     * <li>{@code getCanonicalName("java.lang.String") = "java.lang.String"}</li>
     * </ul>
     *
     * @param className the name of class
     * @return canonical form of class name
     */
    private static String getCanonicalName(String className) {
        className = StringUtil.deleteWhitespace(className);

        if (className == null)
            return null;
        else {
            int dim = 0;
            while (className.startsWith("[")) {
                dim++;
                className = className.substring(1);
            }

            if (dim < 1)
                return className;
            else {
                if (className.startsWith("L")) {
                    className = className.substring(
                        1,
                        className.endsWith(";")
                            ? className.length() - 1
                            : className.length());
                } else {
                    if (!className.isEmpty())
                        className = PrimitiveUtil.getReverseAbbreviationMap().get(className.substring(0, 1));
                }
                return className + "[]".repeat(dim);
            }
        }
    }
}

package dev.simplified.util;

import dev.simplified.collection.Concurrent;
import dev.simplified.collection.ConcurrentList;
import dev.simplified.collection.ConcurrentMap;
import dev.simplified.collection.tuple.pair.Pair;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Helpers for {@code java.lang.System} providing cached system properties, environment variable
 * access, and classpath resource utilities.
 *
 * <p>If a system property cannot be read due to security restrictions, the corresponding field
 * in this class will be set to {@code null} and a message will be written to {@code System.err}.
 *
 * <p>Property values are initialized when the class is loaded. If
 * {@link System#setProperty(String, String)} or {@link System#setProperties(java.util.Properties)}
 * is called after this class is loaded, the cached values will be out of sync.
 */
@UtilityClass
public final class SystemUtil {

    /**
     * The {@code user.home} system property - user home directory.
     */
    private static final String USER_HOME_KEY = "user.home";

    /**
     * The {@code user.dir} system property - user directory.
     */
    private static final String USER_DIR_KEY = "user.dir";

    /**
     * The {@code java.io.tmpdir} system property - Java IO temporary directory.
     */
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

    /**
     * The {@code java.home} system property - Java home directory.
     */
    private static final String JAVA_HOME_KEY = "java.home";

    /** The {@code file.encoding} system property, such as {@code Cp1252}. */
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");

    /** The {@code file.separator} system property - file separator ({@code "/"} on UNIX). */
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

    /** The {@code java.awt.headless} system property - {@code "true"} or {@code "false"}. */
    private static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

    /** The {@code java.home} system property - Java installation directory. */
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);

    /** The {@code java.io.tmpdir} system property - default temp file path. */
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);

    /** The {@code java.class.path} system property - Java class path. */
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

    /** The {@code java.library.path} system property - list of paths to search when loading libraries. */
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

    /** The {@code java.runtime.name} system property - Java Runtime Environment name. */
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

    /** The {@code java.runtime.version} system property - Java Runtime Environment version. */
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

    /** The {@code java.specification.name} system property - Java Runtime Environment specification name. */
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

    /** The {@code java.specification.vendor} system property - Java Runtime Environment specification vendor. */
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

    /** The {@code java.specification.version} system property - Java Runtime Environment specification version. */
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

    /** The {@code java.util.prefs.PreferencesFactory} system property - a class name. */
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");

    /** The {@code java.vendor} system property - Java vendor-specific string. */
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

    /** The {@code java.vendor.url} system property - Java vendor URL. */
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

    /** The {@code java.version} system property - Java version number. */
    public static final String JAVA_VERSION = getSystemProperty("java.version");

    /** The {@code java.vm.info} system property - Java Virtual Machine implementation info. */
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

    /** The {@code java.vm.name} system property - Java Virtual Machine implementation name. */
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

    /** The {@code java.vm.specification.name} system property - Java Virtual Machine specification name. */
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

    /** The {@code java.vm.specification.vendor} system property - Java Virtual Machine specification vendor. */
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

    /** The {@code java.vm.specification.version} system property - Java Virtual Machine specification version. */
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

    /** The {@code java.vm.vendor} system property - Java Virtual Machine implementation vendor. */
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

    /** The {@code java.vm.version} system property - Java Virtual Machine implementation version. */
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    /** The {@code line.separator} system property - line separator ({@code "\n"} on UNIX). */
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

    /** The {@code path.separator} system property - path separator ({@code ":"} on UNIX). */
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    /** The {@code user.dir} system property - user's current working directory. */
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);

    /** The {@code user.home} system property - user's home directory. */
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);

    /** The {@code user.language} system property - user's language code, such as {@code "en"}. */
    public static final String USER_LANGUAGE = getSystemProperty("user.language");

    /** The {@code user.name} system property - user's account name. */
    public static final String USER_NAME = getSystemProperty("user.name");

    /** The {@code user.timezone} system property, for example {@code "America/Los_Angeles"}. */
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    /** Unmodifiable map of environment variables merged from {@code .env} files and OS environment. */
    @Getter
    private static @NotNull ConcurrentMap<String, String> env = loadEnvironmentVariables().toUnmodifiableMap();

    /**
     * Returns the Java home directory as a {@link File}.
     *
     * @return the Java home directory
     * @throws SecurityException if a security manager prevents access to the system property
     * @see System#getProperty(String)
     */
    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    /**
     * Returns the Java IO temporary directory as a {@link File}.
     *
     * @return the temporary directory
     * @throws SecurityException if a security manager prevents access to the system property
     * @see System#getProperty(String)
     */
    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    /**
     * Returns the user's current working directory as a {@link File}.
     *
     * @return the user directory
     * @throws SecurityException if a security manager prevents access to the system property
     * @see System#getProperty(String)
     */
    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    /**
     * Returns the user's home directory as a {@link File}.
     *
     * @return the user home directory
     * @throws SecurityException if a security manager prevents access to the system property
     * @see System#getProperty(String)
     */
    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    /**
     * Checks whether the {@code java.awt.headless} system property is set to {@code "true"}.
     *
     * @return {@code true} if the JVM is running in headless mode, {@code false} otherwise
     * @see #JAVA_AWT_HEADLESS
     */
    public static boolean isJavaAwtHeadless() {
        return JAVA_AWT_HEADLESS != null && JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString());
    }

    /**
     * Determines the preferred local {@link InetAddress} by opening a UDP socket to a well-known
     * external address and reading back the local endpoint.
     *
     * @return the preferred outbound network address
     */
    @SneakyThrows
    public static @NotNull InetAddress getPreferredAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress();
        }
    }

    /**
     * Retrieves a system property by name, defaulting to {@code null} if the property cannot be read.
     *
     * <p>If a {@link SecurityException} is caught, the return value is {@code null} and a message is
     * written to {@code System.err}.
     *
     * @param property the system property name
     * @return the system property value, or {@code null} if a security problem occurs
     */
    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property
                                   + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    /**
     * Parses key-value pairs from the given {@code .env}-formatted input stream.
     *
     * @param inputStream the input stream to read, or {@code null} to return an empty map
     * @return a mutable map of parsed environment variables
     */
    private static @NotNull ConcurrentMap<String, String> readEnvironmentFile(@Nullable InputStream inputStream) {
        ConcurrentMap<String, String> variables = Concurrent.newMap();

        if (inputStream != null) {
            Scanner scanner = new Scanner(inputStream);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("=")) {
                    String[] pair = line.split("=", 2);
                    variables.put(pair[0], pair.length == 2 ? pair[1] : "");
                }
            }
        }

        return variables;
    }

    /**
     * Returns the directory containing the running application's JAR or class files.
     *
     * @return the parent directory of this class's code source location
     */
    @SneakyThrows
    public static @NotNull File getCurrentDirectory() {
        return new File(SystemUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
    }

    /**
     * Loads environment variables by merging {@code .env} files from the classpath and working
     * directory, then overlaying the OS environment.
     *
     * @return a mutable map of all resolved environment variables
     */
    private static @NotNull ConcurrentMap<String, String> loadEnvironmentVariables() {
        ConcurrentMap<String, String> variables = Concurrent.newMap();

        // Load src/main/resources/.env
        try {
            @Cleanup InputStream resourceFile = getResource("../.env");
            variables.putAll(readEnvironmentFile(resourceFile));
        } catch (Exception ignore) { }

        // Load <working directory>/.env
        try {
            @Cleanup InputStream localFile = new FileInputStream(getCurrentDirectory() + FILE_SEPARATOR + ".env");
            variables.putAll(readEnvironmentFile(localFile));
        } catch (Exception ignore) { }

        // Override From OS
        variables.putAll(System.getenv());
        return variables;
    }

    /**
     * Looks up a single environment variable by name (case-insensitive).
     *
     * @param variableName the name of the environment variable
     * @return an optional containing the value if found, or empty otherwise
     */
    public static @NotNull Optional<String> getEnv(@NotNull String variableName) {
        return getEnv()
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase(variableName))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    /**
     * Looks up a single environment variable by name (case-insensitive) and returns it as a
     * key-value {@link Pair}.
     *
     * @param variableName the name of the environment variable
     * @return a pair of the variable name and an optional containing its value
     */
    public static @NotNull Pair<String, Optional<String>> getEnvPair(@NotNull String variableName) {
        return getEnv()
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase(variableName))
            .map(entry -> Pair.of(variableName, Optional.ofNullable(entry.getValue())))
            .findFirst()
            .orElse(Pair.of(variableName, Optional.empty()));
    }

    /**
     * Opens a classpath resource as an {@link InputStream}, stripping any leading
     * {@code "resources/"} or {@code "/"} prefix from the path.
     *
     * @param resourcePath the classpath-relative resource path
     * @return an input stream for the resource, or {@code null} if not found
     */
    public static @Nullable InputStream getResource(@NotNull String resourcePath) {
        return ClassUtil.getClassLoader(SystemUtil.class).getResourceAsStream(
            RegexUtil.replaceFirst(resourcePath, "^resources/", "").replaceFirst("^/", "")
        );
    }

    /**
     * Lists the file names available under a classpath resource directory by reading lines
     * from the directory's input stream.
     *
     * @param resourcePath the classpath-relative directory path
     * @return a list of file names found under the resource path, or an empty list on failure
     */
    public static @NotNull ConcurrentList<String> getResourceFiles(@NotNull String resourcePath) {
        ConcurrentList<String> fileNames = Concurrent.newList();

        try {
            resourcePath = RegexUtil.replaceFirst(resourcePath, "^resources/", "");
            @Cleanup InputStream inputStream = getResource(resourcePath);
            if (inputStream != null) {
                @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String resource;

                while ((resource = bufferedReader.readLine()) != null)
                    fileNames.add(resource);
            }
        } catch (IOException ignore) { }

        return fileNames;
    }

    /**
     * Reads all bytes from a classpath resource.
     *
     * @param resourcePath the classpath-relative resource path
     * @return the full byte contents of the resource, or an empty array if not found
     */
    @SneakyThrows
    public static byte[] readResource(@NotNull String resourcePath) {
        @Cleanup InputStream inputStream = getResource(resourcePath);
        return inputStream != null ? inputStream.readAllBytes() : new byte[0];
    }

    /**
     * Saves a classpath resource to the given output directory, optionally replacing an
     * existing file.
     *
     * @param outputDir the target directory
     * @param resourcePath the classpath-relative resource path
     * @param replace {@code true} to overwrite an existing file, {@code false} to throw if it exists
     * @throws IllegalStateException if the file already exists and {@code replace} is {@code false},
     *                               or if parent directories cannot be created
     */
    public static void saveResource(@NotNull File outputDir, @NotNull String resourcePath, boolean replace) {
        saveResource(outputDir, resourcePath, "", replace);
    }

    /**
     * Saves a classpath resource to a child subdirectory of the given output directory,
     * optionally replacing an existing file.
     *
     * @param outputDir the base output directory
     * @param resourcePath the classpath-relative resource path
     * @param child an optional subdirectory name within {@code outputDir}
     * @param replace {@code true} to overwrite an existing file, {@code false} to throw if it exists
     * @throws IllegalStateException if the file already exists and {@code replace} is {@code false},
     *                               or if parent directories cannot be created
     */
    @SuppressWarnings("all")
    public static void saveResource(@NotNull File outputDir, @NotNull String resourcePath, @Nullable String child, boolean replace) {
        File directory = outputDir;

        if (StringUtil.isNotEmpty(child))
            directory = new File(directory, child);

        File output = new File(directory, resourcePath);

        try (InputStream inputStream = getResource(resourcePath)) {
            if (!directory.exists()) {
                if (!directory.mkdirs())
                    throw new IllegalStateException(String.format("Unable to create parent directories for '%s'", output));
            }

            if (replace)
                output.delete();
            else if (output.exists())
                throw new IllegalStateException(String.format("Output file '%s' already exists", output));

            try (FileOutputStream outputStream = new FileOutputStream(output)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, length);
            }
        } catch (Exception exception) {
            throw new IllegalStateException(String.format("Unable to save resource '%s' to '%s'", resourcePath, output), exception);
        }
    }

}

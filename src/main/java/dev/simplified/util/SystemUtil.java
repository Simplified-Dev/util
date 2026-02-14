package dev.sbs.api.util;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.collection.concurrent.Concurrent;
import dev.sbs.api.collection.concurrent.ConcurrentMap;
import dev.sbs.api.stream.pair.Pair;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * <p>
 * Helpers for {@code java.lang.System}.
 * </p>
 * <p>
 * If a system property cannot be read due to security restrictions, the corresponding field in this class will be set
 * to {@code null} and a message will be written to {@code System.err}.
 * </p>
 * <p>
 * These values are initialized when the class is loaded. If {@link System#setProperty(String,String)} or
 * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, that property will be out of
 * sync with the cached values.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 */
@UtilityClass
public final class SystemUtil {

    /**
     * The System property key for the user home directory.
     */
    private static final String USER_HOME_KEY = "user.home";

    /**
     * The System property key for the user directory.
     */
    private static final String USER_DIR_KEY = "user.dir";

    /**
     * The System property key for the Java IO temporary directory.
     */
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";

    /**
     * The System property key for the Java home directory.
     */
    private static final String JAVA_HOME_KEY = "java.home";

    /**
     * The {@code file.encoding} System Property. Such as {@code Cp1252}.
     */
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");

    /**
     * The {@code file.separator} System Property. File separator (<code>&quot;/&quot;</code> on UNIX).
     */
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

    /**
     * The {@code java.awt.headless} System Property. The value of this property is the String {@code "true"} or {@code "false"}.
     */
    private static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

    /**
     * The {@code java.home} System Property. Java installation directory.
     */
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);

    /**
     * The {@code java.io.tmpdir} System Property. Default temp file path.
     */
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);

    /**
     * The {@code java.class.path} System Property. Java class path.
     */
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

    /**
     * The {@code java.library.path} System Property. List of paths to search when loading libraries.
     */
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

    /**
     * The {@code java.runtime.name} System Property. Java Runtime Environment name.
     */
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

    /**
     * The {@code java.runtime.version} System Property. Java Runtime Environment version.
     */
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

    /**
     * The {@code java.specification.name} System Property. Java Runtime Environment specification name.
     */
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

    /**
     * The {@code java.specification.vendor} System Property. Java Runtime Environment specification vendor.
     */
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

    /**
     * The {@code java.specification.version} System Property. Java Runtime Environment specification version.
     */
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

    /**
     * The {@code java.util.prefs.PreferencesFactory} System Property. A class name.
     */
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");

    /**
     * The {@code java.vendor} System Property. Java vendor-specific string.
     */
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

    /**
     * The {@code java.vendor.url} System Property. Java vendor URL.
     */
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

    /**
     * The {@code java.version} System Property. Java version number.
     */
    public static final String JAVA_VERSION = getSystemProperty("java.version");

    /**
     * The {@code java.vm.info} System Property. Java Virtual Machine implementation info.
     */
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

    /**
     * The {@code java.vm.name} System Property. Java Virtual Machine implementation name.
     */
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

    /**
     * The {@code java.vm.specification.name} System Property. Java Virtual Machine specification name.
     */
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

    /**
     * The {@code java.vm.specification.vendor} System Property. Java Virtual Machine specification vendor.
     */
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

    /**
     * The {@code java.vm.specification.version} System Property. Java Virtual Machine specification version.
     */
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

    /**
     * The {@code java.vm.vendor} System Property. Java Virtual Machine implementation vendor.
     */
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

    /**
     * The {@code java.vm.version} System Property. Java Virtual Machine implementation version.
     */
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    /**
     * The {@code line.separator} System Property. Line separator (<code>&quot;\n&quot;</code> on UNIX).
     */
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

    /**
     * <p>
     * The {@code path.separator} System Property. Path separator (<code>&quot;:&quot;</code> on UNIX).
     * </p>
     */
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    /**
     * The {@code user.dir} System Property. User's current working directory.
     */
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);

    /**
     * The {@code user.home} System Property. User's home directory.
     */
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);

    /**
     * The {@code user.language} System Property. User's language code, such as {@code "en"}.
     */
    public static final String USER_LANGUAGE = getSystemProperty("user.language");

    /**
     * The {@code user.name} System Property. User's account name.
     */
    public static final String USER_NAME = getSystemProperty("user.name");

    /**
     * The {@code user.timezone} System Property. For example: {@code "America/Los_Angeles"}.
     */
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    /**
     * Retrieves a map of environment variables from the `.env` file in the resources folder and the
     * operating system environment variables. Variables defined in the `.env` file are overridden by those
     * from the operating system if duplicates exist.
     */
    @Getter
    private static @NotNull ConcurrentMap<String, String> env = loadEnvironmentVariables().toUnmodifiableMap();

    /**
     * Gets the Java home directory as a {@code File}.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    /**
     * Gets the Java IO temporary directory as a {@code File}.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    /**
     * Gets the user directory as a {@code File}.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    /**
     * Gets the user home directory as a {@code File}.
     *
     * @return a directory
     * @throws SecurityException if a security manager exists and its {@code checkPropertyAccess} method doesn't allow
     * access to the specified system property.
     * @see System#getProperty(String)
     */
    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    /**
     * Returns whether the {@link #JAVA_AWT_HEADLESS} value is {@code true}.
     *
     * @return {@code true} if {@code JAVA_AWT_HEADLESS} is {@code "true"}, {@code false} otherwise.
     * @see #JAVA_AWT_HEADLESS
     */
    public static boolean isJavaAwtHeadless() {
        return JAVA_AWT_HEADLESS != null && JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString());
    }

    @SneakyThrows
    public static @NotNull InetAddress getPreferredAddress() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress();
        }
    }

    /**
     * Gets a System property, defaulting to {@code null} if the property cannot be read.
     * <p>
     * If a {@code SecurityException} is caught, the return value is {@code null} and a message is written to
     * {@code System.err}.
     *
     * @param property the system property name
     * @return the system property value or {@code null} if a security problem occurs
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

    private static @NotNull ConcurrentMap<String, String> readEnvironmentFile(@Nullable InputStream inputStream) {
        ConcurrentMap<String, String> variables = Concurrent.newMap();

        if (inputStream != null) {
            Scanner scanner = new Scanner(inputStream);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("=")) {
                    String[] pair = line.split("=");
                    variables.put(pair[0], pair.length == 2 ? pair[1] : "");
                }
            }
        }

        return variables;
    }

    private static @NotNull ConcurrentMap<String, String> loadEnvironmentVariables() {
        ConcurrentMap<String, String> variables = Concurrent.newMap();

        // Load src/main/resources/.env
        try {
            @Cleanup InputStream resourceFile = getResource("../.env");
            variables.putAll(readEnvironmentFile(resourceFile));
        } catch (Exception ignore) { }

        // Load <working directory>/.env
        try {
            @Cleanup InputStream localFile = new FileInputStream(SimplifiedApi.getCurrentDirectory() + FILE_SEPARATOR + ".env");
            variables.putAll(readEnvironmentFile(localFile));
        } catch (Exception ignore) { }

        // Override From OS
        variables.putAll(System.getenv());
        return variables;
    }

    /**
     * Get environment variable.
     *
     * @param variableName the name of the environment variable
     * @return the value of the environment variable
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
     * Get environment variable.
     *
     * @param variableName the name of the environment variable
     * @return the key-value of the environment variable
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

    public static @Nullable InputStream getResource(@NotNull String resourcePath) {
        return ClassUtil.getClassLoader(SystemUtil.class).getResourceAsStream(
            RegexUtil.replaceFirst(resourcePath, "^resources/", "").replaceFirst("^/", "")
        );
    }

    public static @NotNull List<String> getResourceFiles(@NotNull String resourcePath) {
        List<String> fileNames = new ArrayList<>();

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

    @SneakyThrows
    public static byte[] readResource(@NotNull String resourcePath) {
        @Cleanup InputStream inputStream = getResource(resourcePath);
        return inputStream != null ? inputStream.readAllBytes() : new byte[0];
    }

    public static void saveResource(@NotNull File outputDir, @NotNull String resourcePath, boolean replace) {
        saveResource(outputDir, resourcePath, "", replace);
    }

    @SuppressWarnings("all")
    public static void saveResource(@NotNull File outputDir, @NotNull String resourcePath, @Nullable String child, boolean replace) {
        File directory = outputDir;

        if (StringUtil.isNotEmpty(child))
            directory = new File(directory, child);

        File output = new File(directory, resourcePath);

        try (InputStream inputStream = getResource(resourcePath)) {
            if (!directory.exists()) {
                if (!directory.mkdirs())
                    throw new IllegalStateException(String.format("Unable to create parent directories for '%s'.", output));
            }

            if (replace)
                output.delete();
            else if (output.exists())
                throw new IllegalStateException(String.format("Output file '%s' already exists.", output));

            try (FileOutputStream outputStream = new FileOutputStream(output)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, length);
            }
        } catch (Exception exception) {
            throw new IllegalStateException(String.format("Unable to save resource '%s' to '%s'.", resourcePath, output), exception);
        }
    }

}

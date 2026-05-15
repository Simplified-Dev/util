package dev.simplified.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Backend-agnostic logging facade for programmatic log-level inspection and mutation.
 * <p>
 * Detects the active logging implementation at class-load time via reflection, requiring
 * no compile-time dependency on any logging framework. Supported backends, checked in order:
 * <ol>
 *   <li><b>Log4j2 Core</b> - detects {@code LoggerContext} and delegates to
 *       {@code Configurator.setLevel} and {@code LogManager.getLogger().getLevel()}</li>
 *   <li><b>SLF4J / Logback</b> - detects {@code ch.qos.logback.classic.LoggerContext}
 *       via SLF4J's {@code ILoggerFactory} and delegates to Logback's
 *       {@code Logger.setLevel/getEffectiveLevel}</li>
 *   <li><b>JUL</b> ({@code java.util.logging}) - always available as a fallback, using
 *       the JDK's built-in logging API directly</li>
 * </ol>
 * <p>
 * The {@link Level} enum provides a unified set of log levels that are mapped to each
 * backend's native level type automatically.
 *
 * @see Level
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Logging {

    private static final @NotNull Bridge BRIDGE;

    static {
        Bridge detected = null;

        try {
            detected = new Log4j2Bridge();
        } catch (Exception ignored) {
            try {
                detected = new LogbackBridge();
            } catch (Exception ignored2) {
                // JUL is always available
            }
        }

        BRIDGE = detected != null ? detected : new JulBridge();
    }

    /**
     * Sets the log level for the named logger.
     *
     * @param loggerName the fully-qualified logger name
     * @param level the log level to set
     */
    public static void setLevel(@NotNull String loggerName, @NotNull Level level) {
        BRIDGE.setLevel(loggerName, level);
    }

    /**
     * Sets the log level for the named logger using a level name string.
     * <p>
     * The string is resolved via {@link Level#of(String)}. Unrecognized names
     * default to {@link Level#INFO}.
     *
     * @param loggerName the fully-qualified logger name
     * @param level the log level name (e.g. "DEBUG", "WARN")
     */
    public static void setLevel(@NotNull String loggerName, @NotNull String level) {
        BRIDGE.setLevel(loggerName, Level.of(level));
    }

    /**
     * Sets the root logger level.
     *
     * @param level the log level to set
     */
    public static void setRootLevel(@NotNull Level level) {
        BRIDGE.setRootLevel(level);
    }

    /**
     * Sets the root logger level using a level name string.
     *
     * @param level the log level name (e.g. "DEBUG", "WARN")
     */
    public static void setRootLevel(@NotNull String level) {
        BRIDGE.setRootLevel(Level.of(level));
    }

    /**
     * Returns the effective log level for the named logger.
     * <p>
     * If the logger has no explicit level set, the effective level inherited from its
     * parent logger chain is returned.
     *
     * @param loggerName the fully-qualified logger name
     * @return the effective log level, or {@link Level#INFO} if it cannot be determined
     */
    public static @NotNull Level getLevel(@NotNull String loggerName) {
        return BRIDGE.getLevel(loggerName);
    }

    /**
     * Returns the effective root logger level.
     *
     * @return the root log level, or {@link Level#INFO} if it cannot be determined
     */
    public static @NotNull Level getRootLevel() {
        return BRIDGE.getRootLevel();
    }

    /**
     * Returns the name of the detected logging backend.
     *
     * @return one of {@code "Log4j2"}, {@code "Logback"}, or {@code "JUL"}
     */
    public static @NotNull String getBackend() {
        return BRIDGE.name();
    }

    /**
     * Unified log level abstraction ordered from least verbose ({@link #OFF}) to most
     * verbose ({@link #ALL}).
     * <p>
     * Each constant maps to the equivalent level in Log4j2, Logback/SLF4J, and JUL.
     * Use {@link #includes(Level)} to check whether a configured level encompasses a
     * given severity.
     */
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Level {

        /**
         * Disables all logging output.
         */
        OFF("OFF", "OFF", "OFF"),
        /**
         * Severe errors that may cause premature termination.
         */
        FATAL("FATAL", "ERROR", "SEVERE"),
        /**
         * Errors that might still allow the application to continue.
         */
        ERROR("ERROR", "ERROR", "SEVERE"),
        /**
         * Potentially harmful situations.
         */
        WARN("WARN", "WARN", "WARNING"),
        /**
         * Informational messages highlighting progress at a coarse level.
         */
        INFO("INFO", "INFO", "INFO"),
        /**
         * Fine-grained informational events useful for debugging.
         */
        DEBUG("DEBUG", "DEBUG", "FINE"),
        /**
         * Most detailed informational events.
         */
        TRACE("TRACE", "TRACE", "FINEST"),
        /**
         * Enables all logging output.
         */
        ALL("ALL", "ALL", "ALL");

        /**
         * Level name used by Log4j2.
         */
        private final @NotNull String log4j2Name;
        /**
         * Level name used by Logback/SLF4J.
         */
        private final @NotNull String logbackName;
        /**
         * Level name used by {@code java.util.logging}.
         */
        private final @NotNull String julName;

        /**
         * Checks whether this level is at least as verbose as the given level.
         * <p>
         * A level "includes" another when its ordinal is greater than or equal to the
         * other's. For example, {@code DEBUG.includes(WARN)} returns {@code true}
         * because debug output includes all warnings.
         *
         * @param level the level to compare against
         * @return {@code true} if this level encompasses {@code level}
         */
        public boolean includes(@NotNull Level level) {
            return this.ordinal() >= level.ordinal();
        }

        /**
         * Resolves a level from a case-insensitive name string.
         * <p>
         * Matches against the enum constant name and all backend-specific aliases.
         * Returns {@link #INFO} if the name is not recognized.
         *
         * @param name the level name to resolve
         * @return the matching level, or {@link #INFO} as default
         */
        public static @NotNull Level of(@NotNull String name) {
            String upper = name.toUpperCase();

            for (Level level : values())
                if (level.name().equals(upper) || level.log4j2Name.equals(upper) || level.logbackName.equals(upper) || level.julName.equals(upper))
                    return level;

            return INFO;
        }

    }

    // ──── Bridge Abstraction ────

    private interface Bridge {

        void setLevel(@NotNull String loggerName, @NotNull Level level);

        void setRootLevel(@NotNull Level level);

        @NotNull Level getLevel(@NotNull String loggerName);

        @NotNull Level getRootLevel();

        @NotNull String name();

    }

    // ──── Log4j2 Core ────

    /**
     * Cached Log4j2 Core reflection bridge that detects {@code LoggerContext} and delegates
     * to {@code Configurator} for level changes and {@code LogManager} for level queries.
     */
    private static final class Log4j2Bridge implements Bridge {

        private final @NotNull Method getLevelByName;
        private final @NotNull Method setLevelMethod;
        private final @NotNull Method setRootLevelMethod;
        private final @NotNull Method getLoggerMethod;
        private final @NotNull Method getLoggerLevelMethod;
        private final @NotNull Method levelNameMethod;

        private Log4j2Bridge() throws Exception {
            Class<?> logManager = Class.forName("org.apache.logging.log4j.LogManager");
            Class<?> loggerContextClass = Class.forName("org.apache.logging.log4j.core.LoggerContext");
            Method getContext = logManager.getMethod("getContext", boolean.class);
            Object context = getContext.invoke(null, false);

            if (!loggerContextClass.isInstance(context))
                throw new IllegalStateException("Not a Log4j2 Core context");

            Class<?> levelClass = Class.forName("org.apache.logging.log4j.Level");
            Class<?> configuratorClass = Class.forName("org.apache.logging.log4j.core.config.Configurator");
            Class<?> loggerClass = Class.forName("org.apache.logging.log4j.Logger");

            this.getLevelByName = levelClass.getMethod("getLevel", String.class);
            this.setLevelMethod = configuratorClass.getMethod("setLevel", String.class, levelClass);
            this.setRootLevelMethod = configuratorClass.getMethod("setRootLevel", levelClass);
            this.getLoggerMethod = logManager.getMethod("getLogger", String.class);
            this.getLoggerLevelMethod = loggerClass.getMethod("getLevel");
            this.levelNameMethod = levelClass.getMethod("name");
        }

        @Override
        public void setLevel(@NotNull String loggerName, @NotNull Level level) {
            try {
                Object nativeLevel = this.getLevelByName.invoke(null, level.getLog4j2Name());
                this.setLevelMethod.invoke(null, loggerName, nativeLevel);
            } catch (ReflectiveOperationException ignored) { }
        }

        @Override
        public void setRootLevel(@NotNull Level level) {
            try {
                Object nativeLevel = this.getLevelByName.invoke(null, level.getLog4j2Name());
                this.setRootLevelMethod.invoke(null, nativeLevel);
            } catch (ReflectiveOperationException ignored) { }
        }

        @Override
        public @NotNull Level getLevel(@NotNull String loggerName) {
            try {
                Object logger = this.getLoggerMethod.invoke(null, loggerName);
                Object nativeLevel = this.getLoggerLevelMethod.invoke(logger);

                if (nativeLevel != null)
                    return Level.of((String) this.levelNameMethod.invoke(nativeLevel));
            } catch (ReflectiveOperationException ignored) { }

            return Level.INFO;
        }

        @Override
        public @NotNull Level getRootLevel() {
            return getLevel("");
        }

        @Override
        public @NotNull String name() {
            return "Log4j2";
        }

    }

    // ──── Logback (via SLF4J) ────

    /**
     * Cached Logback reflection bridge that resolves method accessors once at construction time.
     */
    private static final class LogbackBridge implements Bridge {

        private final @NotNull Object factory;
        private final @NotNull Method getLogger;
        private final @NotNull Method toLevel;
        private final @NotNull Method setLevelMethod;
        private final @NotNull Method getEffectiveLevel;
        private final @NotNull Method levelToString;

        private LogbackBridge() throws Exception {
            Class<?> loggerFactoryClass = Class.forName("org.slf4j.LoggerFactory");
            Method getILoggerFactory = loggerFactoryClass.getMethod("getILoggerFactory");
            this.factory = getILoggerFactory.invoke(null);

            if (!"ch.qos.logback.classic.LoggerContext".equals(this.factory.getClass().getName()))
                throw new IllegalStateException("Not a Logback LoggerContext");

            Class<?> levelClass = Class.forName("ch.qos.logback.classic.Level");
            Class<?> loggerClass = Class.forName("ch.qos.logback.classic.Logger");

            this.getLogger = this.factory.getClass().getMethod("getLogger", String.class);
            this.toLevel = levelClass.getMethod("toLevel", String.class);
            this.setLevelMethod = loggerClass.getMethod("setLevel", levelClass);
            this.getEffectiveLevel = loggerClass.getMethod("getEffectiveLevel");
            this.levelToString = levelClass.getMethod("toString");
        }

        @Override
        public void setLevel(@NotNull String loggerName, @NotNull Level level) {
            try {
                Object logger = this.getLogger.invoke(this.factory, loggerName);
                Object logbackLevel = this.toLevel.invoke(null, level.getLogbackName());
                this.setLevelMethod.invoke(logger, logbackLevel);
            } catch (ReflectiveOperationException ignored) { }
        }

        @Override
        public void setRootLevel(@NotNull Level level) {
            setLevel("ROOT", level);
        }

        @Override
        public @NotNull Level getLevel(@NotNull String loggerName) {
            try {
                Object logger = this.getLogger.invoke(this.factory, loggerName);
                Object effectiveLevel = this.getEffectiveLevel.invoke(logger);

                if (effectiveLevel != null)
                    return Level.of((String) this.levelToString.invoke(effectiveLevel));
            } catch (ReflectiveOperationException ignored) { }

            return Level.INFO;
        }

        @Override
        public @NotNull Level getRootLevel() {
            return getLevel("ROOT");
        }

        @Override
        public @NotNull String name() {
            return "Logback";
        }

    }

    // ──── JUL (java.util.logging) ────

    /**
     * Fallback bridge using {@code java.util.logging}, always available on the JDK.
     */
    private static final class JulBridge implements Bridge {

        private static final Map<String, java.util.logging.Level> TO_JUL = Map.of(
            "OFF", java.util.logging.Level.OFF,
            "SEVERE", java.util.logging.Level.SEVERE,
            "WARNING", java.util.logging.Level.WARNING,
            "INFO", java.util.logging.Level.INFO,
            "FINE", java.util.logging.Level.FINE,
            "FINEST", java.util.logging.Level.FINEST,
            "ALL", java.util.logging.Level.ALL
        );

        @Override
        public void setLevel(@NotNull String loggerName, @NotNull Level level) {
            Logger logger = Logger.getLogger(loggerName);
            logger.setLevel(TO_JUL.getOrDefault(level.getJulName(), java.util.logging.Level.INFO));
        }

        @Override
        public void setRootLevel(@NotNull Level level) {
            setLevel("", level);
        }

        @Override
        public @NotNull Level getLevel(@NotNull String loggerName) {
            Logger logger = Logger.getLogger(loggerName);
            java.util.logging.Level julLevel = logger.getLevel();

            while (julLevel == null && logger.getParent() != null) {
                logger = logger.getParent();
                julLevel = logger.getLevel();
            }

            if (julLevel == null)
                return Level.INFO;

            return Level.of(julLevel.getName());
        }

        @Override
        public @NotNull Level getRootLevel() {
            return getLevel("");
        }

        @Override
        public @NotNull String name() {
            return "JUL";
        }

    }

}

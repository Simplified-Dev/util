package dev.simplified.util;

import lombok.experimental.UtilityClass;
import org.intellij.lang.annotations.Language;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Null-safe utility methods for processing strings using regular expressions.
 *
 * @see Pattern
 */
@UtilityClass
public final class RegexUtil {

    private static final LinkedHashMap<String, String> CACHED_COLOR_MESSAGES = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > 100;
        }
    };
    private static final LastCharCompare CODE_COMPARE = new LastCharCompare();

    // Minecraft Chat Formatting
    public static final String SECTOR_SYMBOL = "\u00a7";
    private static final String ALL_PATTERN = "[0-9A-FK-ORa-fk-or]";
    private static final Pattern REPLACE_PATTERN = Pattern.compile("&&(?=" + ALL_PATTERN + ")");
    public static final Pattern VANILLA_PATTERN = Pattern.compile(SECTOR_SYMBOL + "+(" + ALL_PATTERN + ")");

    static {
        CODE_COMPARE.addIgnoreCharacter('r');
    }

    /**
     * Replaces each {@link #SECTOR_SYMBOL} in the given message with a double ampersand ({@code &&}),
     * caching the result for subsequent lookups.
     *
     * @param message the message in which to replace sector symbols
     * @return the message with sector symbols replaced by {@code &&}
     */
    public static String lameColor(String message) {
        if (!CACHED_COLOR_MESSAGES.containsKey(message))
            CACHED_COLOR_MESSAGES.put(message, message.replace(SECTOR_SYMBOL, "&&"));

        return CACHED_COLOR_MESSAGES.get(message);
    }

    /**
     * Removes each substring of the text that matches the given compiled regular expression pattern.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.removeAll(null, *)      = null
     * RegexUtil.removeAll("any", (Pattern) null)  = "any"
     * RegexUtil.removeAll("any", Pattern.compile(""))    = "any"
     * RegexUtil.removeAll("any", Pattern.compile(".*"))  = ""
     * RegexUtil.removeAll("any", Pattern.compile(".+"))  = ""
     * RegexUtil.removeAll("abc", Pattern.compile(".?"))  = ""
     * RegexUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\nB"
     * RegexUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * RegexUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL))  = "AB"
     * RegexUtil.removeAll("ABCabc123abc", Pattern.compile("[a-z]"))     = "ABC123"
     * </pre>
     *
     * @param text the text to remove from, may be null
     * @param regex the compiled regular expression pattern to match
     * @return the text with all matches removed, or {@code null} if null string input
     * @see #replaceAll(String, Pattern, String)
     * @see Matcher#replaceAll(String)
     * @see Pattern
     */
    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * Removes each substring of the text that matches the given regular expression string.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <p>
     * Unlike in the {@link #removePattern(String, String)} method, the {@link Pattern#DOTALL} option
     * is NOT automatically added. To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.
     *
     * <pre>
     * RegexUtil.removeAll(null, *)      = null
     * RegexUtil.removeAll("any", (String) null)  = "any"
     * RegexUtil.removeAll("any", "")    = "any"
     * RegexUtil.removeAll("any", ".*")  = ""
     * RegexUtil.removeAll("any", ".+")  = ""
     * RegexUtil.removeAll("abc", ".?")  = ""
     * RegexUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\nB"
     * RegexUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * RegexUtil.removeAll("ABCabc123abc", "[a-z]")     = "ABC123"
     * </pre>
     *
     * @param text the text to remove from, may be null
     * @param regex the regular expression string to match
     * @return the text with all matches removed, or {@code null} if null string input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replaceAll(String, String, String)
     * @see #removePattern(String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern
     * @see Pattern#DOTALL
     */
    public static String removeAll(final String text, @Language("RegExp") final String regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * Removes the first substring of the text that matches the given compiled regular expression pattern.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.removeFirst(null, *)      = null
     * RegexUtil.removeFirst("any", (Pattern) null)  = "any"
     * RegexUtil.removeFirst("any", Pattern.compile(""))    = "any"
     * RegexUtil.removeFirst("any", Pattern.compile(".*"))  = ""
     * RegexUtil.removeFirst("any", Pattern.compile(".+"))  = ""
     * RegexUtil.removeFirst("abc", Pattern.compile(".?"))  = "bc"
     * RegexUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\n&lt;__&gt;B"
     * RegexUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * RegexUtil.removeFirst("ABCabc123", Pattern.compile("[a-z]"))          = "ABCbc123"
     * RegexUtil.removeFirst("ABCabc123abc", Pattern.compile("[a-z]+"))      = "ABC123abc"
     * </pre>
     *
     * @param text the text to remove from, may be null
     * @param regex the compiled regular expression pattern to match
     * @return the text with the first match removed, or {@code null} if null string input
     * @see #replaceFirst(String, Pattern, String)
     * @see Matcher#replaceFirst(String)
     * @see Pattern
     */
    public static String removeFirst(final String text, final Pattern regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * Removes the first substring of the text that matches the given regular expression string.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <p>
     * The {@link Pattern#DOTALL} option is NOT automatically added. To use the DOTALL option
     * prepend {@code "(?s)"} to the regex. DOTALL is also known as single-line mode in Perl.
     *
     * <pre>
     * RegexUtil.removeFirst(null, *)      = null
     * RegexUtil.removeFirst("any", (String) null)  = "any"
     * RegexUtil.removeFirst("any", "")    = "any"
     * RegexUtil.removeFirst("any", ".*")  = ""
     * RegexUtil.removeFirst("any", ".+")  = ""
     * RegexUtil.removeFirst("abc", ".?")  = "bc"
     * RegexUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\n&lt;__&gt;B"
     * RegexUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * RegexUtil.removeFirst("ABCabc123", "[a-z]")          = "ABCbc123"
     * RegexUtil.removeFirst("ABCabc123abc", "[a-z]+")      = "ABC123abc"
     * </pre>
     *
     * @param text the text to remove from, may be null
     * @param regex the regular expression string to match
     * @return the text with the first match removed, or {@code null} if null string input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replaceFirst(String, String, String)
     * @see String#replaceFirst(String, String)
     * @see Pattern
     * @see Pattern#DOTALL
     */
    public static String removeFirst(final String text, @Language("RegExp") final String regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * Removes each substring of the text that matches the given regular expression
     * using the {@link Pattern#DOTALL} option.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll("(?s)" + regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.removePattern(null, *)       = null
     * RegexUtil.removePattern("any", (String) null)   = "any"
     * RegexUtil.removePattern("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")  = "AB"
     * RegexUtil.removePattern("ABCabc123", "[a-z]")    = "ABC123"
     * </pre>
     *
     * @param text the source text, may be null
     * @param regex the regular expression to match
     * @return the text with all matches removed, or {@code null} if null string input
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String removePattern(final String text, final String regex) {
        return replacePattern(text, regex, StringUtil.EMPTY);
    }

    /**
     * Replaces each substring of the text that matches the given compiled regular expression
     * pattern with the first captured group ({@code $1}).
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll("$1")}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.replaceAll(null, *)       = null
     * RegexUtil.replaceAll("any", (Pattern) null)   = "any"
     * RegexUtil.replaceAll("any", *)   = "any"
     * </pre>
     *
     * @param text the text to search and replace in, may be null
     * @param regex the compiled regular expression pattern to match
     * @return the text with matches replaced by the first captured group, or {@code null} if null string input
     * @see Matcher#replaceAll(String)
     * @see Pattern
     */
    public static String replaceAll(String text, Pattern regex) {
        return replaceAll(text, regex, "$1");
    }

    /**
     * Replaces each substring of the text that matches the given compiled regular expression
     * pattern with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.replaceAll(null, *, *)       = null
     * RegexUtil.replaceAll("any", (Pattern) null, *)   = "any"
     * RegexUtil.replaceAll("any", *, null)   = "any"
     * RegexUtil.replaceAll("", Pattern.compile(""), "zzz")    = "zzz"
     * RegexUtil.replaceAll("", Pattern.compile(".*"), "zzz")  = "zzz"
     * RegexUtil.replaceAll("", Pattern.compile(".+"), "zzz")  = ""
     * RegexUtil.replaceAll("abc", Pattern.compile(""), "ZZ")  = "ZZaZZbZZcZZ"
     * RegexUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")                 = "z\nz"
     * RegexUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL), "z") = "z"
     * RegexUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")             = "z"
     * RegexUtil.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_")       = "ABC___123"
     * RegexUtil.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123"
     * RegexUtil.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123"
     * RegexUtil.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text the text to search and replace in, may be null
     * @param regex the compiled regular expression pattern to match
     * @param replacement the string to substitute for each match
     * @return the text with all replacements applied, or {@code null} if null string input
     * @see Matcher#replaceAll(String)
     * @see Pattern
     */
    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null)
            return text;

        return regex.matcher(text).replaceAll(replacement);
    }

    /**
     * Replaces each substring of the text that matches the given regular expression string
     * with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <p>
     * Unlike in the {@link #replacePattern(String, String, String)} method, the {@link Pattern#DOTALL}
     * option is NOT automatically added. To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.
     *
     * <pre>
     * RegexUtil.replaceAll(null, *, *)       = null
     * RegexUtil.replaceAll("any", (String) null, *)   = "any"
     * RegexUtil.replaceAll("any", *, null)   = "any"
     * RegexUtil.replaceAll("", "", "zzz")    = "zzz"
     * RegexUtil.replaceAll("", ".*", "zzz")  = "zzz"
     * RegexUtil.replaceAll("", ".+", "zzz")  = ""
     * RegexUtil.replaceAll("abc", "", "ZZ")  = "ZZaZZbZZcZZ"
     * RegexUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\nz"
     * RegexUtil.replaceAll("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
     * RegexUtil.replaceAll("ABCabc123", "[a-z]", "_")       = "ABC___123"
     * RegexUtil.replaceAll("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
     * RegexUtil.replaceAll("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
     * RegexUtil.replaceAll("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text the text to search and replace in, may be null
     * @param regex the regular expression string to match
     * @param replacement the string to substitute for each match
     * @return the text with all replacements applied, or {@code null} if null string input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern
     * @see Pattern#DOTALL
     */
    public static String replaceAll(final String text, @Language("RegExp") final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null)
            return text;

        return text.replaceAll(regex, replacement);
    }

    /**
     * Replaces the first substring of the text that matches the given compiled regular expression
     * pattern with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.replaceFirst(null, *, *)       = null
     * RegexUtil.replaceFirst("any", (Pattern) null, *)   = "any"
     * RegexUtil.replaceFirst("any", *, null)   = "any"
     * RegexUtil.replaceFirst("", Pattern.compile(""), "zzz")    = "zzz"
     * RegexUtil.replaceFirst("", Pattern.compile(".*"), "zzz")  = "zzz"
     * RegexUtil.replaceFirst("", Pattern.compile(".+"), "zzz")  = ""
     * RegexUtil.replaceFirst("abc", Pattern.compile(""), "ZZ")  = "ZZabc"
     * RegexUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")      = "z\n&lt;__&gt;"
     * RegexUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")  = "z"
     * RegexUtil.replaceFirst("ABCabc123", Pattern.compile("[a-z]"), "_")          = "ABC_bc123"
     * RegexUtil.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123abc"
     * RegexUtil.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123abc"
     * RegexUtil.replaceFirst("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum  dolor   sit"
     * </pre>
     *
     * @param text the text to search and replace in, may be null
     * @param regex the compiled regular expression pattern to match
     * @param replacement the string to substitute for the first match
     * @return the text with the first replacement applied, or {@code null} if null string input
     * @see Matcher#replaceFirst(String)
     * @see Pattern
     */
    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    /**
     * Replaces the first substring of the text that matches the given regular expression string
     * with the given replacement.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <p>
     * The {@link Pattern#DOTALL} option is NOT automatically added. To use the DOTALL option
     * prepend {@code "(?s)"} to the regex. DOTALL is also known as single-line mode in Perl.
     *
     * <pre>
     * RegexUtil.replaceFirst(null, *, *)       = null
     * RegexUtil.replaceFirst("any", (String) null, *)   = "any"
     * RegexUtil.replaceFirst("any", *, null)   = "any"
     * RegexUtil.replaceFirst("", "", "zzz")    = "zzz"
     * RegexUtil.replaceFirst("", ".*", "zzz")  = "zzz"
     * RegexUtil.replaceFirst("", ".+", "zzz")  = ""
     * RegexUtil.replaceFirst("abc", "", "ZZ")  = "ZZabc"
     * RegexUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\n&lt;__&gt;"
     * RegexUtil.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
     * RegexUtil.replaceFirst("ABCabc123", "[a-z]", "_")          = "ABC_bc123"
     * RegexUtil.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "_")  = "ABC_123abc"
     * RegexUtil.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "")   = "ABC123abc"
     * RegexUtil.replaceFirst("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum  dolor   sit"
     * </pre>
     *
     * @param text the text to search and replace in, may be null
     * @param regex the regular expression string to match
     * @param replacement the string to substitute for the first match
     * @return the text with the first replacement applied, or {@code null} if null string input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see String#replaceFirst(String, String)
     * @see Pattern
     * @see Pattern#DOTALL
     */
    public static String replaceFirst(final String text, @Language("RegExp") final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return text.replaceFirst(regex, replacement);
    }

    /**
     * Replaces each substring of the text that matches the given regular expression
     * with the given replacement using the {@link Pattern#DOTALL} option. DOTALL is also known
     * as single-line mode in Perl.
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll("(?s)" + regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>
     * A {@code null} reference passed to this method is a no-op.
     *
     * <pre>
     * RegexUtil.replacePattern(null, *, *)       = null
     * RegexUtil.replacePattern("any", (String) null, *)   = "any"
     * RegexUtil.replacePattern("any", *, null)   = "any"
     * RegexUtil.replacePattern("", "", "zzz")    = "zzz"
     * RegexUtil.replacePattern("", ".*", "zzz")  = "zzz"
     * RegexUtil.replacePattern("", ".+", "zzz")  = ""
     * RegexUtil.replacePattern("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")       = "z"
     * RegexUtil.replacePattern("ABCabc123", "[a-z]", "_")       = "ABC___123"
     * RegexUtil.replacePattern("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
     * RegexUtil.replacePattern("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
     * RegexUtil.replacePattern("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
     * </pre>
     *
     * @param text the source text, may be null
     * @param regex the regular expression to match
     * @param replacement the string to substitute for each match
     * @return the text with all replacements applied, or {@code null} if null string input
     * @see #replaceAll(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String replacePattern(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null)
            return text;

        return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
    }

    /**
     * Replaces color codes in the given message, sorting and normalizing them according to the
     * given pattern. Results are cached for repeated lookups of the same message.
     *
     * @param message the message containing color codes to process
     * @param pattern the compiled regular expression pattern for color code matching
     * @return the message with color codes replaced and normalized
     */
    public static String replaceColor(String message, Pattern pattern) {
        if (!CACHED_COLOR_MESSAGES.containsKey(message)) {
            Pattern patternx = Pattern.compile(String.format("(((?:[&%s]%s)%s)+)([^&%1$s]*)", SECTOR_SYMBOL, "{1,2}", ALL_PATTERN));
            String[] parts = StringUtil.split(" ", message);
            String newMessage = message;

            for (String part : parts) {
                Matcher matcher = patternx.matcher(part);
                String newPart = part;

                while (matcher.find()) {
                    String[] codes = matcher.group(1).split(String.format("(?<!&|%s)", SECTOR_SYMBOL));
                    Arrays.sort(codes, CODE_COMPARE);
                    String replace = String.format("%s%s", StringUtil.join(codes), matcher.group(3));
                    newPart = newPart.replace(matcher.group(0), replace);
                }

                newMessage = newMessage.replace(part, newPart);
            }

            CACHED_COLOR_MESSAGES.put(message, newMessage);
        }

        return replaceAll(replaceAll(CACHED_COLOR_MESSAGES.get(message), pattern, RegexUtil.SECTOR_SYMBOL + "$1"), REPLACE_PATTERN, "&");
    }

    /**
     * Removes all substrings matching the given compiled pattern from the message.
     *
     * @param message the message to strip
     * @param pattern the compiled regular expression pattern to remove
     * @return the message with all matching substrings removed
     */
    public static String strip(String message, Pattern pattern) {
        return replaceAll(message, pattern, "");
    }

    private static class LastCharCompare implements Comparator<String> {

        private final Set<Character> ignoreCharacters = new HashSet<>();

        public void addIgnoreCharacter(char c) {
            this.ignoreCharacters.add(c);
        }

        @Override
        public int compare(String s1, String s2) {
            if (s1.isEmpty() && !s2.isEmpty()) return 1;
            if (s2.isEmpty() && !s1.isEmpty()) return -1;
            if (s2.isEmpty()) return 0;

            char firstChar = s1.charAt(s1.length() - 1);
            char secondChar = s2.charAt(s2.length() - 1);

            if (this.ignoreCharacters.contains(firstChar))
                return (secondChar - firstChar) * -1;
            else if (this.ignoreCharacters.contains(secondChar))
                return (firstChar - secondChar) * -1;
            else
                return firstChar - secondChar;
        }

    }

}

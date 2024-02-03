package dev.sbs.api.util;

import dev.sbs.api.collection.MaxSizeLinkedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.Language;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Helpers to process Strings using regular expressions.</p>
 *
 * @see Pattern
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexUtil {

    private static final LinkedHashMap<String, String> CACHED_COLOR_MESSAGES = new MaxSizeLinkedMap<>(100);
    private static final LastCharCompare CODE_COMPARE = new LastCharCompare();

    // Minecraft Chat Formatting
    public static final String SECTOR_SYMBOL = "\u00a7";
    private static final String ALL_PATTERN = "[0-9A-FK-ORa-fk-or]";
    private static final Pattern REPLACE_PATTERN = Pattern.compile("&&(?=" + ALL_PATTERN + ")");
    public static final Pattern VANILLA_PATTERN = Pattern.compile(SECTOR_SYMBOL + "+(" + ALL_PATTERN + ")");

    // SkyBlock
    public static final Pattern SERVER_REGEX = Pattern.compile("([0-9]{2}/[0-9]{2}/[0-9]{2}) (m(?:ini|ega)?[0-9]{1,3}[A-Za-z])");
    public static final Pattern API_KEY_REGEX = Pattern.compile("§aYour new API key is §r§b" + StringUtil.UUID_REGEX.pattern() + "§r");
    public static final Pattern TELEPORT_REGEX = Pattern.compile("§aWarped from the §r(.*?)§r§a to the §r(.*?)§r§a!§r");
    public static final Pattern MANA_REGEX = Pattern.compile("§aUsed §r§6(.*?)§r§a! §r§b\\(([\\d]+) Mana\\)§r");

    // Other
    public static final Pattern URL_PATTERN = Pattern.compile("((?:https?://)?[\\w._-]{2,})\\.([a-z]{2,6}(?:/\\S+)?)");
    public static final Pattern NUMBERS_SLASHES = Pattern.compile("[^0-9 /]");
    public static final Pattern LETTERS_NUMBERS = Pattern.compile("[^a-z A-Z:0-9/']");

    // https://regex101.com/r/RFixIy/1
    //public static final transient Pattern LOG_PATTERN = Pattern.compile("\\{(\\{[\\d]+(?:,[^,}]+)*})}");
    //public static final transient Pattern LOG_PATTERN2 = Pattern.compile("(?!.*\\{[^}]*$)\\{\\K(?:\\{[^}]*\\}|[^{}]*)+");
    //public static final transient Pattern LOG_PATTERN3 = Pattern.compile("(?:(?<!')?\\{(?!'))((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?:(?<!')}(?!')?)");
    //public static final Pattern LOG_PATTERN4 = Pattern.compile("(?<!')?\\{(?!')((?:[^{}]*\\{[^{}]*}[^{}]*)*)(?<!')}(?!')?");


    static {
        CODE_COMPARE.addIgnoreCharacter('r');
    }

    /**
     * Replaces the given message's {@link #SECTOR_SYMBOL} with a double ampersand (&&).
     *
     * @param message The message to replace the SECTOR_SYMBOL's.
     * @return The cached replaced message.
     */
    public static String lameColor(String message) {
        if (!CACHED_COLOR_MESSAGES.containsKey(message))
            CACHED_COLOR_MESSAGES.put(message, message.replace(SECTOR_SYMBOL, "&&"));

        return CACHED_COLOR_MESSAGES.get(message);
    }

    /**
     * <p>Removes each substring of the text String that matches the given regular expression pattern.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removeAll(null, *)      = null
     * StringUtil.removeAll("any", (Pattern) null)  = "any"
     * StringUtil.removeAll("any", Pattern.compile(""))    = "any"
     * StringUtil.removeAll("any", Pattern.compile(".*"))  = ""
     * StringUtil.removeAll("any", Pattern.compile(".+"))  = ""
     * StringUtil.removeAll("abc", Pattern.compile(".?"))  = ""
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\nB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL))  = "AB"
     * StringUtil.removeAll("ABCabc123abc", Pattern.compile("[a-z]"))     = "ABC123"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex the regular expression to which this string is to be matched
     * @return the text with any removes processed,
     * {@code null} if null String input
     * @see #replaceAll(String, Pattern, String)
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see java.util.regex.Pattern
     */
    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes each substring of the text String that matches the given regular expression.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>Unlike in the {@link #removePattern(String, String)} method, the {@link Pattern#DOTALL} option
     * is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.removeAll(null, *)      = null
     * StringUtil.removeAll("any", (String) null)  = "any"
     * StringUtil.removeAll("any", "")    = "any"
     * StringUtil.removeAll("any", ".*")  = ""
     * StringUtil.removeAll("any", ".+")  = ""
     * StringUtil.removeAll("abc", ".?")  = ""
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\nB"
     * StringUtil.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * StringUtil.removeAll("ABCabc123abc", "[a-z]")     = "ABC123"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex the regular expression to which this string is to be matched
     * @return the text with any removes processed,
     * {@code null} if null String input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replaceAll(String, String, String)
     * @see #removePattern(String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern
     * @see Pattern#DOTALL
     */
    public static String removeAll(final String text, final String regex) {
        return replaceAll(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes the first substring of the text string that matches the given regular expression pattern.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removeFirst(null, *)      = null
     * StringUtil.removeFirst("any", (Pattern) null)  = "any"
     * StringUtil.removeFirst("any", Pattern.compile(""))    = "any"
     * StringUtil.removeFirst("any", Pattern.compile(".*"))  = ""
     * StringUtil.removeFirst("any", Pattern.compile(".+"))  = ""
     * StringUtil.removeFirst("abc", Pattern.compile(".?"))  = "bc"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\n&lt;__&gt;B"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
     * StringUtil.removeFirst("ABCabc123", Pattern.compile("[a-z]"))          = "ABCbc123"
     * StringUtil.removeFirst("ABCabc123abc", Pattern.compile("[a-z]+"))      = "ABC123abc"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex the regular expression pattern to which this string is to be matched
     * @return the text with the first replacement processed,
     * {@code null} if null String input
     * @see #replaceFirst(String, Pattern, String)
     * @see java.util.regex.Matcher#replaceFirst(String)
     * @see java.util.regex.Pattern
     */
    public static String removeFirst(final String text, final Pattern regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes the first substring of the text string that matches the given regular expression.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, StringUtil.EMPTY)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
     *
     * <pre>
     * StringUtil.removeFirst(null, *)      = null
     * StringUtil.removeFirst("any", (String) null)  = "any"
     * StringUtil.removeFirst("any", "")    = "any"
     * StringUtil.removeFirst("any", ".*")  = ""
     * StringUtil.removeFirst("any", ".+")  = ""
     * StringUtil.removeFirst("abc", ".?")  = "bc"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\n&lt;__&gt;B"
     * StringUtil.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
     * StringUtil.removeFirst("ABCabc123", "[a-z]")          = "ABCbc123"
     * StringUtil.removeFirst("ABCabc123abc", "[a-z]+")      = "ABC123abc"
     * </pre>
     *
     * @param text  text to remove from, may be null
     * @param regex the regular expression to which this string is to be matched
     * @return the text with the first replacement processed,
     * {@code null} if null String input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replaceFirst(String, String, String)
     * @see String#replaceFirst(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String removeFirst(final String text, final String regex) {
        return replaceFirst(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Removes each substring of the source String that matches the given regular expression using the DOTALL option.</p>
     * <p>
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, StringUtil.EMPTY)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(StringUtil.EMPTY)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtil.removePattern(null, *)       = null
     * StringUtil.removePattern("any", (String) null)   = "any"
     * StringUtil.removePattern("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")  = "AB"
     * StringUtil.removePattern("ABCabc123", "[a-z]")    = "ABC123"
     * </pre>
     *
     * @param text  the source string
     * @param regex the regular expression to which this string is to be matched
     * @return The resulting {@code String}
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see Pattern#DOTALL
     */
    public static String removePattern(final String text, final String regex) {
        return replacePattern(text, regex, StringUtil.EMPTY);
    }

    /**
     * <p>Replaces each substring of the text String that matches the given regular expression pattern with the first match.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * RegexUtil.replaceAll(null, *)       = null
     * RegexUtil.replaceAll("any", (Pattern) null)   = "any"
     * RegexUtil.replaceAll("any", *)   = "any"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param regex the regular expression pattern to which this string is to be matched
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see java.util.regex.Pattern
     */
    public static String replaceAll(String text, Pattern regex) {
        return replaceAll(text, regex, "$1");
    }

    /**
     * <p>Replaces each substring of the text String that matches the given regular expression pattern with the given replacement.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
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
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression pattern to which this string is to be matched
     * @param replacement the string to be substituted for each match
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @see java.util.regex.Matcher#replaceAll(String)
     * @see java.util.regex.Pattern
     */
    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null)
            return text;

        return regex.matcher(text).replaceAll(replacement);
    }

    /**
     * <p>Replaces each substring of the text String that matches the given regular expression
     * with the given replacement.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceAll(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>Unlike in the {@link #replacePattern(String, String, String)} method, the {@link Pattern#DOTALL} option
     * is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
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
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     * @return the text with any replacements processed,
     * {@code null} if null String input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see #replacePattern(String, String, String)
     * @see String#replaceAll(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String replaceAll(final String text, @Language("RegExp") final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null)
            return text;

        return text.replaceAll(regex, replacement);
    }

    /**
     * <p>Replaces the first substring of the text string that matches the given regular expression pattern
     * with the given replacement.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code pattern.matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
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
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression pattern to which this string is to be matched
     * @param replacement the string to be substituted for the first match
     * @return the text with the first replacement processed,
     * {@code null} if null String input
     * @see java.util.regex.Matcher#replaceFirst(String)
     * @see java.util.regex.Pattern
     */
    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    /**
     * <p>Replaces the first substring of the text string that matches the given regular expression
     * with the given replacement.</p>
     * <p>
     * This method is a {@code null} safe equivalent to:
     * <ul>
     *  <li>{@code text.replaceFirst(regex, replacement)}</li>
     *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
     * To use the DOTALL option prepend {@code "(?s)"} to the regex.
     * DOTALL is also known as single-line mode in Perl.</p>
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
     * @param text        text to search and replace in, may be null
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for the first match
     * @return the text with the first replacement processed,
     * {@code null} if null String input
     * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
     * @see String#replaceFirst(String, String)
     * @see java.util.regex.Pattern
     * @see java.util.regex.Pattern#DOTALL
     */
    public static String replaceFirst(final String text, @Language("RegExp") final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return text.replaceFirst(regex, replacement);
    }

    /**
     * <p>Replaces each substring of the source String that matches the given regular expression with the given
     * replacement using the {@link Pattern#DOTALL} option. DOTALL is also known as single-line mode in Perl.</p>
     * <p>
     * This call is a {@code null} safe equivalent to:
     * <ul>
     * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
     * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement)}</li>
     * </ul>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
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
     * @param text        the source string
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     * @return The resulting {@code String}
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
     * Replaces the colors in the given message using the given pattern.
     *
     * @param message The message to filter.
     * @param pattern The regular expression pattern.
     * @return The cached filtered message.
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
     * Strips the given message using the given pattern.
     *
     * @param message The message to filter.
     * @param pattern The regular expression pattern.
     * @return The cached filtered message.
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

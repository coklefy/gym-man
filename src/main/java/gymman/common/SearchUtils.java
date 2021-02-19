package gymman.common;

import java.util.Arrays;
import java.util.Locale;

/**
 * Utility methods for the various search routines in repositories.
 */
public final class SearchUtils {
    private SearchUtils() {}

    /**
     * String to lowercase using the default Locale
     * 
     * @param str Input string
     * @return Lowercase string
     */
    public static String lowercase(String str) {
        return str.toLowerCase(Locale.getDefault());
    }

    /**
     * Checks if an input string contains all words from needle, even partially.
     * Can be used to search partial input in a string, i.e. "Fo B" in "Foo Bar".
     * 
     * @param haystack
     * @param needle
     * @return true if matched, false otherwise
     */
    public static boolean containsAllWords(String haystack, String needle) {
        return Arrays.asList(needle.split(" ")).stream()
                .map(e -> haystack.contains(e))
                .reduce(true, (prev, n) -> prev = prev && n);
    }
    
    /**
     * Checks if an input string contains all words from needle, even partially, ignoring case.
     * Can be used to search partial input in a string, i.e. "fo b" in "Foo Bar".
     * 
     * @param haystack
     * @param needle
     * @return true if matched, false otherwise
     */
    public static boolean containsAllWordsCaseInsensitive(String haystack, String needle) {
        return containsAllWords(lowercase(haystack), lowercase(needle));
    }
}

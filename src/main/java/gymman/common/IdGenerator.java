package gymman.common;

import java.util.Random;

/**
 * Simple generator for entity IDs.
 */
public final class IdGenerator {
    private IdGenerator() {}

    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_LENGTH = 16;

    /**
     * Generate an ID of the default length
     * 
     * @return the generated ID
     */
    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    /**
     * Generate an ID of the specified length
     * 
     * @param length
     * @return the generated ID
     */
    public static String generate(int length) {
        return new Random().ints(0, CHARSET.length())
            .limit(length)
            .map(i -> CHARSET.charAt(i))
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}

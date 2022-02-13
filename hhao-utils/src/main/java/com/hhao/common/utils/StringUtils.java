
package com.hhao.common.utils;


import com.hhao.common.lang.Nullable;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * The type String utils.
 *
 * @author Wang
 * @since 1.0.0
 */
public  class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';


    /**
     * Has length boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Has length boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * Has text boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasText(@Nullable CharSequence str) {
        return (str != null && str.length() > 0 && containsText(str));
    }

    /**
     * Has text boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasText(@Nullable String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains whitespace boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean containsWhitespace(@Nullable CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Contains whitespace boolean.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean containsWhitespace(@Nullable String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * Trim whitespace string.
     *
     * @param str the str
     * @return the string
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * Trim all whitespace string.
     *
     * @param str the str
     * @return the string
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Trim leading whitespace string.
     *
     * @param str the str
     * @return the string
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim trailing whitespace string.
     *
     * @param str the str
     * @return the string
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Trim leading character string.
     *
     * @param str              the str
     * @param leadingCharacter the leading character
     * @return the string
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIdx = 0;
        while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
            beginIdx++;
        }
        return str.substring(beginIdx);
    }

    /**
     * Trim trailing character string.
     *
     * @param str               the str
     * @param trailingCharacter the trailing character
     * @return the string
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        int endIdx = str.length() - 1;
        while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
            endIdx--;
        }
        return str.substring(0, endIdx + 1);
    }

    /**
     * Matches character boolean.
     *
     * @param str             the str
     * @param singleCharacter the single character
     * @return the boolean
     */
    public static boolean matchesCharacter(@Nullable String str, char singleCharacter) {
        return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
    }

    /**
     * Starts with ignore case boolean.
     *
     * @param str    the str
     * @param prefix the prefix
     * @return the boolean
     */
    public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length() &&
                str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * Ends with ignore case boolean.
     *
     * @param str    the str
     * @param suffix the suffix
     * @return the boolean
     */
    public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length() &&
                str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
    }

    /**
     * Substring match boolean.
     *
     * @param str       the str
     * @param index     the index
     * @param substring the substring
     * @return the boolean
     */
    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        if (index + substring.length() > str.length()) {
            return false;
        }
        for (int i = 0; i < substring.length(); i++) {
            if (str.charAt(index + i) != substring.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Count occurrences of int.
     *
     * @param str the str
     * @param sub the sub
     * @return the int
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (!hasLength(str) || !hasLength(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Replace string.
     *
     * @param inString   the in string
     * @param oldPattern the old pattern
     * @param newPattern the new pattern
     * @return the string
     */
    public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        int index = inString.indexOf(oldPattern);
        if (index == -1) {
            // no occurrence -> can return input as-is
            return inString;
        }

        int capacity = inString.length();
        if (newPattern.length() > oldPattern.length()) {
            capacity += 16;
        }
        StringBuilder sb = new StringBuilder(capacity);

        int pos = 0;  // our position in the old string
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }

        // append any characters to the right of a match
        sb.append(inString, pos, inString.length());
        return sb.toString();
    }

    /**
     * Delete string.
     *
     * @param inString the in string
     * @param pattern  the pattern
     * @return the string
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any string.
     *
     * @param inString      the in string
     * @param charsToDelete the chars to delete
     * @return the string
     */
    public static String deleteAny(String inString, @Nullable String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        int lastCharIndex = 0;
        char[] result = new char[inString.length()];
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                result[lastCharIndex++] = c;
            }
        }
        if (lastCharIndex == inString.length()) {
            return inString;
        }
        return new String(result, 0, lastCharIndex);
    }

    /**
     * Quote string.
     *
     * @param str the str
     * @return the string
     */
    @Nullable
    public static String quote(@Nullable String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Quote if string object.
     *
     * @param obj the obj
     * @return the object
     */
    @Nullable
    public static Object quoteIfString(@Nullable Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Unqualify string.
     *
     * @param qualifiedName the qualified name
     * @return the string
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }


    /**
     * Unqualify string.
     *
     * @param qualifiedName the qualified name
     * @param separator     the separator
     * @return the string
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Capitalize string.
     *
     * @param str the str
     * @return the string
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * Uncapitalize string.
     *
     * @param str the str
     * @return the string
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        }
        else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars);
    }

    /**
     * Gets filename.
     *
     * @param path the path
     * @return the filename
     */
    @Nullable
    public static String getFilename(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * Gets filename extension.
     *
     * @param path the path
     * @return the filename extension
     */
    @Nullable
    public static String getFilenameExtension(@Nullable String path) {
        if (path == null) {
            return null;
        }

        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }

        return path.substring(extIndex + 1);
    }

    /**
     * Strip filename extension string.
     *
     * @param path the path
     * @return the string
     */
    public static String stripFilenameExtension(String path) {
        int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return path;
        }

        int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return path;
        }

        return path.substring(0, extIndex);
    }

    /**
     * Apply relative path string.
     *
     * @param path         the path
     * @param relativePath the relative path
     * @return the string
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        }
        else {
            return relativePath;
        }
    }

    /**
     * Clean path string.
     *
     * @param path the path
     * @return the string
     */
    public static String cleanPath(String path) {
        if (!hasLength(path)) {
            return path;
        }
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Shortcut if there is no work to do
        if (pathToUse.indexOf('.') == -1) {
            return pathToUse;
        }

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(':');
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (prefix.contains(FOLDER_SEPARATOR)) {
                prefix = "";
            }
            else {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        Deque<String> pathElements = new ArrayDeque<>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            }
            else if (TOP_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            }
            else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                }
                else {
                    // Normal path element found.
                    pathElements.addFirst(element);
                }
            }
        }

        // All path elements stayed the same - shortcut
        if (pathArray.length == pathElements.size()) {
            return prefix + pathToUse;
        }
        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.addFirst(TOP_PATH);
        }
        // If nothing else left, at least explicitly point to current path.
        if (pathElements.size() == 1 && pathElements.getLast().isEmpty() && !prefix.endsWith(FOLDER_SEPARATOR)) {
            pathElements.addFirst(CURRENT_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * Path equals boolean.
     *
     * @param path1 the path 1
     * @param path2 the path 2
     * @return the boolean
     */
    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    /**
     * Uri decode string.
     *
     * @param source  the source
     * @param charset the charset
     * @return the string
     */
    public static String uriDecode(String source, Charset charset) {
        int length = source.length();
        if (length == 0) {
            return source;
        }
        Assert.notNull(charset, "Charset must not be null");

        ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            if (ch == '%') {
                if (i + 2 < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                    }
                    baos.write((char) ((u << 4) + l));
                    i += 2;
                    changed = true;
                }
                else {
                    throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
                }
            }
            else {
                baos.write(ch);
            }
        }
        return (changed ? StreamUtils.copyToString(baos, charset) : source);
    }

    /**
     * Parse locale locale.
     *
     * @param localeValue the locale value
     * @return the locale
     */
    @Nullable
    public static Locale parseLocale(String localeValue) {
        String[] tokens = tokenizeLocaleSource(localeValue);
        if (tokens.length == 1) {
            validateLocalePart(localeValue);
            Locale resolved = Locale.forLanguageTag(localeValue);
            if (resolved.getLanguage().length() > 0) {
                return resolved;
            }
        }
        return parseLocaleTokens(localeValue, tokens);
    }

    /**
     * Parse locale string locale.
     *
     * @param localeString the locale string
     * @return the locale
     */
    @Nullable
    public static Locale parseLocaleString(String localeString) {
        return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
    }

    private static String[] tokenizeLocaleSource(String localeSource) {
        return tokenizeToStringArray(localeSource, "_ ", false, false);
    }

    @Nullable
    private static Locale parseLocaleTokens(String localeString, String[] tokens) {
        String language = (tokens.length > 0 ? tokens[0] : "");
        String country = (tokens.length > 1 ? tokens[1] : "");
        validateLocalePart(language);
        validateLocalePart(country);

        String variant = "";
        if (tokens.length > 2) {
            // There is definitely a variant, and it is everything after the country
            // code sans the separator between the country code and the variant.
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            // Strip off any leading '_' and whitespace, what's left is the variant.
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }

        if (variant.isEmpty() && country.startsWith("#")) {
            variant = country;
            country = "";
        }

        return (language.length() > 0 ? new Locale(language, country, variant) : null);
    }

    private static void validateLocalePart(String localePart) {
        for (int i = 0; i < localePart.length(); i++) {
            char ch = localePart.charAt(i);
            if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException(
                        "Locale part \"" + localePart + "\" contains invalid characters");
            }
        }
    }

    /**
     * Parse time zone string time zone.
     *
     * @param timeZoneString the time zone string
     * @return the time zone
     */
    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            // We don't want that GMT fallback...
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
        }
        return timeZone;
    }


    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    //---------------------------------------------------------------------

    /**
     * To string array string [ ].
     *
     * @param collection the collection
     * @return the string [ ]
     */
    public static String[] toStringArray(@Nullable Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * To string array string [ ].
     *
     * @param enumeration the enumeration
     * @return the string [ ]
     */
    public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
        return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
    }

    /**
     * Add string to array string [ ].
     *
     * @param array the array
     * @param str   the str
     * @return the string [ ]
     */
    public static String[] addStringToArray(@Nullable String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[] {str};
        }

        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate string arrays string [ ].
     *
     * @param array1 the array 1
     * @param array2 the array 2
     * @return the string [ ]
     */
    @Nullable
    public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }

        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Sort string array string [ ].
     *
     * @param array the array
     * @return the string [ ]
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Arrays.sort(array);
        return array;
    }

    /**
     * Trim array elements string [ ].
     *
     * @param array the array
     * @return the string [ ]
     */
    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * Remove duplicate strings string [ ].
     *
     * @param array the array
     * @return the string [ ]
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }

        Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
        return toStringArray(set);
    }

    /**
     * Split string [ ].
     *
     * @param toSplit   the to split
     * @param delimiter the delimiter
     * @return the string [ ]
     */
    @Nullable
    public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }

        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[] {beforeDelimiter, afterDelimiter};
    }

    /**
     * Split array elements into properties properties.
     *
     * @param array     the array
     * @param delimiter the delimiter
     * @return the properties
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Split array elements into properties properties.
     *
     * @param array         the array
     * @param delimiter     the delimiter
     * @param charsToDelete the chars to delete
     * @return the properties
     */
    @Nullable
    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, @Nullable String charsToDelete) {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }

        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = deleteAny(element, charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * Tokenize to string array string [ ].
     *
     * @param str        the str
     * @param delimiters the delimiters
     * @return the string [ ]
     */
    public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize to string array string [ ].
     *
     * @param str               the str
     * @param delimiters        the delimiters
     * @param trimTokens        the trim tokens
     * @param ignoreEmptyTokens the ignore empty tokens
     * @return the string [ ]
     */
    public static String[] tokenizeToStringArray(
            @Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Delimited list to string array string [ ].
     *
     * @param str       the str
     * @param delimiter the delimiter
     * @return the string [ ]
     */
    public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Delimited list to string array string [ ].
     *
     * @param str           the str
     * @param delimiter     the delimiter
     * @param charsToDelete the chars to delete
     * @return the string [ ]
     */
    public static String[] delimitedListToStringArray(
            @Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }
        if (delimiter == null) {
            return new String[] {str};
        }

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        }
        else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Comma delimited list to string array string [ ].
     *
     * @param str the str
     * @return the string [ ]
     */
    public static String[] commaDelimitedListToStringArray(@Nullable String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Comma delimited list to set set.
     *
     * @param str the str
     * @return the set
     */
    public static Set<String> commaDelimitedListToSet(@Nullable String str) {
        String[] tokens = commaDelimitedListToStringArray(str);
        return new LinkedHashSet<>(Arrays.asList(tokens));
    }

    /**
     * Collection to delimited string string.
     *
     * @param coll   the coll
     * @param delim  the delim
     * @param prefix the prefix
     * @param suffix the suffix
     * @return the string
     */
    public static String collectionToDelimitedString(
            @Nullable Collection<?> coll, String delim, String prefix, String suffix) {

        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Collection to delimited string string.
     *
     * @param coll  the coll
     * @param delim the delim
     * @return the string
     */
    public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Collection to comma delimited string string.
     *
     * @param coll the coll
     * @return the string
     */
    public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Array to delimited string string.
     *
     * @param arr   the arr
     * @param delim the delim
     * @return the string
     */
    public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        }

        StringJoiner sj = new StringJoiner(delim);
        for (Object elem : arr) {
            sj.add(String.valueOf(elem));
        }
        return sj.toString();
    }

    /**
     * Array to comma delimited string string.
     *
     * @param arr the arr
     * @return the string
     */
    public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

	/**
	 * Convert Chinese characters to Unicode.
	 * @param configValue value of config
	 * @return new string
	 */
	public static String selectiveConvertUnicode(String configValue) {
		StringBuilder sb = new StringBuilder();
		char[] chars = configValue.toCharArray();
		for (char aChar : chars) {
			if (isBaseLetter(aChar)) {
				sb.append(aChar);
			}
			else {
				sb.append(String.format("\\u%04x", (int) aChar));
			}
		}
		return sb.toString();
	}

	/**
	 * char is base latin or whitespace?
	 * @param ch a character
	 * @return true or false
	 */
	public static boolean isBaseLetter(char ch) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		return ub == Character.UnicodeBlock.BASIC_LATIN || Character.isWhitespace(ch);
	}

	/**
	 * char is chinese?
	 * @param c a character
	 * @return true or false
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
	}
}

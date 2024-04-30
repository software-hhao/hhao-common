/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.utils.html;

import com.hhao.common.utils.Assert;

/**
 * The type Html utils.
 *
 * @author Wang
 * @since 1.0.0
 */
public class HtmlUtils {

    /**
     * The constant DEFAULT_CHARACTER_ENCODING.
     */
    public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

    private static HtmlCharacterEntityReferences characterEntityReferences =
            new HtmlCharacterEntityReferences();


    /**
     * Html escape string.
     *
     * @param input the input
     * @return the string
     */
    public static String htmlEscape(String input) {
        return htmlEscape(input, HtmlUtils.DEFAULT_CHARACTER_ENCODING);
    }


    /**
     * Html escape string.
     *
     * @param input    the input
     * @param encoding the encoding
     * @return the string
     */
    public static String htmlEscape(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            String reference = characterEntityReferences.convertToReference(character, encoding);
            if (reference != null) {
                escaped.append(reference);
            }
            else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }


    /**
     * Html escape decimal string.
     *
     * @param input the input
     * @return the string
     */
    public static String htmlEscapeDecimal(String input) {
        return htmlEscapeDecimal(input, HtmlUtils.DEFAULT_CHARACTER_ENCODING);
    }


    /**
     * Html escape decimal string.
     *
     * @param input    the input
     * @param encoding the encoding
     * @return the string
     */
    public static String htmlEscapeDecimal(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character, encoding)) {
                escaped.append(HtmlCharacterEntityReferences.DECIMAL_REFERENCE_START);
                escaped.append((int) character);
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            }
            else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }


    /**
     * Html escape hex string.
     *
     * @param input the input
     * @return the string
     */
    public static String htmlEscapeHex(String input) {
        return htmlEscapeHex(input, HtmlUtils.DEFAULT_CHARACTER_ENCODING);
    }


    /**
     * Html escape hex string.
     *
     * @param input    the input
     * @param encoding the encoding
     * @return the string
     */
    public static String htmlEscapeHex(String input, String encoding) {
        Assert.notNull(input, "Input is required");
        Assert.notNull(encoding, "Encoding is required");
        StringBuilder escaped = new StringBuilder(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character, encoding)) {
                escaped.append(HtmlCharacterEntityReferences.HEX_REFERENCE_START);
                escaped.append(Integer.toString(character, 16));
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            }
            else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }


    /**
     * Html unescape string.
     *
     * @param input the input
     * @return the string
     */
    public static String htmlUnescape(String input) {
        return new HtmlCharacterEntityDecoder(characterEntityReferences, input).decode();
    }
}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The type Html character entity references.
 *
 * @author Wang
 * @since  1.0.0
 */
public class HtmlCharacterEntityReferences {
    private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";

    /**
     * The Reference start.
     */
    static final char REFERENCE_START = '&';

    /**
     * The Decimal reference start.
     */
    static final String DECIMAL_REFERENCE_START = "&#";

    /**
     * The Hex reference start.
     */
    static final String HEX_REFERENCE_START = "&#x";

    /**
     * The Reference end.
     */
    static final char REFERENCE_END = ';';

    /**
     * The Char null.
     */
    static final char CHAR_NULL = (char) -1;


    private final String[] characterToEntityReferenceMap = new String[3000];

    private final Map<String, Character> entityReferenceToCharacterMap = new HashMap<>(512);


    /**
     * Returns a new set of character entity references reflecting the HTML 4.0 character set.
     */
    public HtmlCharacterEntityReferences() {
        Properties entityReferences = new Properties();

        // Load reference definition file
        InputStream is = HtmlCharacterEntityReferences.class.getResourceAsStream(PROPERTIES_FILE);
        if (is == null) {
            throw new IllegalStateException(
                    "Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
        }
        try {
            try {
                entityReferences.load(is);
            }
            finally {
                is.close();
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException(
                    "Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " +  ex.getMessage());
        }

        // Parse reference definition properties
        Enumeration<?> keys = entityReferences.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            int referredChar = Integer.parseInt(key);
            Assert.isTrue((referredChar < 1000 || (referredChar >= 8000 && referredChar < 10000)),
                    () -> "Invalid reference to special HTML entity: " + referredChar);
            int index = (referredChar < 1000 ? referredChar : referredChar - 7000);
            String reference = entityReferences.getProperty(key);
            this.characterToEntityReferenceMap[index] = REFERENCE_START + reference + REFERENCE_END;
            this.entityReferenceToCharacterMap.put(reference, (char) referredChar);
        }
    }


    /**
     * Return the number of supported entity references.
     *
     * @return the supported reference count
     */
    public int getSupportedReferenceCount() {
        return this.entityReferenceToCharacterMap.size();
    }

    /**
     * Return true if the given character is mapped to a supported entity reference.
     *
     * @param character the character
     * @return the boolean
     */
    public boolean isMappedToReference(char character) {
        return isMappedToReference(character, HtmlUtils.DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Return true if the given character is mapped to a supported entity reference.
     *
     * @param character the character
     * @param encoding  the encoding
     * @return the boolean
     */
    public boolean isMappedToReference(char character, String encoding) {
        return (convertToReference(character, encoding) != null);
    }

    /**
     * Return the reference mapped to the given character, or {@code null} if none found.
     *
     * @param character the character
     * @return the string
     */
    
    public String convertToReference(char character) {
        return convertToReference(character, HtmlUtils.DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Return the reference mapped to the given character, or {@code null} if none found.
     *
     * @param character the character
     * @param encoding  the encoding
     * @return the string
     * @since 4.1.2
     */
    
    public String convertToReference(char character, String encoding) {
        if (encoding.startsWith("UTF-")){
            switch (character){
                case '<':
                    return "&lt;";
                case '>':
                    return "&gt;";
                case '"':
                    return "&quot;";
                case '&':
                    return "&amp;";
                case '\'':
                    return "&#39;";
                default:
            }
        }
        else if (character < 1000 || (character >= 8000 && character < 10000)) {
            int index = (character < 1000 ? character : character - 7000);
            String entityReference = this.characterToEntityReferenceMap[index];
            if (entityReference != null) {
                return entityReference;
            }
        }
        return null;
    }

    /**
     * Return the char mapped to the given entityReference or -1.
     *
     * @param entityReference the entity reference
     * @return the char
     */
    public char convertToCharacter(String entityReference) {
        Character referredCharacter = this.entityReferenceToCharacterMap.get(entityReference);
        if (referredCharacter != null) {
            return referredCharacter;
        }
        return CHAR_NULL;
    }

}

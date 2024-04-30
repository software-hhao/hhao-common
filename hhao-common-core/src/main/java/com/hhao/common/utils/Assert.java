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

package com.hhao.common.utils;


import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;


/**
 * 用于验证的类，参照Spring的Assert
 *
 * @author Wang
 * @since 1.0.0
 */
public class Assert {
    /**
     * Is true.
     *
     * @param expression the expression
     * @param message    the message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Is true.
     *
     * @param expression      the expression
     * @param messageSupplier the message supplier
     */
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is null.
     *
     * @param object  the object
     * @param message the message
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Is null.
     *
     * @param object          the object
     * @param messageSupplier the message supplier
     */
    public static void isNull( Object object, Supplier<String> messageSupplier) {
        if (object != null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Not null.
     *
     * @param object  the object
     * @param message the message
     */
    public static void notNull( Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not null.
     *
     * @param object          the object
     * @param messageSupplier the message supplier
     */
    public static void notNull( Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Has length.
     *
     * @param text    the text
     * @param message the message
     */
    public static void hasLength( String text, String message) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Has length.
     *
     * @param text            the text
     * @param messageSupplier the message supplier
     */
    public static void hasLength( String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }


    /**
     * Has text.
     *
     * @param text    the text
     * @param message the message
     */
    public static void hasText( String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Has text.
     *
     * @param text            the text
     * @param messageSupplier the message supplier
     */
    public static void hasText( String text, Supplier<String> messageSupplier) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Does not contain.
     *
     * @param textToSearch the text to search
     * @param substring    the substring
     * @param message      the message
     */
    public static void doesNotContain( String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Does not contain.
     *
     * @param textToSearch    the text to search
     * @param substring       the substring
     * @param messageSupplier the message supplier
     */
    public static void doesNotContain( String textToSearch, String substring, Supplier<String> messageSupplier) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }

    /**
     * Not empty.
     *
     * @param array   the array
     * @param message the message
     */
    public static void notEmpty( Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not empty.
     *
     * @param array           the array
     * @param messageSupplier the message supplier
     */
    public static void notEmpty( Object[] array, Supplier<String> messageSupplier) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }


    /**
     * No null elements.
     *
     * @param array   the array
     * @param message the message
     */
    public static void noNullElements( Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }


    /**
     * No null elements.
     *
     * @param array           the array
     * @param messageSupplier the message supplier
     */
    public static void noNullElements( Object[] array, Supplier<String> messageSupplier) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Not empty.
     *
     * @param collection the collection
     * @param message    the message
     */
    public static void notEmpty( Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Not empty.
     *
     * @param collection      the collection
     * @param messageSupplier the message supplier
     */
    public static void notEmpty( Collection<?> collection, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }


    /**
     * No null elements.
     *
     * @param collection the collection
     * @param message    the message
     */
    public static void noNullElements( Collection<?> collection, String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }


    /**
     * No null elements.
     *
     * @param collection      the collection
     * @param messageSupplier the message supplier
     */
    public static void noNullElements( Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(nullSafeGet(messageSupplier));
                }
            }
        }
    }

    /**
     * Not empty.
     *
     * @param map     the map
     * @param message the message
     */
    public static void notEmpty( Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Not empty.
     *
     * @param map             the map
     * @param messageSupplier the message supplier
     */
    public static void notEmpty( Map<?, ?> map, Supplier<String> messageSupplier) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalArgumentException(nullSafeGet(messageSupplier));
        }
    }


    /**
     * Is instance of.
     *
     * @param type    the type
     * @param obj     the obj
     * @param message the message
     */
    public static void isInstanceOf(Class<?> type,  Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, message);
        }
    }

    /**
     * Is instance of.
     *
     * @param type            the type
     * @param obj             the obj
     * @param messageSupplier the message supplier
     */
    public static void isInstanceOf(Class<?> type,  Object obj, Supplier<String> messageSupplier) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
        }
    }

    /**
     * Is instance of.
     *
     * @param type the type
     * @param obj  the obj
     */
    public static void isInstanceOf(Class<?> type,  Object obj) {
        isInstanceOf(type, obj, "");
    }


    /**
     * Is assignable.
     *
     * @param superType the super type
     * @param subType   the sub type
     * @param message   the message
     */
    public static void isAssignable(Class<?> superType,  Class<?> subType, String message) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, message);
        }
    }

    /**
     * Is assignable.
     *
     * @param superType       the super type
     * @param subType         the sub type
     * @param messageSupplier the message supplier
     */
    public static void isAssignable(Class<?> superType,  Class<?> subType, Supplier<String> messageSupplier) {
        notNull(superType, "Super type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
        }
    }


    /**
     * Is assignable.
     *
     * @param superType the super type
     * @param subType   the sub type
     */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }


    private static void instanceCheckFailed(Class<?> type,  Object obj,  String msg) {
        String className = (obj != null ? obj.getClass().getName() : "null");
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            }
            else {
                result = messageWithTypeName(msg, className);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + ("Object of class [" + className + "] must be an instance of " + type);
        }
        throw new IllegalArgumentException(result);
    }

    private static void assignableCheckFailed(Class<?> superType,  Class<?> subType,  String msg) {
        String result = "";
        boolean defaultMessage = true;
        if (StringUtils.hasLength(msg)) {
            if (endsWithSeparator(msg)) {
                result = msg + " ";
            }
            else {
                result = messageWithTypeName(msg, subType);
                defaultMessage = false;
            }
        }
        if (defaultMessage) {
            result = result + (subType + " is not assignable to " + superType);
        }
        throw new IllegalArgumentException(result);
    }

    private static boolean endsWithSeparator(String msg) {
        return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
    }

    private static String messageWithTypeName(String msg,  Object typeName) {
        return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
    }

    
    private static String nullSafeGet( Supplier<String> messageSupplier) {
        return (messageSupplier != null ? messageSupplier.get() : null);
    }

}


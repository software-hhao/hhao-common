package com.hhao.common.utils;


import com.hhao.common.lang.Nullable;

import java.util.*;


/**
 * The type Collection utils.
 *
 * @author Wang
 * @since 1.0.0
 */
public  class CollectionUtils {

    /**
     * The Default load factor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;


    /**
     * Is empty boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }


    /**
     * Is empty boolean.
     *
     * @param map the map
     * @return the boolean
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }


    /**
     * New hash map hash map.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param expectedSize the expected size
     * @return the hash map
     */
    public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
        return new HashMap<>((int) (expectedSize / DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR);
    }


    /**
     * New linked hash map linked hash map.
     *
     * @param <K>          the type parameter
     * @param <V>          the type parameter
     * @param expectedSize the expected size
     * @return the linked hash map
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int expectedSize) {
        return new LinkedHashMap<>((int) (expectedSize / DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR);
    }

    /**
     * Array to list list.
     *
     * @param source the source
     * @return the list
     */
    public static List<?> arrayToList(@Nullable Object source) {
        return Arrays.asList(ObjectUtils.toObjectArray(source));
    }

    /**
     * Merge array into collection.
     *
     * @param <E>        the type parameter
     * @param array      the array
     * @param collection the collection
     */
    @SuppressWarnings("unchecked")
    public static <E> void mergeArrayIntoCollection(@Nullable Object array, Collection<E> collection) {
        Object[] arr = ObjectUtils.toObjectArray(array);
        for (Object elem : arr) {
            collection.add((E) elem);
        }
    }

    /**
     * Merge properties into map.
     *
     * @param <K>   the type parameter
     * @param <V>   the type parameter
     * @param props the props
     * @param map   the map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> void mergePropertiesIntoMap(@Nullable Properties props, Map<K, V> map) {
        if (props != null) {
            for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    // Allow for defaults fallback or potentially overridden accessor...
                    value = props.getProperty(key);
                }
                map.put((K) key, (V) value);
            }
        }
    }

    /**
     * Contains boolean.
     *
     * @param iterator the iterator
     * @param element  the element
     * @return the boolean
     */
    public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contains boolean.
     *
     * @param enumeration the enumeration
     * @param element     the element
     * @return the boolean
     */
    public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contains instance boolean.
     *
     * @param collection the collection
     * @param element    the element
     * @return the boolean
     */
    public static boolean containsInstance(@Nullable Collection<?> collection, Object element) {
        if (collection != null) {
            for (Object candidate : collection) {
                if (candidate == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Contains any boolean.
     *
     * @param source     the source
     * @param candidates the candidates
     * @return the boolean
     */
    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        return findFirstMatch(source, candidates) != null;
    }

    /**
     * Find first match e.
     *
     * @param <E>        the type parameter
     * @param source     the source
     * @param candidates the candidates
     * @return the e
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return null;
        }
        for (Object candidate : candidates) {
            if (source.contains(candidate)) {
                return (E) candidate;
            }
        }
        return null;
    }

    /**
     * Find value of type t.
     *
     * @param <T>        the type parameter
     * @param collection the collection
     * @param type       the type
     * @return the t
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T findValueOfType(Collection<?> collection, @Nullable Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object element : collection) {
            if (type == null || type.isInstance(element)) {
                if (value != null) {
                    // More than one value found... no clear single value.
                    return null;
                }
                value = (T) element;
            }
        }
        return value;
    }

    /**
     * Find value of type object.
     *
     * @param collection the collection
     * @param types      the types
     * @return the object
     */
    @Nullable
    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
            return null;
        }
        for (Class<?> type : types) {
            Object value = findValueOfType(collection, type);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Has unique object boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean hasUniqueObject(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Object elem : collection) {
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            }
            else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find common element type class.
     *
     * @param collection the collection
     * @return the class
     */
    @Nullable
    public static Class<?> findCommonElementType(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Class<?> candidate = null;
        for (Object val : collection) {
            if (val != null) {
                if (candidate == null) {
                    candidate = val.getClass();
                }
                else if (candidate != val.getClass()) {
                    return null;
                }
            }
        }
        return candidate;
    }

    /**
     * First element t.
     *
     * @param <T> the type parameter
     * @param set the set
     * @return the t
     */
    @Nullable
    public static <T> T firstElement(@Nullable Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).first();
        }

        Iterator<T> it = set.iterator();
        T first = null;
        if (it.hasNext()) {
            first = it.next();
        }
        return first;
    }

    /**
     * First element t.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the t
     */
    @Nullable
    public static <T> T firstElement(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Last element t.
     *
     * @param <T> the type parameter
     * @param set the set
     * @return the t
     */
    @Nullable
    public static <T> T lastElement(@Nullable Set<T> set) {
        if (isEmpty(set)) {
            return null;
        }
        if (set instanceof SortedSet) {
            return ((SortedSet<T>) set).last();
        }

        // Full iteration necessary...
        Iterator<T> it = set.iterator();
        T last = null;
        while (it.hasNext()) {
            last = it.next();
        }
        return last;
    }

    /**
     * Last element t.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the t
     */
    @Nullable
    public static <T> T lastElement(@Nullable List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * To array a [ ].
     *
     * @param <A>         the type parameter
     * @param <E>         the type parameter
     * @param enumeration the enumeration
     * @param array       the array
     * @return the a [ ]
     */
    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
        ArrayList<A> elements = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            elements.add(enumeration.nextElement());
        }
        return elements.toArray(array);
    }

    /**
     * To iterator iterator.
     *
     * @param <E>         the type parameter
     * @param enumeration the enumeration
     * @return the iterator
     */
    public static <E> Iterator<E> toIterator(@Nullable Enumeration<E> enumeration) {
        return (enumeration != null ? new EnumerationIterator<>(enumeration) : Collections.emptyIterator());
    }

    private static class EnumerationIterator<E> implements Iterator<E> {

        private final Enumeration<E> enumeration;

        /**
         * Instantiates a new Enumeration iterator.
         *
         * @param enumeration the enumeration
         */
        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        @Override
        public E next() {
            return this.enumeration.nextElement();
        }

        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}

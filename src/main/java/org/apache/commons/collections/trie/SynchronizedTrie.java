/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.trie;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.collections.Trie;
import org.apache.commons.collections.collection.SynchronizedCollection;
import org.apache.commons.collections.set.SynchronizedSet;

/**
 * A synchronized {@link Trie}.
 * 
 * @since 4.0
 * @version $Id$
 */
public class SynchronizedTrie<K, V> implements Trie<K, V>, Serializable {
    
    private static final long serialVersionUID = 3121878833178676939L;
    
    private final Trie<K, V> delegate;
    
    /**
     * Factory method to create a synchronized trie.
     * 
     * @param trie  the trie to decorate, must not be null
     * @return a new synchronized trie
     * @throws IllegalArgumentException if trie is null
     */
    public static <K, V> SynchronizedTrie<K, V> synchronizedTrie(Trie<K, V> trie) {
        return new SynchronizedTrie<K, V>(trie);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * 
     * @param trie  the trie to decorate, must not be null
     * @throws IllegalArgumentException if set is null
     */
    public SynchronizedTrie(Trie<K, V> trie) {
        if (trie == null) {
            throw new IllegalArgumentException("Collection must not be null");
        }
        this.delegate = trie;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Entry<K, V> select(K key, 
            Cursor<? super K, ? super V> cursor) {
        return delegate.select(key, cursor);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Entry<K, V> select(K key) {
        return delegate.select(key);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized K selectKey(K key) {
        return delegate.selectKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized V selectValue(K key) {
        return delegate.selectValue(key);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Entry<K, V> traverse(Cursor<? super K, ? super V> cursor) {
        return delegate.traverse(cursor);
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized Set<Entry<K, V>> entrySet() {
        return SynchronizedSet.synchronizedSet(delegate.entrySet());
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Set<K> keySet() {
        return SynchronizedSet.synchronizedSet(delegate.keySet());
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Collection<V> values() {
        return SynchronizedCollection.synchronizedCollection(delegate.values());
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void clear() {
        delegate.clear();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized V get(Object key) {
        return delegate.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isEmpty() {
        return delegate.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized V put(K key, V value) {
        return delegate.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        delegate.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized V remove(Object key) {
        return delegate.remove(key);
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized K lastKey() {
        return delegate.lastKey();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> subMap(K fromKey, K toKey) {
        return Collections.synchronizedSortedMap(delegate.subMap(fromKey, toKey));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> tailMap(K fromKey) {
        return Collections.synchronizedSortedMap(delegate.tailMap(fromKey));
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized Comparator<? super K> comparator() {
        return delegate.comparator();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized K firstKey() {
        return delegate.firstKey();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> headMap(K toKey) {
        return Collections.synchronizedSortedMap(delegate.headMap(toKey));
    }
    
    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> getPrefixedBy(K key, int offset, int length) {
        return Collections.synchronizedSortedMap(delegate.getPrefixedBy(key, offset, length));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> getPrefixedBy(K key, int length) {
        return Collections.synchronizedSortedMap(delegate.getPrefixedBy(key, length));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> getPrefixedBy(K key) {
        return Collections.synchronizedSortedMap(delegate.getPrefixedBy(key));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> getPrefixedByBits(K key, int lengthInBits) {
        return Collections.synchronizedSortedMap(delegate.getPrefixedByBits(key, lengthInBits));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap<K, V> getPrefixedByBits(K key, 
            int offsetInBits, int lengthInBits) {
        return Collections.synchronizedSortedMap(delegate.getPrefixedByBits(key, offsetInBits, lengthInBits));
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int size() {
        return delegate.size();
    }

    @Override
    public synchronized int hashCode() {
        return delegate.hashCode();
    }
    
    @Override
    public synchronized boolean equals(Object obj) {
        return delegate.equals(obj);
    }
    
    @Override
    public synchronized String toString() {
        return delegate.toString();
    }
}

/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/map/LinkedMap.java,v 1.3 2003/12/07 01:23:54 scolebourne Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.commons.collections.map;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedIterator;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.ResettableIterator;

/**
 * A <code>Map</code> implementation that maintains the order of the entries.
 * In this implementation order is maintained is by original insertion, but
 * subclasses may work differently.
 * <p>
 * This implementation improves on the JDK1.4 LinkedHashMap by adding the 
 * {@link org.apache.commons.collections.iterators.MapIterator MapIterator}
 * functionality, additional convenience methods and allowing
 * bidirectional iteration. It also implements <code>OrderedMap</code>.
 * <p>
 * The <code>orderedMapIterator()</code> method provides direct access to a
 * bidirectional iterator. The iterators from the other views can also be cast
 * to <code>OrderedIterator</code> if required.
 * <p>
 * All the available iterators can be reset back to the start by casting to
 * <code>ResettableIterator</code> and calling <code>reset()</code>.
 * <p>
 * The implementation is also designed to be subclassed, with lots of useful
 * methods exposed.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 1.3 $ $Date: 2003/12/07 01:23:54 $
 *
 * @author java util LinkedHashMap
 * @author Stephen Colebourne
 */
public class LinkedMap extends HashedMap implements OrderedMap {
    
    /** Serialisation version */
    static final long serialVersionUID = -1954063410665686469L;
    
    /** Header in the linked list */
    protected transient LinkEntry header;

    /**
     * Constructs a new empty map with default size and load factor.
     */
    public LinkedMap() {
        super();
    }

    /**
     * Constructs a new, empty map with the specified initial capacity. 
     *
     * @param initialCapacity  the initial capacity
     * @throws IllegalArgumentException if the initial capacity is less than one
     */
    public LinkedMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new, empty map with the specified initial capacity and
     * load factor. 
     *
     * @param initialCapacity  the initial capacity
     * @param loadFactor  the load factor
     * @throws IllegalArgumentException if the initial capacity is less than one
     * @throws IllegalArgumentException if the load factor is less than zero
     */
    public LinkedMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructor copying elements from another map.
     *
     * @param map  the map to copy
     * @throws NullPointerException if the map is null
     */
    public LinkedMap(Map map) {
        super(map);
    }

    /**
     * Initialise this subclass during construction.
     */
    protected void init() {
        header = new LinkEntry(null, -1, null, null);
        header.before = header.after = header;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the map contains the specified value.
     * 
     * @param value  the value to search for
     * @return true if the map contains the value
     */
    public boolean containsValue(Object value) {
        // override uses faster iterator
        if (value == null) {
            for (LinkEntry entry = header.after; entry != header; entry = entry.after) {
                if (entry.getValue() == null) {
                    return true;
                }
            }
        } else {
            for (LinkEntry entry = header.after; entry != header; entry = entry.after) {
                if (isEqualValue(value, entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clears the map, resetting the size to zero and nullifying references
     * to avoid garbage collection issues.
     */
    public void clear() {
        // override to reset the linked list
        super.clear();
        header.before = header.after = header;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the first key in the map, which is the most recently inserted.
     * 
     * @return the most recently inserted key
     */
    public Object firstKey() {
        if (size == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return header.after.getKey();
    }

    /**
     * Gets the last key in the map, which is the first inserted.
     * 
     * @return the eldest key
     */
    public Object lastKey() {
        if (size == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return header.before.getKey();
    }

    /**
     * Gets the next key in sequence.
     * 
     * @param key  the key to get after
     * @return the next key
     */
    public Object nextKey(Object key) {
        LinkEntry entry = (LinkEntry) getEntry(key);
        return (entry == null || entry.after == header ? null : entry.after.getKey());
    }

    /**
     * Gets the previous key in sequence.
     * 
     * @param key  the key to get before
     * @return the previous key
     */
    public Object previousKey(Object key) {
        LinkEntry entry = (LinkEntry) getEntry(key);
        return (entry == null || entry.before == header ? null : entry.before.getKey());
    }

    //-----------------------------------------------------------------------    
    /**
     * Adds an entry into this map, maintaining insertion order.
     * <p>
     * This implementation adds the entry to the data storage table and
     * to the end of the linked list.
     * 
     * @param entry  the entry to add
     * @param hashIndex  the index into the data array to store at
     */
    protected void addEntry(HashEntry entry, int hashIndex) {
        LinkEntry link = (LinkEntry) entry;
        link.after  = header;
        link.before = header.before;
        header.before.after = link;
        header.before = link;
        data[hashIndex] = entry;
    }
    
    /**
     * Creates an entry to store the data.
     * <p>
     * This implementation creates a new LinkEntry instance.
     * 
     * @param next  the next entry in sequence
     * @param hashCode  the hash code to use
     * @param key  the key to store
     * @param value  the value to store
     * @return the newly created entry
     */
    protected HashEntry createEntry(HashEntry next, int hashCode, Object key, Object value) {
        return new LinkEntry(next, hashCode, key, value);
    }
    
    /**
     * Removes an entry from the map and the linked list.
     * <p>
     * This implementation removes the entry from the linked list chain, then
     * calls the superclass implementation.
     * 
     * @param entry  the entry to remove
     * @param hashIndex  the index into the data structure
     * @param previous  the previous entry in the chain
     */
    protected void removeEntry(HashEntry entry, int hashIndex, HashEntry previous) {
        LinkEntry link = (LinkEntry) entry;
        link.before.after = link.after;
        link.after.before = link.before;
        link.after = null;
        link.before = null;
        super.removeEntry(entry, hashIndex, previous);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator over the map.
     * Changes made to the iterator affect this map.
     * <p>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     * It also avoids creating the Mep Entry object.
     * 
     * @return the map iterator
     */
    public MapIterator mapIterator() {
        if (size == 0) {
            return IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR;
        }
        return new LinkMapIterator(this);
    }

    /**
     * Gets a bidirectional iterator over the map.
     * Changes made to the iterator affect this map.
     * <p>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     * It also avoids creating the Mep Entry object.
     * 
     * @return the map iterator
     */
    public OrderedMapIterator orderedMapIterator() {
        if (size == 0) {
            return IteratorUtils.EMPTY_ORDERED_MAP_ITERATOR;
        }
        return new LinkMapIterator(this);
    }

    /**
     * MapIterator
     */
    static class LinkMapIterator extends LinkIterator implements OrderedMapIterator {
        
        LinkMapIterator(LinkedMap map) {
            super(map);
        }

        public Object next() {
            return super.nextEntry().getKey();
        }

        public Object previous() {
            return super.previousEntry().getKey();
        }

        public Object getKey() {
            HashEntry current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(HashedMap.GETKEY_INVALID);
            }
            return current.getKey();
        }

        public Object getValue() {
            HashEntry current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(HashedMap.GETVALUE_INVALID);
            }
            return current.getValue();
        }

        public Object setValue(Object value) {
            HashEntry current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(HashedMap.SETVALUE_INVALID);
            }
            return current.setValue(value);
        }
    }
    
    //-----------------------------------------------------------------------    
    /**
     * Creates an entry set iterator.
     * Subclasses can override this to return iterators with different properties.
     * 
     * @return the entrySet iterator
     */
    protected Iterator createEntrySetIterator() {
        if (size() == 0) {
            return IteratorUtils.EMPTY_ORDERED_ITERATOR;
        }
        return new EntrySetIterator(this);
    }

    /**
     * EntrySetIterator and MapEntry
     */
    static class EntrySetIterator extends LinkIterator {
        
        EntrySetIterator(LinkedMap map) {
            super(map);
        }

        public Object next() {
            return super.nextEntry();
        }

        public Object previous() {
            return super.previousEntry();
        }
    }

    //-----------------------------------------------------------------------    
    /**
     * Creates a key set iterator.
     * Subclasses can override this to return iterators with different properties.
     * 
     * @return the keySet iterator
     */
    protected Iterator createKeySetIterator() {
        if (size() == 0) {
            return IteratorUtils.EMPTY_ORDERED_ITERATOR;
        }
        return new KeySetIterator(this);
    }

    /**
     * KeySetIterator
     */
    static class KeySetIterator extends EntrySetIterator {
        
        KeySetIterator(LinkedMap map) {
            super(map);
        }

        public Object next() {
            return super.nextEntry().getKey();
        }

        public Object previous() {
            return super.previousEntry().getKey();
        }
    }
    
    //-----------------------------------------------------------------------    
    /**
     * Creates a values iterator.
     * Subclasses can override this to return iterators with different properties.
     * 
     * @return the values iterator
     */
    protected Iterator createValuesIterator() {
        if (size() == 0) {
            return IteratorUtils.EMPTY_ORDERED_ITERATOR;
        }
        return new ValuesIterator(this);
    }

    /**
     * ValuesIterator
     */
    static class ValuesIterator extends LinkIterator {
        
        ValuesIterator(LinkedMap map) {
            super(map);
        }

        public Object next() {
            return super.nextEntry().getValue();
        }

        public Object previous() {
            return super.previousEntry().getValue();
        }
    }
    
    //-----------------------------------------------------------------------
    /**
     * LinkEntry
     */
    protected static class LinkEntry extends HashEntry {
        
        protected LinkEntry before;
        protected LinkEntry after;
        
        protected LinkEntry(HashEntry next, int hashCode, Object key, Object value) {
            super(next, hashCode, key, value);
        }
    }
    
    /**
     * Base Iterator
     */
    protected static abstract class LinkIterator
            implements OrderedIterator, ResettableIterator {
                
        protected final LinkedMap map;
        protected LinkEntry current;
        protected LinkEntry next;
        protected int expectedModCount;
        
        protected LinkIterator(LinkedMap map) {
            super();
            this.map = map;
            this.next = map.header.after;
            this.expectedModCount = map.modCount;
        }

        public boolean hasNext() {
            return (next != map.header);
        }

        public boolean hasPrevious() {
            return (next.before != map.header);
        }

        protected LinkEntry nextEntry() {
            if (map.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (next == map.header)  {
                throw new NoSuchElementException(HashedMap.NO_NEXT_ENTRY);
            }
            current = next;
            next = next.after;
            return current;
        }

        protected LinkEntry previousEntry() {
            if (map.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            LinkEntry previous = next.before;
            if (previous == map.header)  {
                throw new NoSuchElementException(HashedMap.NO_PREVIOUS_ENTRY);
            }
            next = previous;
            current = previous;
            return current;
        }
        
        protected LinkEntry currentEntry() {
            return current;
        }
        
        public void remove() {
            if (current == null) {
                throw new IllegalStateException(HashedMap.REMOVE_INVALID);
            }
            if (map.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            map.remove(current.getKey());
            current = null;
            expectedModCount = map.modCount;
        }
        
        public void reset() {
            current = null;
            next = map.header.after;
        }

        public String toString() {
            if (current != null) {
                return "Iterator[" + current.getKey() + "=" + current.getValue() + "]";
            } else {
                return "Iterator[]";
            }
        }
    }
    
}

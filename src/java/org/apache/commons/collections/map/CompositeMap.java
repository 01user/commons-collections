/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/map/CompositeMap.java,v 1.1 2003/12/14 21:42:55 psteitz Exp $
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.collection.CompositeCollection;
import org.apache.commons.collections.set.CompositeSet;

/**
 * Decorates a map of other maps to provide a single unified view.
 * <p>
 * Changes made to this map will actually be made on the decorated map.
 * Add and remove operations require the use of a pluggable strategy. If no
 * strategy is provided then add and remove are unsupported.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.1 $ $Date: 2003/12/14 21:42:55 $
 *
 * @author Brian McCallister
 */
public class CompositeMap implements Map {
    /** Array of all maps in the composite */
    private Map[] composite = null;
    
    /** Handle mutation operations */
    private MapMutator mutator = null;
    
    /**
     * Create a new, empty, CompositeMap
     */
    public CompositeMap() {
        this(new Map[]{}, null);
    }
    
    /**
     * Create a new CompositeMap with two composited Map instances
     * @param one First Map to be composited
     * @param two Second Map to be composited
     * @throws IllegalArgumentException if there is a key collision
     */
    public CompositeMap(Map one, Map two) {
        this(new Map[]{one, two}, null);
    }
    
    /**
     * Create a new CompositeMap with two composited Map instances
     * @param one First Map to be composited
     * @param two Second Map to be composited
     * @param mutator MapMutator to be used for mutation operations
     */
    public CompositeMap(Map one, Map two, MapMutator mutator) {
        this(new Map[]{one, two}, mutator);
    }
    
    /**
     * Create a new CompositeMap which composites all of the Map instances in the
     * argument. It copies the argument array, it does not use it directly.
     * @param composite Maps to be composited
     * @throws IllegalArgumentException if there is a key collision
     */
    public CompositeMap(Map[] composite) {
        this(composite, null);
    }
    
    /**
     * Create a new CompositeMap which composites all of the Map instances in the
     * argument. It copies the argument array, it does not use it directly.
     * @param composite Maps to be composited
     * @param mutator MapMutator to be used for mutation operations
     */
    public CompositeMap(Map[] composite, MapMutator mutator) {
        this.mutator = mutator;
        this.composite = new Map[0];
        for (int i = composite.length - 1; i >= 0; --i) {
            this.addComposited(composite[i]);
        }
    }
    
    /**
     * Specify the MapMutator to be used by mutation operations
     * @param mutator The MapMutator to be used for mutation delegation
     */
    public void setMutator(MapMutator mutator) {
        this.mutator = mutator;
    }
    
    /**
     * Add an additional Map to the composite
     *
     * @param map Map to be added to the composite
     * @throws IllegalArgumentException if there is a key collision and there is no
     *         MapMutator set to handle it.
     */
    public synchronized void addComposited(Map map) throws IllegalArgumentException {
        for (int i = composite.length - 1; i >= 0; --i) {
            Collection intersect = CollectionUtils.intersection(this.composite[i].keySet(), map.keySet());
            if (intersect.size() != 0) {
                if (this.mutator == null) {
                    throw new IllegalArgumentException("Key collision adding Map to CompositeMap");
                }
                else {
                    this.mutator.resolveCollision(this, this.composite[i], map, intersect);
                }
            }
        }
        Map[] temp = new Map[this.composite.length + 1];
        System.arraycopy(this.composite, 0, temp, 0, this.composite.length);
        temp[temp.length - 1] = map;
        this.composite = temp;
    }
    
    /**
     * Remove a Map from the composite
     *
     * @param map The Map to be removed from the composite
     * @return The removed Map or <code>null</code> if map is not in the composite
     */
    public synchronized Map removeComposited(Map map) {
        int size = this.composite.length;
        for (int i = 0; i < size; ++i) {
            if (this.composite[i].equals(map)) {
                Map[] temp = new Map[size - 1];
                System.arraycopy(this.composite, 0, temp, 0, i);
                System.arraycopy(this.composite, i + 1, temp, i, size - i - 1);
                this.composite = temp;
                return map;
            }
        }
        return null;
    }
    
    /* Map Implementation */
    
    /**
     * Calls <code>clear()</code> on all composited Maps
     *
     * @throws UnsupportedOperationException if any of the composited Maps do not support clear()
     */
    public void clear() {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            this.composite[i].clear();
        }
    }
    
    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains at a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException if the key is <tt>null</tt> and this map
     *            does not not permit <tt>null</tt> keys (optional).
     */
    public boolean containsKey(Object key) {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            if (this.composite[i].containsKey(key)) return true;
        }
        return false;
    }
    
    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested.
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value.
     * @throws ClassCastException if the value is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException if the value is <tt>null</tt> and this map
     *            does not not permit <tt>null</tt> values (optional).
     */
    public boolean containsValue(Object value) {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            if (this.composite[i].containsValue(value)) return true;
        }
        return false;
    }
    
    /**
     * Returns a set view of the mappings contained in this map.  Each element
     * in the returned set is a {@link Map.Entry}.  The set is backed by the
     * map, so changes to the map are reflected in the set, and vice-versa.
     * If the map is modified while an iteration over the set is in progress,
     * the results of the iteration are undefined.  The set supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not support
     * the <tt>add</tt> or <tt>addAll</tt> operations.
     * <p>
     * This implementation returns a <code>CompositeSet</code> which
     * composites the entry sets from all of the composited maps.
     *
     * @see CompositeSet
     * @return a set view of the mappings contained in this map.
     */
    public Set entrySet() {
        CompositeSet entries = new CompositeSet();
        for (int i = this.composite.length - 1; i >= 0; --i) {
            entries.addComposited(this.composite[i].entrySet());
        }
        return entries;
    }
    
    /**
     * Returns the value to which this map maps the specified key.  Returns
     * <tt>null</tt> if the map contains no mapping for this key.  A return
     * value of <tt>null</tt> does not <i>necessarily</i> indicate that the
     * map contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to <tt>null</tt>.  The <tt>containsKey</tt>
     * operation may be used to distinguish these two cases.
     *
     * <p>More formally, if this map contains a mapping from a key
     * <tt>k</tt> to a value <tt>v</tt> such that <tt>(key==null ? k==null :
     * key.equals(k))</tt>, then this method returns <tt>v</tt>; otherwise
     * it returns <tt>null</tt>.  (There can be at most one such mapping.)
     *
     * @param key key whose associated value is to be returned.
     * @return the value to which this map maps the specified key, or
     *	       <tt>null</tt> if the map contains no mapping for this key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  this map (optional).
     * @throws NullPointerException key is <tt>null</tt> and this map does not
     *		  not permit <tt>null</tt> keys (optional).
     *
     * @see #containsKey(Object)
     */
    public Object get(Object key) {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            if (this.composite[i].containsKey(key)) return this.composite[i].get(key);
        }
        return null;
    }
    
    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            if (!this.composite[i].isEmpty()) return false;
        }
        return true;
    }
    
    /**
     * Returns a set view of the keys contained in this map.  The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa.  If the map is modified while an iteration over the set is
     * in progress, the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding mapping from
     * the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt> <tt>retainAll</tt>, and <tt>clear</tt> operations.
     * It does not support the add or <tt>addAll</tt> operations.
     * <p>
     * This implementation returns a <code>CompositeSet</code> which
     * composites the key sets from all of the composited maps.
     *
     * @return a set view of the keys contained in this map.
     */
    public Set keySet() {
        CompositeSet keys = new CompositeSet();
        for (int i = this.composite.length - 1; i >= 0; --i) {
            keys.addComposited(this.composite[i].keySet());
        }
        return keys;
    }
    
    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * this key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.))
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.  A <tt>null</tt> return can
     *	       also indicate that the map previously associated <tt>null</tt>
     *	       with the specified key, if the implementation supports
     *	       <tt>null</tt> values.
     *
     * @throws UnsupportedOperationException if no MapMutator has been specified
     * @throws ClassCastException if the class of the specified key or value
     * 	          prevents it from being stored in this map.
     * @throws IllegalArgumentException if some aspect of this key or value
     *	          prevents it from being stored in this map.
     * @throws NullPointerException this map does not permit <tt>null</tt>
     *            keys or values, and the specified key or value is
     *            <tt>null</tt>.
     */
    public Object put(Object key, Object value) {
        if (this.mutator == null) throw new UnsupportedOperationException("No mutator specified");
        return this.mutator.put(this, this.composite, key, value);
    }
    
    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object,Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is unspecified if the
     * specified map is modified while the operation is in progress.
     *
     * @param t Mappings to be stored in this map.
     *
     * @throws UnsupportedOperationException if the <tt>putAll</tt> method is
     * 		  not supported by this map.
     *
     * @throws ClassCastException if the class of a key or value in the
     * 	          specified map prevents it from being stored in this map.
     *
     * @throws IllegalArgumentException some aspect of a key or value in the
     *	          specified map prevents it from being stored in this map.
     * @throws NullPointerException the specified map is <tt>null</tt>, or if
     *         this map does not permit <tt>null</tt> keys or values, and the
     *         specified map contains <tt>null</tt> keys or values.
     */
    public void putAll(Map t) {
        if (this.mutator == null) throw new UnsupportedOperationException("No mutator specified");
        this.mutator.putAll(this, this.composite, t);
    }
    
    /**
     * Removes the mapping for this key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     *
     * <p>Returns the value to which the map previously associated the key, or
     * <tt>null</tt> if the map contained no mapping for this key.  (A
     * <tt>null</tt> return can also indicate that the map previously
     * associated <tt>null</tt> with the specified key if the implementation
     * supports <tt>null</tt> values.)  The map will not contain a mapping for
     * the specified  key once the call returns.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt>
     *	       if there was no mapping for key.
     *
     * @throws ClassCastException if the key is of an inappropriate type for
     * 		  the composited map (optional).
     * @throws NullPointerException if the key is <tt>null</tt> and the composited map
     *            does not not permit <tt>null</tt> keys (optional).
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is
     *         not supported by the composited map containing the key
     */
    public Object remove(Object key) {
        for (int i = this.composite.length - 1; i >= 0; --i) {
            if (this.composite[i].containsKey(key)) return this.composite[i].remove(key);
        }
        return null;
    }
    
    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map.
     */
    public int size() {
        int size = 0;
        for (int i = this.composite.length - 1; i >= 0; --i) {
            size += this.composite[i].size();
        }
        return size;
    }
    
    /**
     * Returns a collection view of the values contained in this map.  The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa.  If the map is modified while an
     * iteration over the collection is in progress, the results of the
     * iteration are undefined.  The collection supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations.
     * It does not support the add or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map.
     */
    public Collection values() {
        CompositeCollection keys = new CompositeCollection();
        for (int i = this.composite.length - 1; i >= 0; --i) {
            keys.addComposited(this.composite[i].values());
        }
        return keys;
    }
    
    /**
     * @see Map#hashCode
     */
    public int hashCode() {
        int code = 0;
        for (Iterator i = this.entrySet().iterator(); i.hasNext();) {
            code += i.next().hashCode();
        }
        return code;
    }
    
    /**
     * @see Map#equals
     */
    public boolean equals(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return (this.entrySet().equals(map.entrySet()));
        }
        return false;
    }
    
    /**
     * This interface allows definition for all of the indeterminate
     * mutators in a CompositeMap, as well as providing a hook for
     * callbacks on key collisions.
     */
    public static interface MapMutator {
        /**
         * Called when adding a new Composited Map results in a
         * key collision.
         *
         * @param composite the CompositeMap withthe collision
         * @param existing the Map already in the composite which contains the
         *        offending key
         * @param added the Map being added
         * @param intersect the intersection of the keysets of the existing and added maps
         */
        public void resolveCollision(CompositeMap composite,
        Map existing,
        Map added,
        Collection intersect);
        
        /**
         * Called when the CompositeMap.put() method is invoked.
         *
         * @param map the CompositeMap which is being modified
         * @param composited array of Maps in the CompositeMap being modified
         * @param key key with which the specified value is to be associated.
         * @param value value to be associated with the specified key.
         * @return previous value associated with specified key, or <tt>null</tt>
         *	       if there was no mapping for key.  A <tt>null</tt> return can
         *	       also indicate that the map previously associated <tt>null</tt>
         *	       with the specified key, if the implementation supports
         *	       <tt>null</tt> values.
         *
         * @throws UnsupportedOperationException if not defined
         * @throws ClassCastException if the class of the specified key or value
         * 	          prevents it from being stored in this map.
         * @throws IllegalArgumentException if some aspect of this key or value
         *	          prevents it from being stored in this map.
         * @throws NullPointerException this map does not permit <tt>null</tt>
         *            keys or values, and the specified key or value is
         *            <tt>null</tt>.
         */
        public Object put(CompositeMap map, Map[] composited, Object key, Object value);
        
        /**
         * Called when the CompositeMap.putAll() method is invoked.
         *
         * @param map the CompositeMap which is being modified
         * @param composited array of Maps in the CompositeMap being modified
         * @param t Mappings to be stored in this CompositeMap
         *
         * @throws UnsupportedOperationException if not defined
         * @throws ClassCastException if the class of the specified key or value
         * 	          prevents it from being stored in this map.
         * @throws IllegalArgumentException if some aspect of this key or value
         *	          prevents it from being stored in this map.
         * @throws NullPointerException this map does not permit <tt>null</tt>
         *            keys or values, and the specified key or value is
         *            <tt>null</tt>.
         */
        public void putAll(CompositeMap map, Map[] composited, Map t);
    }
}

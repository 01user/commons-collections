/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/Attic/DualTreeBidiMap.java,v 1.2 2003/11/01 18:47:18 scolebourne Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
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
package org.apache.commons.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.decorators.AbstractSortedMapDecorator;

/**
 * Implementation of <code>BidiMap</code> that uses two <code>TreeMap</code> instances.
 * 
 * @since Commons Collections 3.0
 * @version $Id: DualTreeBidiMap.java,v 1.2 2003/11/01 18:47:18 scolebourne Exp $
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 */
public class DualTreeBidiMap extends AbstractDualBidiMap implements SortedBidiMap {

    /**
     * Creates an empty <code>DualTreeBidiMap</code>
     */
    public DualTreeBidiMap() {
        super();
    }

    /** 
     * Constructs a <code>DualTreeBidiMap</code> and copies the mappings from
     * specified <code>Map</code>.  
     *
     * @param map  the map whose mappings are to be placed in this map
     */
    public DualTreeBidiMap(Map map) {
        super();
        putAll(map);
    }

    /** 
     * Constructs a <code>HashBidiMap</code> that decorates the specified maps.
     *
     * @param normalMap  the normal direction map
     * @param reverseMap  the reverse direction map
     * @param inverseBidiMap  the inverse BidiMap
     */
    protected DualTreeBidiMap(Map normalMap, Map reverseMap, BidiMap inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
    }
    
    /**
     * Creates a new instance of the map used by the subclass to store data.
     * 
     * @return the map to be used for internal storage
     */
    protected Map createMap() {
        return new TreeMap();
    }

    /**
     * Creates a new instance of this object.
     * 
     * @param normalMap  the normal direction map
     * @param reverseMap  the reverse direction map
     * @param inverseBidiMap  the inverse BidiMap
     * @return new bidi map
     */
    protected BidiMap createBidiMap(Map normalMap, Map reverseMap, BidiMap inverseMap) {
        return new DualTreeBidiMap(normalMap, reverseMap, inverseMap);
    }

    // SortedBidiMap
    //-----------------------------------------------------------------------
    public SortedBidiMap inverseSortedBidiMap() {
        return (SortedBidiMap) inverseBidiMap();
    }

    // SortedMap
    //-----------------------------------------------------------------------
    public Comparator comparator() {
        return ((SortedMap) maps[0]).comparator();
    }

    public Object firstKey() {
        return ((SortedMap) maps[0]).firstKey();
    }

    public Object lastKey() {
        return ((SortedMap) maps[0]).lastKey();
    }

    //-----------------------------------------------------------------------
    public SortedMap headMap(Object toKey) {
        SortedMap sub = ((SortedMap) maps[0]).headMap(toKey);
        return new ViewMap(this, sub);
    }

    public SortedMap tailMap(Object fromKey) {
        SortedMap sub = ((SortedMap) maps[0]).tailMap(fromKey);
        return new ViewMap(this, sub);
    }

    public SortedMap subMap(Object fromKey, Object toKey) {
        SortedMap sub = ((SortedMap) maps[0]).subMap(fromKey, toKey);
        return new ViewMap(this, sub);
    }
    
    protected static class ViewMap extends AbstractSortedMapDecorator {
        final DualTreeBidiMap bidi;
        
        protected ViewMap(DualTreeBidiMap bidi, SortedMap sm) {
            // the implementation is not great here...
            // use the maps[0] as the filtered map, but maps[1] as the full map
            // this forces containsValue and clear to be overridden
            super((SortedMap) bidi.createBidiMap(sm, bidi.maps[1], bidi.inverseBidiMap));
            this.bidi = (DualTreeBidiMap) map;
        }
        
        public boolean containsValue(Object value) {
            // override as default implementation jumps to [1]
            return bidi.maps[0].containsValue(value);
        }
        
        public void clear() {
            // override as default implementation jumps to [1]
            for (Iterator it = keySet().iterator(); it.hasNext();) {
                it.next();
                it.remove();
            }
        }
        
        public SortedMap headMap(Object toKey) {
            return new ViewMap(bidi, super.headMap(toKey));
        }

        public SortedMap tailMap(Object fromKey) {
            return new ViewMap(bidi, super.tailMap(fromKey));
        }

        public SortedMap subMap(Object fromKey, Object toKey) {
            return new ViewMap(bidi, super.subMap(fromKey, toKey));
        }
    }

}

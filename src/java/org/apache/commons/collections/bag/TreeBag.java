/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/bag/TreeBag.java,v 1.6 2004/01/05 21:54:06 scolebourne Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2004 The Apache Software Foundation.  All rights
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
package org.apache.commons.collections.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.SortedBag;

/**
 * Implements <code>SortedBag</code>, using a <code>TreeMap</code> to provide
 * the data storage. This is the standard implementation of a sorted bag.
 * <p>
 * Order will be maintained among the bag members and can be viewed through the
 * iterator.
 * <p>
 * A <code>Bag</code> stores each object in the collection together with a
 * count of occurances. Extra methods on the interface allow multiple copies
 * of an object to be added or removed at once. It is important to read the
 * interface javadoc carefully as several methods violate the
 * <code>Collection</code> interface specification.
 *
 * @since Commons Collections 3.0 (previously in main package v2.0)
 * @version $Revision: 1.6 $ $Date: 2004/01/05 21:54:06 $
 * 
 * @author Chuck Burdick
 * @author Stephen Colebourne
 */
public class TreeBag
        extends AbstractMapBag implements SortedBag, Serializable {

    /** Serial version lock */
    static final long serialVersionUID = -7740146511091606676L;
    
    /**
     * Constructs an empty <code>TreeBag</code>.
     */
    public TreeBag() {
        super(new TreeMap());
    }

    /**
     * Constructs an empty bag that maintains order on its unique
     * representative members according to the given {@link Comparator}.
     * 
     * @param comparator  the comparator to use
     */
    public TreeBag(Comparator comparator) {
        super(new TreeMap(comparator));
    }

    /**
     * Constructs a <code>TreeBag</code> containing all the members of the
     * specified collection.
     * 
     * @param coll  the collection to copy into the bag
     */
    public TreeBag(Collection coll) {
        this();
        addAll(coll);
    }

    //-----------------------------------------------------------------------
    public Object first() {
        return ((SortedMap) getMap()).firstKey();
    }

    public Object last() {
        return ((SortedMap) getMap()).lastKey();
    }

    public Comparator comparator() {
        return ((SortedMap) getMap()).comparator();
    }

    //-----------------------------------------------------------------------
    /**
     * Write the bag out using a custom routine.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(comparator());
        super.doWriteObject(out);
    }

    /**
     * Read the bag in using a custom routine.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Comparator comp = (Comparator) in.readObject();
        super.doReadObject(new TreeMap(comp), in);
    }
    
}

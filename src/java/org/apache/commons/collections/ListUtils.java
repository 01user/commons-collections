/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/ListUtils.java,v 1.21 2003/09/21 16:26:08 scolebourne Exp $
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
package org.apache.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.decorators.FixedSizeList;
import org.apache.commons.collections.decorators.LazyList;
import org.apache.commons.collections.decorators.PredicatedList;
import org.apache.commons.collections.decorators.SynchronizedList;
import org.apache.commons.collections.decorators.TransformedList;
import org.apache.commons.collections.decorators.TypedList;
import org.apache.commons.collections.decorators.UnmodifiableList;
import org.apache.commons.collections.observed.ModificationListener;
import org.apache.commons.collections.observed.ObservableList;

/**
 * Provides utility methods and decorators for {@link List} instances.
 *
 * @since Commons Collections 1.0
 * @version $Revision: 1.21 $ $Date: 2003/09/21 16:26:08 $
 * 
 * @author Federico Barbieri
 * @author Peter Donald
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Neil O'Toole
 * @author Matthew Hawthorne
 */
public class ListUtils {

    /**
     * An empty unmodifiable list.
     * This uses the {@link Collections Collections} implementation 
     * and is provided for completeness.
     */
    public static final List EMPTY_LIST = Collections.EMPTY_LIST;
    
    /**
     * <code>ListUtils</code> should not normally be instantiated.
     */
    public ListUtils() {
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new list containing all elements that are contained in
     * both given lists.
     *
     * @param list1  the first list
     * @param list2  the second list
     * @return  the intersection of those two lists
     * @throws NullPointerException if either list is null
     */
    public static List intersection(final List list1, final List list2) {
        final ArrayList result = new ArrayList();
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            final Object o = iterator.next();

            if (list1.contains(o)) {
                result.add(o);
            }
        }

        return result;
    }

    /**
     * Subtracts all elements in the second list from the first list,
     * placing the results in a new list.
     * <p>
     * This differs from {@link List#removeAll(Collection)} in that
     * cardinality is respected; if <Code>list1</Code> contains two
     * occurrences of <Code>null</Code> and <Code>list2</Code> only
     * contains one occurrence, then the returned list will still contain
     * one occurrence.
     *
     * @param list1  the list to subtract from
     * @param list2  the list to subtract
     * @return  a new list containing the results
     * @throws NullPointerException if either list is null
     */
    public static List subtract(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * Returns the sum of the given lists.  This is their intersection
     * subtracted from their union.
     *
     * @param list1  the first list 
     * @param list2  the second list
     * @return  a new list containing the sum of those lists
     * @throws NullPointerException if either list is null
     */ 
    public static List sum(final List list1, final List list2) {
        return subtract(union(list1, list2), intersection(list1, list2));
    }

    /**
     * Returns a new list containing the second list appended to the
     * first list.  The {@link List#addAll(Collection)} operation is
     * used to append the two given lists into a new list.
     *
     * @param list1  the first list 
     * @param list2  the second list
     * @return  a new list containing the union of those lists
     * @throws NullPointerException if either list is null
     */
    public static List union(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * Tests two lists for value-equality as per the equality contract in
     * {@link java.util.List#equals(java.lang.Object)}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <blockquote>
     * Compares the two list objects for equality.  Returns
     * <tt>true</tt> if and only if both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.  (Two elements <tt>e1</tt> and
     * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.  This
     * definition ensures that the equals method works properly across
     * different implementations of the <tt>List</tt> interface.
     * </blockquote>
     *
     * <b>Note:</b> The behaviour of this method is undefined if the lists are
     * modified during the equals comparison.
     * 
     * @see java.util.List
     * @param list1  the first list, may be null
     * @param list2  the second list, may be null
     * @return whether the lists are equal by value comparison
     */
    public static boolean isEqualList(final Collection list1, final Collection list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Object obj1 = null;
        Object obj2 = null;

        while (it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();

            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }
    
    /**
     * Generates a hashcode using the algorithm specified in 
     * {@link java.util.List#hashCode()}.
     * <p>
     * This method is useful for implementing <code>List</code> when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * 
     * @see java.util.List#hashCode()
     * @param list  the list to generate the hashCode for, may be null
     * @return the hash code
     */
    public static int hashCodeForList(final Collection list) {
        if (list == null) {
            return 0;
        }
        int hashCode = 1;
        Iterator it = list.iterator();
        Object obj = null;
        
        while (it.hasNext()) {
            obj = it.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }   

    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized list backed by the given list.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to 
     * avoid non-deterministic behavior:
     *  
     * <pre>
     * List list = ListUtils.synchronizedList(myList);
     * synchronized (list) {
     *     Iterator i = list.iterator();
     *     while (i.hasNext()) {
     *         process (i.next());
     *     }
     * }
     * </pre>
     * 
     * This method uses the implementation in the decorators subpackage.
     * 
     * @param list  the list to synchronize, must not be null
     * @return a synchronized list backed by the given list
     * @throws IllegalArgumentException  if the list is null
     */
    public static List synchronizedList(List list) {
        return SynchronizedList.decorate(list);
    }

    /**
     * Returns an unmodifiable list backed by the given list.
     * <p>
     * This method uses the implementation in the decorators subpackage.
     *
     * @param list  the list to make unmodifiable, must not be null
     * @return an unmodifiable list backed by the given list
     * @throws IllegalArgumentException  if the list is null
     */
    public static List unmodifiableList(List list) {
        return UnmodifiableList.decorate(list);
    }

    /**
     * Returns a predicated list backed by the given list.  Only objects
     * that pass the test in the given predicate can be added to the list.
     * It is important not to use the original list after invoking this 
     * method, as it is a backdoor for adding unvalidated objects.
     *
     * @param list  the list to predicate, must not be null
     * @param predicate  the predicate for the list, must not be null
     * @return a predicated list backed by the given list
     * @throws IllegalArgumentException  if the List or Predicate is null
     */
    public static List predicatedList(List list, Predicate predicate) {
        return PredicatedList.decorate(list, predicate);
    }

    /**
     * Returns a typed list backed by the given list.
     * <p>
     * Only objects of the specified type can be added to the list.
     * 
     * @param list  the list to limit to a specific type, must not be null
     * @param type  the type of objects which may be added to the list
     * @return a typed list backed by the specified list
     */
    public static List typedList(List list, Class type) {
        return TypedList.decorate(list, type);
    }
    
    /**
     * Returns a transformed list backed by the given list.
     * <p>
     * Each object is passed through the transformer as it is added to the
     * List. It is important not to use the original list after invoking this 
     * method, as it is a backdoor for adding untransformed objects.
     *
     * @param list  the list to predicate, must not be null
     * @param transformer  the transformer for the list, must not be null
     * @return a transformed list backed by the given list
     * @throws IllegalArgumentException  if the List or Transformer is null
     */
    public static List transformedList(List list, Transformer transformer) {
        return TransformedList.decorate(list, transformer);
    }
    
    /**
     * Returns an observable list where changes are notified to listeners.
     * <p>
     * This method creates an observable list and attaches the specified listener.
     * If more than one listener or other complex setup is required then the
     * ObservableList class should be accessed directly.
     *
     * @param list  the list to decorate, must not be null
     * @param listener  list listener, must not be null
     * @return the observed list
     * @throws IllegalArgumentException if the list or listener is null
     * @throws IllegalArgumentException if there is no valid handler for the listener
     */
    public static ObservableList observableList(List list, ModificationListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null");
        }
        return ObservableList.decorate(list, listener);
    }
    
    /**
     * Returns a "lazy" list whose elements will be created on demand.
     * <p>
     * When the index passed to the returned list's {@link List#get(int) get}
     * method is greater than the list's size, then the factory will be used
     * to create a new object and that object will be inserted at that index.
     * <p>
     * For instance:
     *
     * <pre>
     * Factory factory = new Factory() {
     *     public Object create() {
     *         return new Date();
     *     }
     * }
     * List lazy = ListUtils.lazyList(new ArrayList(), factory);
     * Object obj = lazy.get(3);
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
     * instance is the fourth element in the list.  The first, second, 
     * and third element are all set to <code>null</code>.
     *
     * @param list  the list to make lazy, must not be null
     * @param factory  the factory for creating new objects, must not be null
     * @return a lazy list backed by the given list
     * @throws IllegalArgumentException  if the List or Factory is null
     */
    public static List lazyList(List list, Factory factory) {
        return LazyList.decorate(list, factory);
    }

    /**
     * Returns a fixed-sized list backed by the given list.
     * Elements may not be added or removed from the returned list, but 
     * existing elements can be changed (for instance, via the 
     * {@link List#set(int,Object)} method).
     *
     * @param list  the list whose size to fix, must not be null
     * @return a fixed-size list backed by that list
     * @throws IllegalArgumentException  if the List is null
     */
    public static List fixedSizeList(List list) {
        return FixedSizeList.decorate(list);
    }

}

/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/ListUtils.java,v 1.16 2003/04/09 23:37:54 scolebourne Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
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
 *    any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
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
import java.util.ListIterator;

/**
 * Contains static utility methods and decorators for {@link List} 
 * instances.
 *
 * @since Commons Collections 1.0
 * @version $Revision: 1.16 $ $Date: 2003/04/09 23:37:54 $
 * 
 * @author  <a href="mailto:fede@apache.org">Federico Barbieri</a>
 * @author  <a href="mailto:donaldp@apache.org">Peter Donald</a>
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
     * This differs from {@link List#removeAll(Collection)} in that
     * cardinality is respected; if <Code>list1</Code> contains two
     * occurrences of <Code>null</Code> and <Code>list2</Code> only
     * contains one occurrence, then the returned list will still contain
     * one occurrence.
     *
     * @param list1  the list to subtract from
     * @param list2  the lsit to subtract
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
     * Implementation of a ListIterator that wraps an original.
     */
    static class ListIteratorWrapper 
            implements ListIterator {

        final protected ListIterator iterator;

        public ListIteratorWrapper(ListIterator iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            return iterator.next();
        }

        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        public Object previous() {
            return iterator.previous();
        }

        public int nextIndex() {
            return iterator.nextIndex();
        }

        public int previousIndex() {
            return iterator.previousIndex();
        }

        public void remove() {
            iterator.remove();
        }

        public void set(Object o) {
            iterator.set(o);
        }

        public void add(Object o) {
            iterator.add(o);
        }

    }

    /**
     * Implementation of a list that checks (predicates) each entry.
     */
    static class PredicatedList 
            extends CollectionUtils.PredicatedCollection
            implements List {

        public PredicatedList(List list, Predicate p) {
            super(list, p);
        }

        public boolean addAll(int i, Collection c) {
            for (Iterator iter = c.iterator(); iter.hasNext(); ) {
                validate(iter.next());
            }
            return getList().addAll(i, c);
        }

        public Object get(int i) {
            return getList().get(i);
        }

        public Object set(int i, Object o) {
            validate(o);
            return getList().set(i, o);
        }

        public void add(int i, Object o) {
            validate(o);
            getList().add(i, o);
        }

        public Object remove(int i) {
            return getList().remove(i);
        }

        public int indexOf(Object o) {
            return getList().indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return getList().lastIndexOf(o);
        }

        public ListIterator listIterator() {
            return listIterator(0);
        }

        public ListIterator listIterator(int i) {
            return new ListIteratorWrapper(getList().listIterator(i)) {
                public void add(Object o) {
                    validate(o);
                    iterator.add(o);
                }

                public void set(Object o) {
                    validate(o);
                    iterator.set(o);
                }
            };
        }

        public List subList(int i1, int i2) {
            List sub = getList().subList(i1, i2);
            return new PredicatedList(sub, predicate);
        }

        private List getList() {
            return (List)collection;
        }

    }

    /**
     * Implementation of a list that has a fixed size.
     */
    static class FixedSizeList 
            extends CollectionUtils.UnmodifiableCollection
            implements List {

        public FixedSizeList(List list) {
            super(list);
        }

        public boolean addAll(int i, Collection c) {
            throw new UnsupportedOperationException();
        }

        public Object get(int i) {
            return getList().get(i);
        }

        public Object set(int i, Object o) {
            return getList().set(i, o);
        }

        public void add(int i, Object o) {
            throw new UnsupportedOperationException();
        }

        public Object remove(int i) {
            throw new UnsupportedOperationException();
        }

        public int indexOf(Object o) {
            return getList().indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return getList().lastIndexOf(o);
        }

        public ListIterator listIterator() {
            return listIterator(0);
        }

        public ListIterator listIterator(int i) {
            return new ListIteratorWrapper(getList().listIterator(i)) {
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                public void add(Object o) {
                    throw new UnsupportedOperationException();
                }

                public void remove(Object o) {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public List subList(int i1, int i2) {
            List sub = getList().subList(i1, i2);
            return new FixedSizeList(sub);
        }

        private List getList() {
            return (List)collection;
        }

    }

    /**
     * Implementation of a list that creates objects on demand.
     */
    static class LazyList 
            extends CollectionUtils.CollectionWrapper 
            implements List {

        protected final Factory factory;

        public LazyList(List list, Factory factory) {
            super(list);
            if (factory == null) {
                throw new IllegalArgumentException("Factory must not be null");
            }
            this.factory = factory;
        }

        
        /* Proxy method to the impl's get method. With the exception that if it's out
         * of bounds, then the collection will grow, leaving place-holders in its
         * wake, so that an item can be set at any given index. Later the
         * place-holders are removed to return to a pure collection.
         *
         * If there's a place-holder at the index, then it's replaced with a proper
         * object to be used.
         */
        public Object get(int index) {
            Object obj;
            if (index < (getList().size())) {
            /* within bounds, get the object */
                obj = getList().get(index);
                if (obj == null) {
                    /* item is a place holder, create new one, set and return */
                    obj = this.factory.create();
                    this.getList().set(index, obj);
                    return obj;
                } else {
                    /* good and ready to go */
                    return obj;
                }
            } else {
                /* we have to grow the list */
                for (int i = getList().size(); i < index; i++) {
                    getList().add(null);
                }
                /* create our last object, set and return */
                obj = this.factory.create();
                getList().add(obj);
                return obj;
            }
        }


        /* proxy the call to the provided list implementation. */
        public List subList(int fromIndex, int toIndex) {
            /* wrap the returned sublist so it can continue the functionality */
            return new LazyList(getList().subList(fromIndex, toIndex), factory);
        }

        public boolean addAll(int i, Collection c) {
            return getList().addAll(i, c);
        }

        public Object set(int i, Object o) {
            return getList().set(i, o);
        }

        public void add(int i, Object o) {
            getList().add(i, o);
        }

        public Object remove(int i) {
            return getList().remove(i);
        }

        public int indexOf(Object o) {
            return getList().indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return getList().lastIndexOf(o);
        }

        public ListIterator listIterator() {
            return getList().listIterator();
        }

        public ListIterator listIterator(int i) {
            return getList().listIterator(i);
        }

        private List getList() {
            return (List)collection;
        }

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
     * This method uses the implementation in {@link java.util.Collections Collections}.
     * 
     * @param list  the list to synchronize, must not be null
     * @return a synchronized list backed by the given list
     * @throws IllegalArgumentException  if the list is null
     */
    public static List synchronizedList(List list) {
        return Collections.synchronizedList(list);
    }

    /**
     * Returns an unmodifiable list backed by the given list.
     * <p>
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param list  the list to make unmodifiable, must not be null
     * @return an unmodifiable list backed by the given list
     * @throws IllegalArgumentException  if the list is null
     */
    public static List unmodifiableList(List list) {
        return Collections.unmodifiableList(list);
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
        return new PredicatedList(list, predicate);
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
        return predicatedList(list, new CollectionUtils.InstanceofPredicate(type));
    }
    
    /**
     * Returns a "lazy" list whose elements will be created on demand.<P>
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
        return new LazyList(list, factory);
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
        return new FixedSizeList(list);
    }

}

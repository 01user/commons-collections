/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/observed/Attic/ObservableList.java,v 1.3 2003/10/13 21:18:56 scolebourne Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
package org.apache.commons.collections.observed;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.decorators.AbstractListIteratorDecorator;

/**
 * Decorates a <code>List</code> implementation to observe modifications.
 * <p>
 * Each modifying method call made on this <code>List</code> is forwarded to a
 * {@link ModificationHandler}.
 * The handler manages the event, notifying listeners and optionally vetoing changes.
 * The default handler is
 * {@link org.apache.commons.collections.observed.standard.StandardModificationHandler StandardModificationHandler}.
 * See this class for details of configuration available.
 * <p>
 * All indices on events returned by <code>subList</code> are relative to the
 * base <code>List</code>.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.3 $ $Date: 2003/10/13 21:18:56 $
 * 
 * @author Stephen Colebourne
 */
public class ObservableList extends ObservableCollection implements List {

    // Factories
    //-----------------------------------------------------------------------
    /**
     * Factory method to create an observable list.
     * <p>
     * A {@link org.apache.commons.collections.observed.standard.StandardModificationHandler} will be created.
     * This can be accessed by {@link #getHandler()} to add listeners.
     *
     * @param list  the list to decorate, must not be null
     * @return the observed List
     * @throws IllegalArgumentException if the list is null
     */
    public static ObservableList decorate(final List list) {
        return new ObservableList(list, null);
    }

    /**
     * Factory method to create an observable list using a listener or a handler.
     * <p>
     * A lot of functionality is available through this method.
     * If you don't need the extra functionality, simply implement the
     * {@link org.apache.commons.collections.observed.standard.StandardModificationListener}
     * interface and pass it in as the second parameter.
     * <p>
     * Internally, an <code>ObservableList</code> relies on a {@link ModificationHandler}.
     * The handler receives all the events and processes them, typically by
     * calling listeners. Different handler implementations can be plugged in
     * to provide a flexible event system.
     * <p>
     * The handler implementation is determined by the listener parameter via
     * the registered factories. The listener may be a manually configured 
     * <code>ModificationHandler</code> instance.
     * <p>
     * The listener is defined as an Object for maximum flexibility.
     * It does not have to be a listener in the classic JavaBean sense.
     * It is entirely up to the factory and handler as to how the parameter
     * is interpretted. An IllegalArgumentException is thrown if no suitable
     * handler can be found for this listener.
     * <p>
     * A <code>null</code> listener will create a
     * {@link org.apache.commons.collections.observed.standard.StandardModificationHandler}.
     *
     * @param list  the list to decorate, must not be null
     * @param listener  list listener, may be null
     * @return the observed list
     * @throws IllegalArgumentException if the list is null
     * @throws IllegalArgumentException if there is no valid handler for the listener
     */
    public static ObservableList decorate(
            final List list,
            final Object listener) {
        
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        return new ObservableList(list, listener);
    }

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies) and takes a handler.
     * <p>
     * The handler implementation is determined by the listener parameter via
     * the registered factories. The listener may be a manually configured 
     * <code>ModificationHandler</code> instance.
     * 
     * @param list  the list to decorate, must not be null
     * @param listener  the listener, may be null
     * @throws IllegalArgumentException if the list is null
     */
    protected ObservableList(
            final List list,
            final Object listener) {
        super(list, listener);
    }
    
    /**
     * Constructor used by subclass views, such as subList.
     * 
     * @param handler  the handler to use, must not be null
     * @param list  the subList to decorate, must not be null
     * @throws IllegalArgumentException if the list is null
     */
    protected ObservableList(
            final ModificationHandler handler,
            final List list) {
        super(handler, list);
    }
    
    /**
     * Typecast the collection to a List.
     * 
     * @return the wrapped collection as a List
     */
    private List getList() {
        return (List) getCollection();
    }

    // List API
    //-----------------------------------------------------------------------
    public Object get(int index) {
        return getList().get(index);
    }

    public int indexOf(Object object) {
        return getList().indexOf(object);
    }

    public int lastIndexOf(Object object) {
        return getList().lastIndexOf(object);
    }

    //-----------------------------------------------------------------------
    public void add(int index, Object object) {
        if (handler.preAddIndexed(index, object)) {
            getList().add(index, object);
            handler.postAddIndexed(index, object);
        }
    }

    public boolean addAll(int index, Collection coll) {
        boolean result = false;
        if (handler.preAddAllIndexed(index, coll)) {
            result = getList().addAll(index, coll);
            handler.postAddAllIndexed(index, coll, result);
        }
        return result;
    }

    public Object remove(int index) {
        Object result = null;
        if (handler.preRemoveIndexed(index)) {
            result = getList().remove(index);
            handler.postRemoveIndexed(index, result);
        }
        return result;
    }

    public Object set(int index, Object object) {
        Object result = null;
        if (handler.preSetIndexed(index, object)) {
            result = getList().set(index, object);
            handler.postSetIndexed(index, object, result);
        }
        return result;
    }

    public ListIterator listIterator() {
        return new ObservableListIterator(getList().listIterator());
    }

    public ListIterator listIterator(int index) {
        return new ObservableListIterator(getList().listIterator(index));
    }

    /**
     * Returns a subList view on the original base <code>List</code>.
     * <p>
     * Changes to the subList affect the underlying List. Change events will
     * return change indices relative to the underlying List, not the subList.
     * 
     * @param fromIndex  inclusive start index of the range
     * @param toIndex  exclusive end index of the range
     * @return the subList view
     */
    public List subList(int fromIndex, int toIndex) {
        List subList = getList().subList(fromIndex, toIndex);
        return new ObservableList(subList, getHandler().createSubListHandler(fromIndex, toIndex));
    }

    // ListIterator
    //-----------------------------------------------------------------------
    /**
     * Inner class ListIterator for the ObservableList.
     */
    protected class ObservableListIterator extends AbstractListIteratorDecorator {
        
        protected Object last;
        
        protected ObservableListIterator(ListIterator iterator) {
            super(iterator);
        }
        
        public Object next() {
            last = super.next();
            return last;
        }

        public Object previous() {
            last = iterator.previous();
            return last;
        }

        public void remove() {
            int index = iterator.previousIndex();
            if (handler.preRemoveIterated(index, last)) {
                iterator.remove();
                handler.postRemoveIterated(index, last);
            }
        }
        
        public void add(Object object) {
            int index = iterator.nextIndex();
            if (handler.preAddIterated(index, object)) {
                iterator.add(object);
                handler.postAddIterated(index, object);
            }
        }

        public void set(Object object) {
            int index = iterator.previousIndex();
            if (handler.preSetIterated(index, object, last)) {
                iterator.set(object);
                handler.postSetIterated(index, object, last);
            }
        }
    }

}

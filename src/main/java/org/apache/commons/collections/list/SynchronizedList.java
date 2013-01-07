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
package org.apache.commons.collections.list;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.collection.SynchronizedCollection;

/**
 * Decorates another <code>List</code> to synchronize its behaviour
 * for a multi-threaded environment.
 * <p>
 * Methods are synchronized, then forwarded to the decorated list.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since 3.0
 * @version $Id$
 */
public class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {

    /** Serialization version */
     private static final long serialVersionUID = -1403835447328619437L;

    /**
     * Factory method to create a synchronized list.
     * 
     * @param <T> the type of the elements in the list
     * @param list  the list to decorate, must not be null
     * @return a new synchronized list
     * @throws IllegalArgumentException if list is null
     */
    public static <T> SynchronizedList<T> synchronizedList(final List<T> list) {
        return new SynchronizedList<T>(list);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * 
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    protected SynchronizedList(final List<E> list) {
        super(list);
    }

    /**
     * Constructor that wraps (not copies).
     * 
     * @param list  the list to decorate, must not be null
     * @param lock  the lock to use, must not be null
     * @throws IllegalArgumentException if list is null
     */
    protected SynchronizedList(final List<E> list, final Object lock) {
        super(list, lock);
    }

    /**
     * Gets the decorated list.
     * 
     * @return the decorated list
     */
    protected List<E> getList() {
        return (List<E>) collection;
    }

    //-----------------------------------------------------------------------
    
    public void add(final int index, final E object) {
        synchronized (lock) {
            getList().add(index, object);
        }
    }

    public boolean addAll(final int index, final Collection<? extends E> coll) {
        synchronized (lock) {
            return getList().addAll(index, coll);
        }
    }

    public E get(final int index) {
        synchronized (lock) {
            return getList().get(index);
        }
    }

    public int indexOf(final Object object) {
        synchronized (lock) {
            return getList().indexOf(object);
        }
    }

    public int lastIndexOf(final Object object) {
        synchronized (lock) {
            return getList().lastIndexOf(object);
        }
    }

    /**
     * Iterators must be manually synchronized.
     * <pre>
     * synchronized (coll) {
     *   ListIterator it = coll.listIterator();
     *   // do stuff with iterator
     * }
     * 
     * @return an iterator that must be manually synchronized on the collection
     */
    public ListIterator<E> listIterator() {
        return getList().listIterator();
    }

    /**
     * Iterators must be manually synchronized.
     * <pre>
     * synchronized (coll) {
     *   ListIterator it = coll.listIterator(3);
     *   // do stuff with iterator
     * }
     * 
     * @param index  index of first element to be returned by this list iterator
     * @return an iterator that must be manually synchronized on the collection
     */
    public ListIterator<E> listIterator(final int index) {
        return getList().listIterator(index);
    }

    public E remove(final int index) {
        synchronized (lock) {
            return getList().remove(index);
        }
    }

    public E set(final int index, final E object) {
        synchronized (lock) {
            return getList().set(index, object);
        }
    }

    public List<E> subList(final int fromIndex, final int toIndex) {
        synchronized (lock) {
            final List<E> list = getList().subList(fromIndex, toIndex);
            // the lock is passed into the constructor here to ensure that the sublist is
            // synchronized on the same lock as the parent list
            return new SynchronizedList<E>(list, lock);
        }
    }

}

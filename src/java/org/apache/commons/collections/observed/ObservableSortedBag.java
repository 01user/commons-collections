/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/observed/Attic/ObservableSortedBag.java,v 1.4 2003/11/27 22:55:15 scolebourne Exp $
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

import java.util.Comparator;

import org.apache.commons.collections.SortedBag;

/**
 * Decorates a <code>SortedBag</code> implementation to observe modifications.
 * <p>
 * Each modifying method call made on this <code>SortedBag</code> is forwarded to a
 * {@link ModificationHandler}.
 * The handler manages the event, notifying listeners and optionally vetoing changes.
 * The default handler is
 * {@link org.apache.commons.collections.observed.standard.StandardModificationHandler StandardModificationHandler}.
 * See this class for details of configuration available.
 *
 * @deprecated TO BE REMOVED BEFORE v3.0
 * @since Commons Collections 3.0
 * @version $Revision: 1.4 $ $Date: 2003/11/27 22:55:15 $
 * 
 * @author Stephen Colebourne
 */
public class ObservableSortedBag extends ObservableBag implements SortedBag {
    
    // Factories
    //-----------------------------------------------------------------------
    /**
     * Factory method to create an observable bag.
     * <p>
     * A {@link org.apache.commons.collections.observed.standard.StandardModificationHandler} will be created.
     * This can be accessed by {@link #getHandler()} to add listeners.
     *
     * @param bag  the bag to decorate, must not be null
     * @return the observed bag
     * @throws IllegalArgumentException if the collection is null
     */
    public static ObservableSortedBag decorate(final SortedBag bag) {
        return new ObservableSortedBag(bag, null);
    }

    /**
     * Factory method to create an observable bag using a listener or a handler.
     * <p>
     * A lot of functionality is available through this method.
     * If you don't need the extra functionality, simply implement the
     * {@link org.apache.commons.collections.observed.standard.StandardModificationListener}
     * interface and pass it in as the second parameter.
     * <p>
     * Internally, an <code>ObservableSortedBag</code> relies on a {@link ModificationHandler}.
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
     * @param bag  the bag to decorate, must not be null
     * @param listener  bag listener, may be null
     * @return the observed bag
     * @throws IllegalArgumentException if the bag is null
     * @throws IllegalArgumentException if there is no valid handler for the listener
     */
    public static ObservableSortedBag decorate(
            final SortedBag bag,
            final Object listener) {
        
        if (bag == null) {
            throw new IllegalArgumentException("SortedBag must not be null");
        }
        return new ObservableSortedBag(bag, listener);
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
     * @param bag  the bag to decorate, must not be null
     * @param listener  the listener, may be null
     * @throws IllegalArgumentException if the bag is null
     */
    protected ObservableSortedBag(
            final SortedBag bag,
            final Object listener) {
        super(bag, listener);
    }

    /**
     * Typecast the collection to a SortedBag.
     * 
     * @return the wrapped collection as a SortedBag
     */
    private SortedBag getSortedBag() {
        return (SortedBag) getCollection();
    }

    // SortedBag API
    //-----------------------------------------------------------------------
    public Comparator comparator() {
        return getSortedBag().comparator();
    }

    public Object first() {
        return getSortedBag().first();
    }

    public Object last() {
        return getSortedBag().last();
    }

}

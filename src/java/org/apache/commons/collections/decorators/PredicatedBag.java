/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/decorators/Attic/PredicatedBag.java,v 1.2 2003/08/31 12:56:23 scolebourne Exp $
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
package org.apache.commons.collections.decorators;

import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Predicate;

/**
 * <code>PredicatedBag</code> decorates another <code>Bag</code>
 * to validate additions match a specified predicate.
 * <p>
 * If an object cannot be added to the list, an IllegalArgumentException
 * is thrown.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.2 $ $Date: 2003/08/31 12:56:23 $
 * 
 * @author Stephen Colebourne
 * @author Paul Jack
 */
public class PredicatedBag extends PredicatedCollection implements Bag {

    /**
     * Factory method to create a predicated (validating) bag.
     * <p>
     * If there are any elements already in the bag being decorated, they
     * are validated.
     * 
     * @param bag  the bag to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @throws IllegalArgumentException if bag or predicate is null
     * @throws IllegalArgumentException if the bag contains invalid elements
     */
    public static Bag decorate(Bag bag, Predicate predicate) {
        return new PredicatedBag(bag, predicate);
    }

    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the bag being decorated, they
     * are validated.
     * 
     * @param bag  the bag to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @throws IllegalArgumentException if bag or predicate is null
     * @throws IllegalArgumentException if the bag contains invalid elements
     */
    protected PredicatedBag(Bag bag, Predicate predicate) {
        super(bag, predicate);
    }

    /**
     * Gets the decorated bag.
     * 
     * @return the decorated bag
     */
    protected Bag getBag() {
        return (Bag) getCollection();
    }
    
    //-----------------------------------------------------------------------
    public boolean add(Object object, int count) {
        validate(object);
        return getBag().add(object, count);
    }

    public boolean remove(Object object, int count) {
        return getBag().remove(object, count);
    }

    public Set uniqueSet() {
        return getBag().uniqueSet();
    }

    public int getCount(Object object) {
        return getBag().getCount(object);
    }

}

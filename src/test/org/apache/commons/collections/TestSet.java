/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/Attic/TestSet.java,v 1.3 2003/07/12 15:11:25 scolebourne Exp $
 * $Revision: 1.3 $
 * $Date: 2003/07/12 15:11:25 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
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
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  Tests base {@link Set} methods and contracts.<P>
 *
 *  Since {@link Set} doesn't stipulate much new behavior that isn't already
 *  found in {@link Collection}, this class basically just adds tests for
 *  {@link Set#equals()} and {@link Set#hashCode()} along with an updated
 *  {@link #verify()} that ensures elements do not appear more than once in the
 *  set.<P>
 *
 *  To use, subclass and override the {@link #makeEmptySet()}
 *  method.  You may have to override other protected methods if your
 *  set is not modifiable, or if your set restricts what kinds of
 *  elements may be added; see {@link TestCollection} for more details.<P>
 *
 *  @author Paul Jack
 *  @version $Id: TestSet.java,v 1.3 2003/07/12 15:11:25 scolebourne Exp $
 */
public abstract class TestSet extends TestCollection {

    /**
     *  Constructor.
     *
     *  @param name  name for test
     */
    public TestSet(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    /**
     *  Provides additional verifications for sets.
     */
    protected void verify() {
        super.verify();
        assertEquals("Sets should be equal", confirmed, collection);
        assertEquals("Sets should have equal hashCodes", 
                     confirmed.hashCode(), collection.hashCode());
        HashSet set = new HashSet();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            assertTrue("Set.iterator should only return unique elements", 
                       set.add(iterator.next()));
        }
    }

    //-----------------------------------------------------------------------
    /**
     *  Returns an empty {@link HashSet} for use in modification testing.
     *
     *  @return a confirmed empty collection
     */
    protected Collection makeConfirmedCollection() {
        return new HashSet();
    }

    /**
     *  Returns a full {@link HashSet} for use in modification testing.
     *
     *  @return a confirmed full collection
     */
    protected Collection makeConfirmedFullCollection() {
        HashSet set = new HashSet();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    /**
     *  Makes an empty set.  The returned set should have no elements.
     *
     *  @return an empty set
     */
    protected abstract Set makeEmptySet();

    /**
     *  Makes a full set by first creating an empty set and then adding
     *  all the elements returned by {@link #getFullElements()}.
     *
     *  Override if your set does not support the add operation.
     *
     *  @return a full set
     */
    protected Set makeFullSet() {
        Set set = makeEmptySet();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    /**
     *  Makes an empty collection by invoking {@link #makeEmptySet()}.  
     *
     *  @return an empty collection
     */
    protected final Collection makeCollection() {
        return makeEmptySet();
    }

    /**
     *  Makes a full collection by invoking {@link #makeFullSet()}.
     *
     *  @return a full collection
     */
    protected final Collection makeFullCollection() {
        return makeFullSet();
    }

    //-----------------------------------------------------------------------
    /**
     *  Return the {@link TestCollection#collection} fixture, but cast as a
     *  Set.  
     */
    protected Set getSet() {
        return (Set)collection;
    }

    /**
     *  Return the {@link TestCollection#confirmed} fixture, but cast as a 
     *  Set.
     **/
    protected Set getConfirmedSet() {
        return (Set)confirmed;
    }

    //-----------------------------------------------------------------------
    /**
     *  Tests {@link Set#equals(Object)}.
     */
    public void testSetEquals() {
        resetEmpty();
        assertEquals("Empty sets should be equal", 
                     getSet(), getConfirmedSet());
        verify();

        HashSet set2 = new HashSet();
        set2.add("foo");
        assertTrue("Empty set shouldn't equal nonempty set", 
                   !getSet().equals(set2));

        resetFull();
        assertEquals("Full sets should be equal", getSet(), getConfirmedSet());
        verify();

        set2.clear();
        set2.addAll(Arrays.asList(getOtherElements()));
        assertTrue("Sets with different contents shouldn't be equal", 
                   !getSet().equals(set2));
    }


    /**
     *  Tests {@link Set#hashCode()}.
     */
    public void testSetHashCode() {
        resetEmpty();
        assertEquals("Empty sets have equal hashCodes", 
                     getSet().hashCode(), getConfirmedSet().hashCode());

        resetFull();
        assertEquals("Equal sets have equal hashCodes", 
                     getSet().hashCode(), getConfirmedSet().hashCode());
    }


}

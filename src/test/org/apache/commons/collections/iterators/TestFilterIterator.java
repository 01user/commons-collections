/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/iterators/TestFilterIterator.java,v 1.3 2002/11/01 19:07:54 rwaldhoff Exp $
 * $Revision: 1.3 $
 * $Date: 2002/11/01 19:07:54 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
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


// TestFilterIterator.java 
package org.apache.commons.collections.iterators;

import junit.framework.TestSuite;
import junit.framework.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.Predicate;

/**
 *
 * @author  Jan Sorensen
 */
public class TestFilterIterator extends TestIterator {

    /** Creates new TestFilterIterator */
    public TestFilterIterator(String name) {
        super(name);
    }

    private String[] array;
    private FilterIterator iterator;
    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
        array = new String[] { "a", "b", "c" };
        initIterator();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() throws Exception {
        iterator = null;
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(TestFilterIterator.class));
    }

    /**
     * Returns an full iterator wrapped in a
     * FilterIterator that blocks all the elements
     * 
     * @return "empty" FilterIterator
     */
    public Iterator makeEmptyIterator() {
        return makeBlockAllFilter(new ArrayIterator(array));
    }

    /**
     * Returns an array with elements wrapped in a pass-through
     * FilterIterator
     * 
     * @return 
     */
    public Iterator makeFullIterator() {
        return makePassThroughFilter(new ArrayIterator(array));
    }

    public Object makeObject() {
        return makeFullIterator();
    }

    public void testRepeatedHasNext() {
        for (int i = 0; i <= array.length; i++) {
            assertTrue(iterator.hasNext());
        }
    }

    public void testRepeatedNext() {
        for (int i = 0; i < array.length; i++)
            iterator.next();
        verifyNoMoreElements();
    }

    public void testReturnValues() {
        verifyElementsInPredicate(new String[0]);
        verifyElementsInPredicate(new String[] { "a" });
        verifyElementsInPredicate(new String[] { "b" });
        verifyElementsInPredicate(new String[] { "c" });
        verifyElementsInPredicate(new String[] { "a", "b" });
        verifyElementsInPredicate(new String[] { "a", "c" });
        verifyElementsInPredicate(new String[] { "b", "c" });
        verifyElementsInPredicate(new String[] { "a", "b", "c" });
    }

    private void verifyNoMoreElements() {
        assertTrue(!iterator.hasNext());
        try {
            iterator.next();
            fail("NoSuchElementException expected");
        }
        catch (NoSuchElementException e) {
            // success
        }
    }

    private void verifyElementsInPredicate(final String[] elements) {
        Predicate pred = new Predicate() {
            public boolean evaluate(Object x) {
                for (int i = 0; i < elements.length; i++)
                    if (elements[i].equals(x))
                        return true;
                return false;
            }
        };
        initIterator();
        iterator.setPredicate(pred);
        for (int i = 0; i < elements.length; i++) {
            String s = (String)iterator.next();
            assertEquals(elements[i], s);
            assertTrue(i == elements.length - 1 ? !iterator.hasNext() : iterator.hasNext());
        }
        verifyNoMoreElements();
    }

    private void initIterator() {
        iterator = makePassThroughFilter(new ArrayIterator(array));
    }

    /**
     * Returns a FilterIterator that does not filter
     * any of its elements
     * 
     * @param i      the Iterator to "filter"
     * @return "filtered" iterator
     */
    protected FilterIterator makePassThroughFilter(Iterator i) {
        Predicate pred = new Predicate() {
                public boolean evaluate(Object x) { return true; }
        };
        return new FilterIterator(i,pred);
    }

    /**
     * Returns a FilterIterator that blocks
     * all of its elements
     * 
     * @param i      the Iterator to "filter"
     * @return "filtered" iterator
     */
    protected FilterIterator makeBlockAllFilter(Iterator i) {
        Predicate pred = new Predicate() {
                public boolean evaluate(Object x) { return false; }
        };
        return new FilterIterator(i,pred);
    }
}


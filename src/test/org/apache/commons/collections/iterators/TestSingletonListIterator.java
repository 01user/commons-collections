/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2004 The Apache Software Foundation.  All rights
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
 */
package org.apache.commons.collections.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.ResettableListIterator;

/**
 * Tests the SingletonListIterator.
 *
 * @version $Revision: 1.10 $ $Date: 2004/01/14 21:34:26 $
 * 
 * @author Stephen Colebourne
 */
public class TestSingletonListIterator extends AbstractTestListIterator {

    private static final Object testValue = "foo";
    
    public static Test suite() {
        return new TestSuite(TestSingletonListIterator.class);
    }
    
    public TestSingletonListIterator(String testName) {
        super(testName);
    }
    
    /**
     * Returns a SingletonListIterator from which 
     * the element has already been removed.
     */
    public ListIterator makeEmptyListIterator() {
        SingletonListIterator iter = (SingletonListIterator)makeFullIterator();
        iter.next();
        iter.remove();
        iter.reset();        
        return iter;
    }

    public ListIterator makeFullListIterator() {
        return new SingletonListIterator( testValue );
    }

    public boolean supportsAdd() {
        return false;
    }

    public boolean supportsRemove() {
        return true;
    }

    public boolean supportsEmptyIterator() {
        return true;
    }

    public void testIterator() {
        ListIterator iter = (ListIterator) makeObject();
        assertTrue( "Iterator should have next item", iter.hasNext() );
        assertTrue( "Iterator should have no previous item", !iter.hasPrevious() );
        assertEquals( "Iteration next index", 0, iter.nextIndex() );
        assertEquals( "Iteration previous index", -1, iter.previousIndex() );
        
        Object iterValue = iter.next();
        assertEquals( "Iteration value is correct", testValue, iterValue );
        
        assertTrue( "Iterator should have no next item", !iter.hasNext() );
        assertTrue( "Iterator should have previous item", iter.hasPrevious() );
        assertEquals( "Iteration next index", 1, iter.nextIndex() );
        assertEquals( "Iteration previous index", 0, iter.previousIndex() );

        iterValue = iter.previous();
        assertEquals( "Iteration value is correct", testValue, iterValue );
        
        assertTrue( "Iterator should have next item", iter.hasNext() );
        assertTrue( "Iterator should have no previous item", !iter.hasPrevious() );
        assertEquals( "Iteration next index", 0, iter.nextIndex() );
        assertEquals( "Iteration previous index", -1, iter.previousIndex() );

        iterValue = iter.next();
        assertEquals( "Iteration value is correct", testValue, iterValue );
        
        assertTrue( "Iterator should have no next item", !iter.hasNext() );
        assertTrue( "Iterator should have previous item", iter.hasPrevious() );
        assertEquals( "Iteration next index", 1, iter.nextIndex() );
        assertEquals( "Iteration previous index", 0, iter.previousIndex() );

    	try {
    	    iter.next();
    	} catch (Exception e) {
    	  assertTrue("NoSuchElementException must be thrown", 
    		 e.getClass().equals((new NoSuchElementException()).getClass()));
    	}
        iter.previous();
        try {
            iter.previous();
        } catch (Exception e) {
          assertTrue("NoSuchElementException must be thrown", 
             e.getClass().equals((new NoSuchElementException()).getClass()));
        }
    }
    
    public void testReset() {
        ResettableListIterator it = (ResettableListIterator) makeObject();
        
        assertEquals(true, it.hasNext());
        assertEquals(false, it.hasPrevious());
        assertEquals(testValue, it.next());
        assertEquals(false, it.hasNext());
        assertEquals(true, it.hasPrevious());

        it.reset();
        
        assertEquals(true, it.hasNext());
        assertEquals(false, it.hasPrevious());
        assertEquals(testValue, it.next());
        assertEquals(false, it.hasNext());
        assertEquals(true, it.hasPrevious());
        
        it.reset();
        it.reset();
        
        assertEquals(true, it.hasNext());
    }
    
}


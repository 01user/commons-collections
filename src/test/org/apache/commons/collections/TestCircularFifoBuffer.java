/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/Attic/TestCircularFifoBuffer.java,v 1.4 2003/10/02 22:14:29 scolebourne Exp $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

/**
 * Test cases for CircularFifoBuffer.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/10/02 22:14:29 $
 * 
 * @author Stephen Colebourne
 */
public class TestCircularFifoBuffer extends AbstractTestCollection {

    public TestCircularFifoBuffer(String n) {
        super(n);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestCircularFifoBuffer.class);
    }

    //-----------------------------------------------------------------------
    /**
     *  Runs through the regular verifications, but also verifies that 
     *  the buffer contains the same elements in the same sequence as the
     *  list.
     */
    protected void verify() {
        super.verify();
        Iterator iterator1 = collection.iterator();
        Iterator iterator2 = confirmed.iterator();
        while (iterator2.hasNext()) {
            assertTrue(iterator1.hasNext());
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            assertEquals(o1, o2);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden because UnboundedFifoBuffer doesn't allow null elements.
     * @return false
     */
    protected boolean isNullSupported() {
        return false;
    }

    /**
     * Overridden because UnboundedFifoBuffer isn't fail fast.
     * @return false
     */
    protected boolean isFailFastSupported() {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns an empty ArrayList.
     *
     * @return an empty ArrayList
     */
    protected Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    /**
     * Returns a full ArrayList.
     *
     * @return a full ArrayList
     */
    protected Collection makeConfirmedFullCollection() {
        Collection c = makeConfirmedCollection();
        c.addAll(java.util.Arrays.asList(getFullElements()));
        return c;
    }

    /**
     * Returns an empty BoundedFifoBuffer that won't overflow.  
     *  
     * @return an empty BoundedFifoBuffer
     */
    protected Collection makeCollection() {
        return new CircularFifoBuffer(100);
    }

    //-----------------------------------------------------------------------
    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testCircularFifoBufferCircular() {
        List list = new ArrayList();
        list.add("A");
        list.add("B");
        list.add("C");
        Buffer buf = new CircularFifoBuffer(list);
        
        assertEquals(true, buf.contains("A"));
        assertEquals(true, buf.contains("B"));
        assertEquals(true, buf.contains("C"));
        
        buf.add("D");
        
        assertEquals(false, buf.contains("A"));
        assertEquals(true, buf.contains("B"));
        assertEquals(true, buf.contains("C"));
        assertEquals(true, buf.contains("D"));
        
        assertEquals("B", buf.get());
        assertEquals("B", buf.remove());
        assertEquals("C", buf.remove());
        assertEquals("D", buf.remove());
    }

    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testCircularFifoBufferRemove() {
        resetFull();
        int size = confirmed.size();
        for (int i = 0; i < size; i++) {
            Object o1 = ((CircularFifoBuffer) collection).remove();
            Object o2 = ((ArrayList) confirmed).remove(0);
            assertEquals("Removed objects should be equal", o1, o2);
            verify();
        }

        try {
            ((CircularFifoBuffer) collection).remove();
            fail("Empty buffer should raise Underflow.");
        } catch (BufferUnderflowException e) {
            // expected
        }
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException1() {
        try {
            new CircularFifoBuffer(0);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException2() {
        try {
            new CircularFifoBuffer(-20);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }
    
    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException3() {
        try {
            new CircularFifoBuffer(null);
        } catch (NullPointerException ex) {
            return;
        }
        fail();
    }
}

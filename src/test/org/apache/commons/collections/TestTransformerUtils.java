/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/TestTransformerUtils.java,v 1.6 2003/11/23 23:25:33 scolebourne Exp $
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.FunctorException;
import org.apache.commons.collections.functors.NOPTransformer;

/**
 * Tests the org.apache.commons.collections.TransformerUtils class.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 1.6 $ $Date: 2003/11/23 23:25:33 $
 *
 * @author Stephen Colebourne
 * @author James Carman
 */
public class TestTransformerUtils extends junit.framework.TestCase {

    private static final Object cObject = new Object();
    private static final Object cString = "Hello";
    private static final Object cInteger = new Integer(6);

    /**
     * Construct
     */
    public TestTransformerUtils(String name) {
        super(name);
    }

    /**
     * Main.
     * @param args
     */    
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Return class as a test suite.
     */
    public static Test suite() {
        return new TestSuite(TestTransformerUtils.class);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
    }

    // exceptionTransformer
    //------------------------------------------------------------------

    public void testExceptionTransformer() {
        assertNotNull(TransformerUtils.exceptionTransformer());
        assertSame(TransformerUtils.exceptionTransformer(), TransformerUtils.exceptionTransformer());
        try {
            TransformerUtils.exceptionTransformer().transform(null);
        } catch (FunctorException ex) {
            try {
                TransformerUtils.exceptionTransformer().transform(cString);
            } catch (FunctorException ex2) {
                return;
            }
        }
        fail();
    }
    
    // nullTransformer
    //------------------------------------------------------------------

    public void testNullTransformer() {
        assertNotNull(TransformerUtils.nullTransformer());
        assertSame(TransformerUtils.nullTransformer(), TransformerUtils.nullTransformer());
        assertEquals(null, TransformerUtils.nullTransformer().transform(null));
        assertEquals(null, TransformerUtils.nullTransformer().transform(cObject));
        assertEquals(null, TransformerUtils.nullTransformer().transform(cString));
        assertEquals(null, TransformerUtils.nullTransformer().transform(cInteger));
    }

    // nopTransformer
    //------------------------------------------------------------------

    public void testNopTransformer() {
        assertNotNull(TransformerUtils.nullTransformer());
        assertSame(TransformerUtils.nullTransformer(), TransformerUtils.nullTransformer());
        assertEquals(null, TransformerUtils.nopTransformer().transform(null));
        assertEquals(cObject, TransformerUtils.nopTransformer().transform(cObject));
        assertEquals(cString, TransformerUtils.nopTransformer().transform(cString));
        assertEquals(cInteger, TransformerUtils.nopTransformer().transform(cInteger));
    }

    // constantTransformer
    //------------------------------------------------------------------

    public void testConstantTransformer() {
        assertEquals(cObject, TransformerUtils.constantTransformer(cObject).transform(null));
        assertEquals(cObject, TransformerUtils.constantTransformer(cObject).transform(cObject));
        assertEquals(cObject, TransformerUtils.constantTransformer(cObject).transform(cString));
        assertEquals(cObject, TransformerUtils.constantTransformer(cObject).transform(cInteger));
    }

    // cloneTransformer
    //------------------------------------------------------------------

    public void testCloneTransformer() {
        assertEquals(null, TransformerUtils.cloneTransformer().transform(null));
        assertEquals(cString, TransformerUtils.cloneTransformer().transform(cString));
        assertEquals(cInteger, TransformerUtils.cloneTransformer().transform(cInteger));
        try {
            assertEquals(cObject, TransformerUtils.cloneTransformer().transform(cObject));
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    // mapTransformer
    //------------------------------------------------------------------

    public void testMapTransformer() {
        Map map = new HashMap();
        map.put(null, new Integer(0));
        map.put(cObject, new Integer(1));
        map.put(cString, new Integer(2));
        assertEquals(new Integer(0), TransformerUtils.mapTransformer(map).transform(null));
        assertEquals(new Integer(1), TransformerUtils.mapTransformer(map).transform(cObject));
        assertEquals(new Integer(2), TransformerUtils.mapTransformer(map).transform(cString));
        assertEquals(null, TransformerUtils.mapTransformer(map).transform(cInteger));
    }

    // commandTransformer
    //------------------------------------------------------------------

    public void testExecutorTransformer() {
        assertEquals(null, TransformerUtils.asTransformer(ClosureUtils.nopClosure()).transform(null));
        assertEquals(cObject, TransformerUtils.asTransformer(ClosureUtils.nopClosure()).transform(cObject));
        assertEquals(cString, TransformerUtils.asTransformer(ClosureUtils.nopClosure()).transform(cString));
        assertEquals(cInteger, TransformerUtils.asTransformer(ClosureUtils.nopClosure()).transform(cInteger));
        try {
            TransformerUtils.asTransformer((Closure) null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    // predicateTransformer
    //------------------------------------------------------------------

    public void testPredicateTransformer() {
        assertEquals(Boolean.TRUE, TransformerUtils.asTransformer(PredicateUtils.truePredicate()).transform(null));
        assertEquals(Boolean.TRUE, TransformerUtils.asTransformer(PredicateUtils.truePredicate()).transform(cObject));
        assertEquals(Boolean.TRUE, TransformerUtils.asTransformer(PredicateUtils.truePredicate()).transform(cString));
        assertEquals(Boolean.TRUE, TransformerUtils.asTransformer(PredicateUtils.truePredicate()).transform(cInteger));
        try {
            TransformerUtils.asTransformer((Predicate) null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    // factoryTransformer
    //------------------------------------------------------------------

    public void testFactoryTransformer() {
        assertEquals(null, TransformerUtils.asTransformer(FactoryUtils.nullFactory()).transform(null));
        assertEquals(null, TransformerUtils.asTransformer(FactoryUtils.nullFactory()).transform(cObject));
        assertEquals(null, TransformerUtils.asTransformer(FactoryUtils.nullFactory()).transform(cString));
        assertEquals(null, TransformerUtils.asTransformer(FactoryUtils.nullFactory()).transform(cInteger));
        try {
            TransformerUtils.asTransformer((Factory) null);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    // chainedTransformer
    //------------------------------------------------------------------

    public void testChainedTransformer() {
        Transformer a = TransformerUtils.constantTransformer("A");
        Transformer b = TransformerUtils.constantTransformer("B");
        
        assertEquals("A", TransformerUtils.chainedTransformer(b, a).transform(null));
        assertEquals("B", TransformerUtils.chainedTransformer(a, b).transform(null));
        assertEquals("A", TransformerUtils.chainedTransformer(new Transformer[] {b, a}).transform(null));
        Collection coll = new ArrayList();
        coll.add(b);
        coll.add(a);
        assertEquals("A", TransformerUtils.chainedTransformer(coll).transform(null));

        assertSame(NOPTransformer.INSTANCE, TransformerUtils.chainedTransformer(new Transformer[0]));
        assertSame(NOPTransformer.INSTANCE, TransformerUtils.chainedTransformer(Collections.EMPTY_LIST));
        
        try {
            TransformerUtils.chainedTransformer(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.chainedTransformer((Transformer[]) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.chainedTransformer((Collection) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.chainedTransformer(new Transformer[] {null, null});
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            coll = new ArrayList();
            coll.add(null);
            coll.add(null);
            TransformerUtils.chainedTransformer(coll);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    // switchTransformer
    //------------------------------------------------------------------

    public void testSwitchTransformer() {
        Transformer a = TransformerUtils.constantTransformer("A");
        Transformer b = TransformerUtils.constantTransformer("B");
        Transformer c = TransformerUtils.constantTransformer("C");
        
        assertEquals("A", TransformerUtils.switchTransformer(PredicateUtils.truePredicate(), a, b).transform(null));
        assertEquals("B", TransformerUtils.switchTransformer(PredicateUtils.falsePredicate(), a, b).transform(null));
        
        assertEquals(null, TransformerUtils.switchTransformer(
            new Predicate[] {PredicateUtils.equalPredicate("HELLO"), PredicateUtils.equalPredicate("THERE")}, 
            new Transformer[] {a, b}).transform("WELL"));
        assertEquals("A", TransformerUtils.switchTransformer(
            new Predicate[] {PredicateUtils.equalPredicate("HELLO"), PredicateUtils.equalPredicate("THERE")}, 
            new Transformer[] {a, b}).transform("HELLO"));
        assertEquals("B", TransformerUtils.switchTransformer(
            new Predicate[] {PredicateUtils.equalPredicate("HELLO"), PredicateUtils.equalPredicate("THERE")}, 
            new Transformer[] {a, b}).transform("THERE"));
            
        assertEquals("C", TransformerUtils.switchTransformer(
            new Predicate[] {PredicateUtils.equalPredicate("HELLO"), PredicateUtils.equalPredicate("THERE")}, 
            new Transformer[] {a, b}, c).transform("WELL"));
            
        Map map = new HashMap();
        map.put(PredicateUtils.equalPredicate("HELLO"), a);
        map.put(PredicateUtils.equalPredicate("THERE"), b);
        assertEquals(null, TransformerUtils.switchTransformer(map).transform("WELL"));
        assertEquals("A", TransformerUtils.switchTransformer(map).transform("HELLO"));
        assertEquals("B", TransformerUtils.switchTransformer(map).transform("THERE"));
        map.put(null, c);
        assertEquals("C", TransformerUtils.switchTransformer(map).transform("WELL"));

        assertSame(ConstantTransformer.NULL_INSTANCE, TransformerUtils.switchTransformer(new Predicate[0], new Transformer[0]));
        assertSame(ConstantTransformer.NULL_INSTANCE, TransformerUtils.switchTransformer(new HashMap()));
        map = new HashMap();
        map.put(null, null);
        assertSame(ConstantTransformer.NULL_INSTANCE, TransformerUtils.switchTransformer(map));
            
        try {
            TransformerUtils.switchTransformer(null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.switchTransformer((Predicate[]) null, (Transformer[]) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.switchTransformer((Map) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.switchTransformer(new Predicate[2], new Transformer[2]);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.switchTransformer(new Predicate[2], new Transformer[1]);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    // switchMapTransformer
    //------------------------------------------------------------------

    public void testSwitchMapTransformer() {
        Transformer a = TransformerUtils.constantTransformer("A");
        Transformer b = TransformerUtils.constantTransformer("B");
        Transformer c = TransformerUtils.constantTransformer("C");
        
        Map map = new HashMap();
        map.put("HELLO", a);
        map.put("THERE", b);
        assertEquals(null, TransformerUtils.switchMapTransformer(map).transform("WELL"));
        assertEquals("A", TransformerUtils.switchMapTransformer(map).transform("HELLO"));
        assertEquals("B", TransformerUtils.switchMapTransformer(map).transform("THERE"));
        map.put(null, c);
        assertEquals("C", TransformerUtils.switchMapTransformer(map).transform("WELL"));

        assertSame(ConstantTransformer.NULL_INSTANCE, TransformerUtils.switchMapTransformer(new HashMap()));
        map = new HashMap();
        map.put(null, null);
        assertSame(ConstantTransformer.NULL_INSTANCE, TransformerUtils.switchMapTransformer(map));
        
        try {
            TransformerUtils.switchMapTransformer(null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    // invokerTransformer
    //------------------------------------------------------------------

    public void testInvokerTransformer() {
        List list = new ArrayList();
        assertEquals(new Integer(0), TransformerUtils.invokerTransformer("size").transform(list));
        list.add(new Object());
        assertEquals(new Integer(1), TransformerUtils.invokerTransformer("size").transform(list));
        assertEquals(null, TransformerUtils.invokerTransformer("size").transform(null));

        try {
            TransformerUtils.invokerTransformer(null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.invokerTransformer("noSuchMethod").transform(new Object());
            fail();
        } catch (FunctorException ex) {}
    }
    
    // invokerTransformer2
    //------------------------------------------------------------------

    public void testInvokerTransformer2() {
        List list = new ArrayList();
        assertEquals(Boolean.FALSE, TransformerUtils.invokerTransformer(
            "contains", new Class[] {Object.class}, new Object[] {cString}).transform(list));
        list.add(cString);
        assertEquals(Boolean.TRUE, TransformerUtils.invokerTransformer(
            "contains", new Class[] {Object.class}, new Object[] {cString}).transform(list));
        assertEquals(null, TransformerUtils.invokerTransformer(
            "contains", new Class[] {Object.class}, new Object[] {cString}).transform(null));

        try {
            TransformerUtils.invokerTransformer(null, null, null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            TransformerUtils.invokerTransformer(
                "noSuchMethod", new Class[] {Object.class}, new Object[] {cString}).transform(new Object());
            fail();
        } catch (FunctorException ex) {}
    }
    
    // stringValueTransformer
    //------------------------------------------------------------------
    
    public void testStringValueTransformer() {
        assertNotNull( "StringValueTransformer should NEVER return a null value.",
           TransformerUtils.stringValueTransformer().transform(null));
        assertEquals( "StringValueTransformer should return \"null\" when given a null argument.", "null",
            TransformerUtils.stringValueTransformer().transform(null));
        assertEquals( "StringValueTransformer should return toString value", "6",
            TransformerUtils.stringValueTransformer().transform(new Integer(6)));
    }
    
}

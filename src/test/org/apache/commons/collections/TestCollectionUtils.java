/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/TestCollectionUtils.java,v 1.3 2002/08/10 00:36:34 pjack Exp $
 * $Revision: 1.3 $
 * $Date: 2002/08/10 00:36:34 $
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

import junit.framework.*;
import java.util.*;

/**
 * @author Rodney Waldhoff
 * @version $Id: TestCollectionUtils.java,v 1.3 2002/08/10 00:36:34 pjack Exp $
 */
public class TestCollectionUtils extends TestCase {
    public TestCollectionUtils(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestCollectionUtils.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestCollectionUtils.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    private Collection _a = null;
    private Collection _b = null;

    public void setUp() {
        _a = new ArrayList();
        _a.add("a");
        _a.add("b");
        _a.add("b");
        _a.add("c");
        _a.add("c");
        _a.add("c");
        _a.add("d");
        _a.add("d");
        _a.add("d");
        _a.add("d");
        _b = new LinkedList();
        _b.add("e");
        _b.add("d");
        _b.add("d");
        _b.add("c");
        _b.add("c");
        _b.add("c");
        _b.add("b");
        _b.add("b");
        _b.add("b");
        _b.add("b");

    }

    public void testGetCardinalityMap() {
        Map freq = CollectionUtils.getCardinalityMap(_a);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(4),freq.get("d"));
        assertTrue(null == freq.get("e"));

        freq = CollectionUtils.getCardinalityMap(_b);
        assertTrue(null == freq.get("a"));
        assertEquals(new Integer(4),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));
    }

    public void testCardinality() {
        assertEquals(1,CollectionUtils.cardinality("a",_a));
        assertEquals(2,CollectionUtils.cardinality("b",_a));
        assertEquals(3,CollectionUtils.cardinality("c",_a));
        assertEquals(4,CollectionUtils.cardinality("d",_a));
        assertEquals(0,CollectionUtils.cardinality("e",_a));

        assertEquals(0,CollectionUtils.cardinality("a",_b));
        assertEquals(4,CollectionUtils.cardinality("b",_b));
        assertEquals(3,CollectionUtils.cardinality("c",_b));
        assertEquals(2,CollectionUtils.cardinality("d",_b));
        assertEquals(1,CollectionUtils.cardinality("e",_b));
    }

    public void testUnion() {
        Collection col = CollectionUtils.union(_a,_b);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(4),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(4),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));

        Collection col2 = CollectionUtils.union(_b,_a);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("a"));
        assertEquals(new Integer(4),freq2.get("b"));
        assertEquals(new Integer(3),freq2.get("c"));
        assertEquals(new Integer(4),freq2.get("d"));
        assertEquals(new Integer(1),freq2.get("e"));
    }

    public void testIntersection() {
        Collection col = CollectionUtils.intersection(_a,_b);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertTrue(null == freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertEquals(new Integer(3),freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertTrue(null == freq.get("e"));

        Collection col2 = CollectionUtils.intersection(_b,_a);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertTrue(null == freq2.get("a"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertEquals(new Integer(3),freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("d"));
        assertTrue(null == freq2.get("e"));
    }

    public void testDisjunction() {
        Collection col = CollectionUtils.disjunction(_a,_b);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertEquals(new Integer(2),freq.get("b"));
        assertTrue(null == freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertEquals(new Integer(1),freq.get("e"));

        Collection col2 = CollectionUtils.disjunction(_b,_a);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("a"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertTrue(null == freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("d"));
        assertEquals(new Integer(1),freq2.get("e"));
    }

    public void testDisjunctionAsUnionMinusIntersection() {
        Collection dis = CollectionUtils.disjunction(_a,_b);
        Collection un = CollectionUtils.union(_a,_b);
        Collection inter = CollectionUtils.intersection(_a,_b);
        assertTrue(CollectionUtils.isEqualCollection(dis,CollectionUtils.subtract(un,inter)));
    }

    public void testDisjunctionAsSymmetricDifference() {
        Collection dis = CollectionUtils.disjunction(_a,_b);
        Collection amb = CollectionUtils.subtract(_a,_b);
        Collection bma = CollectionUtils.subtract(_b,_a);
        assertTrue(CollectionUtils.isEqualCollection(dis,CollectionUtils.union(amb,bma)));
    }

    public void testSubtract() {
        Collection col = CollectionUtils.subtract(_a,_b);
        Map freq = CollectionUtils.getCardinalityMap(col);
        assertEquals(new Integer(1),freq.get("a"));
        assertTrue(null == freq.get("b"));
        assertTrue(null == freq.get("c"));
        assertEquals(new Integer(2),freq.get("d"));
        assertTrue(null == freq.get("e"));

        Collection col2 = CollectionUtils.subtract(_b,_a);
        Map freq2 = CollectionUtils.getCardinalityMap(col2);
        assertEquals(new Integer(1),freq2.get("e"));
        assertTrue(null == freq2.get("d"));
        assertTrue(null == freq2.get("c"));
        assertEquals(new Integer(2),freq2.get("b"));
        assertTrue(null == freq2.get("a"));
    }

    public void testIsSubCollectionOfSelf() {
        assertTrue(CollectionUtils.isSubCollection(_a,_a));
        assertTrue(CollectionUtils.isSubCollection(_b,_b));
    }

    public void testIsSubCollection() {
        assertTrue(!CollectionUtils.isSubCollection(_a,_b));
        assertTrue(!CollectionUtils.isSubCollection(_b,_a));
    }

    public void testIsSubCollection2() {
        Collection c = new ArrayList();
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("a");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("b");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("b");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("c");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(!CollectionUtils.isSubCollection(_a,c));
        c.add("d");
        assertTrue(CollectionUtils.isSubCollection(c,_a));
        assertTrue(CollectionUtils.isSubCollection(_a,c));
        c.add("e");
        assertTrue(!CollectionUtils.isSubCollection(c,_a));
        assertTrue(CollectionUtils.isSubCollection(_a,c));
    }

    public void testIsEqualCollectionToSelf() {
        assertTrue(CollectionUtils.isEqualCollection(_a,_a));
        assertTrue(CollectionUtils.isEqualCollection(_b,_b));
    }

    public void testIsEqualCollection() {
        assertTrue(!CollectionUtils.isEqualCollection(_a,_b));
        assertTrue(!CollectionUtils.isEqualCollection(_b,_a));
    }

    public void testIsEqualCollection2() {
        Collection a = new ArrayList();
        Collection b = new ArrayList();
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("1");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("1");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("2");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("2");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
        a.add("1");
        assertTrue(!CollectionUtils.isEqualCollection(a,b));
        assertTrue(!CollectionUtils.isEqualCollection(b,a));
        b.add("1");
        assertTrue(CollectionUtils.isEqualCollection(a,b));
        assertTrue(CollectionUtils.isEqualCollection(b,a));
    }


    public void testIndex() {
        Map map = new HashMap();
        map.put(new Integer(0), "element");
        Object test = CollectionUtils.index(map, 0);
        assertTrue(test.equals("element"));

        List list = new ArrayList();
        list.add("element");
        test = CollectionUtils.index(list, 0);
        assertTrue(test.equals("element"));

        Bag bag = new HashBag();
        bag.add("element", 1);
        test = CollectionUtils.index(bag, 0);
        assertTrue(test.equals("element"));
    }
}

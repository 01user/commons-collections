/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/comparators/TestNullComparator.java,v 1.7 2003/11/18 22:37:18 scolebourne Exp $
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
package org.apache.commons.collections.comparators;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test the NullComparator.
 * 
 * @version $Revision: 1.7 $ $Date: 2003/11/18 22:37:18 $
 * 
 * @author Michael A. Smith
 */
public abstract class TestNullComparator extends AbstractTestComparator {

    public TestNullComparator(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestNullComparator.class.getName());
        suite.addTest(new TestSuite(TestNullComparator1.class));
        suite.addTest(new TestSuite(TestNullComparator2.class));
        return suite;
    }

    /**
     *  Test the NullComparator with nulls high, using comparable comparator
     **/
    public static class TestNullComparator1 extends TestNullComparator {

	public TestNullComparator1(String testName) {
	    super(testName);
	}

    public Comparator makeComparator() {
	    return new NullComparator();
	}
	
    public List getComparableObjectsOrdered() {
        List list = new LinkedList();
	    list.add(new Integer(1));
	    list.add(new Integer(2));
	    list.add(new Integer(3));
	    list.add(new Integer(4));
	    list.add(new Integer(5));
	    list.add(null);
	    return list;
	}

	public String getCanonicalComparatorName(Object object) {
	    return super.getCanonicalComparatorName(object) + "1";
	}
    }

    /**
     *  Test the NullComparator with nulls low using the comparable comparator
     **/
    public static class TestNullComparator2 extends TestNullComparator {
        
        public TestNullComparator2(String testName) {
            super(testName);
        }
        
        public Comparator makeComparator() {
            return new NullComparator(false);
        }
        
        public List getComparableObjectsOrdered() {
            List list = new LinkedList();
            list.add(null);
            list.add(new Integer(1));
            list.add(new Integer(2));
            list.add(new Integer(3));
            list.add(new Integer(4));
            list.add(new Integer(5));
            return list;
        }
        
        public String getCanonicalComparatorName(Object object) {
            return super.getCanonicalComparatorName(object) + "2";
        }
    }
}

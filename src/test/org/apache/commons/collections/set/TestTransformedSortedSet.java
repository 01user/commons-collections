/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/set/TestTransformedSortedSet.java,v 1.1 2003/11/16 00:05:46 scolebourne Exp $
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
package org.apache.commons.collections.set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;

import org.apache.commons.collections.AbstractTestSortedSet;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link TestSortedSet} for exercising the {@link TransformedSortedSet}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.1 $ $Date: 2003/11/16 00:05:46 $
 * 
 * @author Stephen Colebourne
 */
public class TestTransformedSortedSet extends AbstractTestSortedSet {
    
    public TestTransformedSortedSet(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestTransformedSortedSet.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestTransformedSortedSet.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    protected Set makeEmptySet() {
        return TransformedSortedSet.decorate(new TreeSet(), TestTransformedCollection.NOOP_TRANSFORMER);
    }

    protected Set makeFullSet() {
        SortedSet set = new TreeSet();
        set.addAll(Arrays.asList(getFullElements()));
        return TransformedSortedSet.decorate(set, TestTransformedCollection.NOOP_TRANSFORMER);
    }
    
    //-----------------------------------------------------------------------   
    public void testTransformedSet() {
        Set set = TransformedSortedSet.decorate(new HashSet(), TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, set.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            set.add(els[i]);
            assertEquals(i + 1, set.size());
            assertEquals(true, set.contains(new Integer((String) els[i])));
            assertEquals(false, set.contains(els[i]));
        }
        
        assertEquals(false, set.remove(els[0]));
        assertEquals(true, set.remove(new Integer((String) els[0])));
        
    } 
}

/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/decorators/Attic/TestObservedCollection.java,v 1.3 2003/08/31 22:44:54 scolebourne Exp $
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
package org.apache.commons.collections.decorators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.TestCollection;
import org.apache.commons.collections.event.StandardModificationHandler;

/**
 * Extension of {@link TestCollection} for exercising the
 * {@link ObservedCollection} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.3 $ $Date: 2003/08/31 22:44:54 $
 * 
 * @author Stephen Colebourne
 */
public class TestObservedCollection extends TestCollection {
    
    private static Integer SIX = new Integer(6);
    private static Integer SEVEN = new Integer(7);
    private static Integer EIGHT = new Integer(8);
    private static final ObservedTestHelper.Listener LISTENER = ObservedTestHelper.LISTENER;
    private static final ObservedTestHelper.PreListener PRE_LISTENER = ObservedTestHelper.PRE_LISTENER;
    private static final ObservedTestHelper.PostListener POST_LISTENER = ObservedTestHelper.POST_LISTENER;
    
    public TestObservedCollection(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestObservedCollection.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestObservedCollection.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    //-----------------------------------------------------------------------
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    protected Collection makeConfirmedFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }
    
    public Collection makeCollection() {
        return ObservedCollection.decorate(new ArrayList(), LISTENER);
    }

    protected Collection makeFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return ObservedCollection.decorate(list, LISTENER);
    }
    
    //-----------------------------------------------------------------------
    public void testObservedCollection() {
        ObservedCollection coll = ObservedCollection.decorate(new ArrayList());
        ObservedTestHelper.doTestFactoryPlain(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestFactoryWithListener(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), PRE_LISTENER);
        ObservedTestHelper.doTestFactoryWithPreListener(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), POST_LISTENER);
        ObservedTestHelper.doTestFactoryWithPostListener(coll);
        
        coll = ObservedCollection.decorate(new ArrayList());
        ObservedTestHelper.doTestAddRemoveGetPreListeners(coll);
        
        coll = ObservedCollection.decorate(new ArrayList());
        ObservedTestHelper.doTestAddRemoveGetPostListeners(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestAdd(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestAddAll(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestClear(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestRemove(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestRemoveAll(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestRetainAll(coll);
        
        coll = ObservedCollection.decorate(new ArrayList(), LISTENER);
        ObservedTestHelper.doTestIteratorRemove(coll);
    }

    //-----------------------------------------------------------------------    
    public void testFactoryWithHandler() {
        StandardModificationHandler handler = new StandardModificationHandler();
        ObservedCollection coll = ObservedCollection.decorate(new ArrayList(), handler);
        
        assertSame(handler, coll.getHandler());
        assertEquals(0, coll.getHandler().getPreModificationListeners().length);
        assertEquals(0, coll.getHandler().getPostModificationListeners().length);
    }
    
//    public void testFactoryWithMasks() {
//        ObservedCollection coll = ObservedCollection.decorate(new ArrayList(), LISTENER, -1, 0);
//        LISTENER.preEvent = null;
//        LISTENER.postEvent = null;
//        coll.add(SIX);
//        assertTrue(LISTENER.preEvent != null);
//        assertTrue(LISTENER.postEvent == null);
//        
//        coll = ObservedCollection.decorate(new ArrayList(), LISTENER, 0, -1);
//        LISTENER.preEvent = null;
//        LISTENER.postEvent = null;
//        coll.add(SIX);
//        assertTrue(LISTENER.preEvent == null);
//        assertTrue(LISTENER.postEvent != null);
//        
//        coll = ObservedCollection.decorate(new ArrayList(), LISTENER, -1, -1);
//        LISTENER.preEvent = null;
//        LISTENER.postEvent = null;
//        coll.add(SIX);
//        assertTrue(LISTENER.preEvent != null);
//        assertTrue(LISTENER.postEvent != null);
//        
//        coll = ObservedCollection.decorate(new ArrayList(), LISTENER, 0, 0);
//        LISTENER.preEvent = null;
//        LISTENER.postEvent = null;
//        coll.add(SIX);
//        assertTrue(LISTENER.preEvent == null);
//        assertTrue(LISTENER.postEvent == null);
//        
//        coll = ObservedCollection.decorate(new ArrayList(), LISTENER, ModificationEventType.ADD, ModificationEventType.ADD_ALL);
//        LISTENER.preEvent = null;
//        LISTENER.postEvent = null;
//        coll.add(SIX);
//        assertTrue(LISTENER.preEvent != null);
//        assertTrue(LISTENER.postEvent == null);
//    }
//    
}

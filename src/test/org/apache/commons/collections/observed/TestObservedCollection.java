/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/observed/Attic/TestObservedCollection.java,v 1.1 2003/09/03 23:54:25 scolebourne Exp $
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
package org.apache.commons.collections.observed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.TestCollection;

/**
 * Extension of {@link TestCollection} for exercising the
 * {@link ObservedCollection} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.1 $ $Date: 2003/09/03 23:54:25 $
 * 
 * @author Stephen Colebourne
 */
public class TestObservedCollection extends TestCollection implements ObservedTestHelper.ObservedFactory {
    
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
        return ObservedCollection.decorate(new ArrayList(), ObservedTestHelper.LISTENER);
    }

    protected Collection makeFullCollection() {
        List list = new ArrayList();
        list.addAll(Arrays.asList(getFullElements()));
        return ObservedCollection.decorate(list, ObservedTestHelper.LISTENER);
    }
    
    //-----------------------------------------------------------------------
    public void testObservedCollection() {
        ObservedTestHelper.bulkTestObservedCollection(this);
    }

    //-----------------------------------------------------------------------
    public ObservedCollection createObservedCollection() {
        return ObservedCollection.decorate(new ArrayList());
    }

    public ObservedCollection createObservedCollection(Object listener) {
        return ObservedCollection.decorate(new ArrayList(), listener);
    }

//  public void testFactoryWithMasks() {
//      ObservedCollection coll = ObservedCollection.decorate(new ArrayList(), LISTENER, -1, 0);
//      LISTENER.preEvent = null;
//      LISTENER.postEvent = null;
//      coll.add(SIX);
//      assertTrue(LISTENER.preEvent != null);
//      assertTrue(LISTENER.postEvent == null);
//        
//      coll = ObservedCollection.decorate(new ArrayList(), LISTENER, 0, -1);
//      LISTENER.preEvent = null;
//      LISTENER.postEvent = null;
//      coll.add(SIX);
//      assertTrue(LISTENER.preEvent == null);
//      assertTrue(LISTENER.postEvent != null);
//        
//      coll = ObservedCollection.decorate(new ArrayList(), LISTENER, -1, -1);
//      LISTENER.preEvent = null;
//      LISTENER.postEvent = null;
//      coll.add(SIX);
//      assertTrue(LISTENER.preEvent != null);
//      assertTrue(LISTENER.postEvent != null);
//        
//      coll = ObservedCollection.decorate(new ArrayList(), LISTENER, 0, 0);
//      LISTENER.preEvent = null;
//      LISTENER.postEvent = null;
//      coll.add(SIX);
//      assertTrue(LISTENER.preEvent == null);
//      assertTrue(LISTENER.postEvent == null);
//        
//      coll = ObservedCollection.decorate(new ArrayList(), LISTENER, ModificationEventType.ADD, ModificationEventType.ADD_ALL);
//      LISTENER.preEvent = null;
//      LISTENER.postEvent = null;
//      coll.add(SIX);
//      assertTrue(LISTENER.preEvent != null);
//      assertTrue(LISTENER.postEvent == null);
//  }
//    
}

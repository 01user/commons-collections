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
package org.apache.commons.collections.list;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;

/**
 * Test class for NodeCachingLinkedList, a performance optimised LinkedList.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/01/14 21:34:28 $
 * 
 * @author Jeff Varszegi
 * @author Phil Steitz
 */
public class TestNodeCachingLinkedList extends TestAbstractLinkedList {

    public TestNodeCachingLinkedList(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        compareSpeed();
        String[] testCaseName = { TestNodeCachingLinkedList.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestNodeCachingLinkedList.class);
    }

    //-----------------------------------------------------------------------    
    public List makeEmptyList() {
        return new NodeCachingLinkedList();
    }

    public String getCompatibilityVersion() {
        return "3";
    }
    
    //-----------------------------------------------------------------------
    public void testShrinkCache() {
        if (isRemoveSupported() == false || isAddSupported() == false) return;
        resetEmpty();
        NodeCachingLinkedList list = (NodeCachingLinkedList) collection;
        
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        list.removeAllNodes();        // Will dump all 4 elements into cache
        ((NodeCachingLinkedList) list).setMaximumCacheSize(2); // shrink cache
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        checkNodes();
        list.removeNode(list.getNode(0, false)); // no room in cache
        list.removeNode(list.getNode(0, false)); 
        list.removeNode(list.getNode(0, false)); 
        checkNodes();    
        list.addAll( Arrays.asList( new String[]{"1", "2", "3", "4"}));
        checkNodes();     
    }       
    
    //-----------------------------------------------------------------------
    public static void compareSpeed() {
        NodeCachingLinkedList ncll = new NodeCachingLinkedList();
        LinkedList ll = new LinkedList();
        
        Object o1 = new Object();
        Object o2 = new Object();
        
        int loopCount = 4000000;
        
        long startTime, endTime;
        
        System.out.println("Testing relative execution time of commonly-used methods...");
        
        startTime = System.currentTimeMillis();   
        for(int x = loopCount; x > 0; x--) {
            // unrolled a few times to minimize effect of loop
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
        }
        endTime = System.currentTimeMillis();   
        System.out.println("Time with LinkedList: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();   
        for(int x = loopCount; x > 0; x--) {
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
        }
        endTime = System.currentTimeMillis();   
        System.out.println("Time with NodeCachingLinkedList: " + (endTime - startTime) + " ms");

    }
    
//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection,
//           "D:/dev/collections/data/test/NodeCachingLinkedList.emptyCollection.version3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection,
//            "D:/dev/collections/data/test/NodeCachingLinkedList.fullCollection.version3.obj");
//    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.bag;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.collection.TestTransformedCollection;

/**
 * Extension of {@link AbstractBagTest} for exercising the {@link TransformedBag}
 * implementation.
 *
 * @since 3.0
 * @version $Id$
 */
public class TransformedBagTest<T> extends AbstractBagTest<T> {

    public TransformedBagTest(String testName) {
        super(testName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(new HashBag<T>(),
                (Transformer<T, T>) TestTransformedCollection.NOOP_TRANSFORMER);
    }

    @SuppressWarnings("unchecked")
    public void testTransformedBag() {
        //T had better be Object!
        Bag<T> bag = TransformedBag.transformingBag(new HashBag<T>(),
                (Transformer<T, T>) TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, bag.size());
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            bag.add((T) els[i]);
            assertEquals(i + 1, bag.size());
            assertEquals(true, bag.contains(new Integer((String) els[i])));
            assertEquals(false, bag.contains(els[i]));
        }

        assertEquals(false, bag.remove(els[0]));
        assertEquals(true, bag.remove(new Integer((String) els[0])));
    }

    @SuppressWarnings("unchecked")
    public void testTransformedBag_decorateTransform() {
        Bag<T> originalBag = new HashBag<T>();
        Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (int i = 0; i < els.length; i++) {
            originalBag.add((T) els[i]);
        }
        Bag<T> bag = TransformedBag.transformedBag(originalBag,
                (Transformer<T, T>) TestTransformedCollection.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(els.length, bag.size());
        for (int i = 0; i < els.length; i++) {
            assertEquals(true, bag.contains(new Integer((String) els[i])));
            assertEquals(false, bag.contains(els[i]));
        }
        
        assertEquals(false, bag.remove(els[0]));
        assertEquals(true, bag.remove(new Integer((String) els[0])));
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/TransformedBag.emptyCollection.version3.1.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/TransformedBag.fullCollection.version3.1.obj");
//    }

}

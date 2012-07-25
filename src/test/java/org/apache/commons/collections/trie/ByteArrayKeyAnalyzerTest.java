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
package org.apache.commons.collections.trie;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

public class ByteArrayKeyAnalyzerTest {

    private static final int SIZE = 20000;
    
    @Test
    public void bitSet() {
        byte[] key = toByteArray("10100110", 2);
        ByteArrayKeyAnalyzer ka = new ByteArrayKeyAnalyzer(key.length * 8);
        int length = ka.lengthInBits(key);
        
        Assert.assertTrue(ka.isBitSet(key, 0, length));
        Assert.assertFalse(ka.isBitSet(key, 1, length));
        Assert.assertTrue(ka.isBitSet(key, 2, length));
        Assert.assertFalse(ka.isBitSet(key, 3, length));
        Assert.assertFalse(ka.isBitSet(key, 4, length));
        Assert.assertTrue(ka.isBitSet(key, 5, length));
        Assert.assertTrue(ka.isBitSet(key, 6, length));
        Assert.assertFalse(ka.isBitSet(key, 7, length));
    }
    
    @Test
    public void keys() {
        PatriciaTrie<byte[], BigInteger> trie
            = new PatriciaTrie<byte[], BigInteger>(ByteArrayKeyAnalyzer.INSTANCE);
        
        Map<byte[], BigInteger> map 
            = new TreeMap<byte[], BigInteger>(ByteArrayKeyAnalyzer.INSTANCE);
        
        for (int i = 0; i < SIZE; i++) {
            BigInteger value = BigInteger.valueOf(i);
            byte[] key = toByteArray(value);
            
            BigInteger existing = trie.put(key, value);
            Assert.assertNull(existing);
            
            map.put(key, value);
        }
        
        Assert.assertEquals(map.size(), trie.size());
        
        for (byte[] key : map.keySet()) {
            BigInteger expected = new BigInteger(1, key);
            BigInteger value = trie.get(key);
            
            Assert.assertEquals(expected, value);
        }
    }
    
    @Test
    public void prefix() {
        byte[] prefix   = toByteArray("00001010", 2);
        byte[] key1     = toByteArray("11001010", 2);
        byte[] key2     = toByteArray("10101100", 2);
        
        ByteArrayKeyAnalyzer keyAnalyzer = new ByteArrayKeyAnalyzer(key1.length * 8);
        
        int prefixLength = keyAnalyzer.lengthInBits(prefix);
            
        Assert.assertFalse(keyAnalyzer.isPrefix(prefix, 4, prefixLength, key1));
        Assert.assertTrue(keyAnalyzer.isPrefix(prefix, 4, prefixLength, key2));
    }
    
    private static byte[] toByteArray(String value, int radix) {
        return toByteArray(Long.parseLong(value, radix));
    }
    
    private static byte[] toByteArray(long value) {
        return toByteArray(BigInteger.valueOf(value));
    }
    
    private static byte[] toByteArray(BigInteger value) {
        byte[] src = value.toByteArray();
        if (src.length <= 1) {
            return src;
        }
        
        if (src[0] != 0) {
            return src;
        }
        
        byte[] dst = new byte[src.length-1];
        System.arraycopy(src, 1, dst, 0, dst.length);
        return dst;
    }
}

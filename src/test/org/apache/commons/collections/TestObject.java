/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/test/org/apache/commons/collections/Attic/TestObject.java,v 1.19 2003/02/26 00:35:19 rwaldhoff Exp $
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Tests base {@link java.lang.Object} methods and contracts.
 * <p>
 * To use, simply extend this class, and implement
 * the {@link #makeObject()} method.
 * <p>
 * If your {@link Object} fails one of these tests by design,
 * you may still use this base set of cases.  Simply override the
 * test case (method) your {@link Object} fails.
 *
 * @author Rodney Waldhoff
 * @author Anonymous
 * 
 * @version $Revision: 1.19 $ $Date: 2003/02/26 00:35:19 $
 */
public abstract class TestObject extends BulkTest {

    // constructor
    // ------------------------------------------------------------------------
    
    public TestObject(String testName) {
        super(testName);
    }

    // abstract
    // ------------------------------------------------------------------------
    
    /**
     * Return a new, empty {@link Object} to used for testing.
     */
    protected abstract Object makeObject();

    // tests
    // ------------------------------------------------------------------------
    
    public void testObjectEqualsSelf() {
        Object obj = makeObject();
        assertEquals("A Object should equal itself",obj,obj);
    }

    public void testEqualsNull() {
        Object obj = makeObject();
        assertTrue(! obj.equals(null) ); // make sure this doesn't throw NPE either
    }

    public void testObjectHashCodeEqualsSelfHashCode() {
        Object obj = makeObject();
        assertEquals("hashCode should be repeatable",obj.hashCode(),obj.hashCode());
    }

    public void testObjectHashCodeEqualsContract() {
        Object obj1 = makeObject();
        if(obj1.equals(obj1)) {
            assertEquals("[1] When two objects are equal, their hashCodes should be also.",obj1.hashCode(),obj1.hashCode());
        }
        Object obj2 = makeObject();
        if(obj1.equals(obj2)) {
            assertEquals("[2] When two objects are equal, their hashCodes should be also.",obj1.hashCode(),obj2.hashCode());
            assertTrue("When obj1.equals(obj2) is true, then obj2.equals(obj1) should also be true", obj2.equals(obj1));
        }
    }

    public void testSerializeDeserializeThenCompare() throws Exception {
        Object obj = makeObject();
        if(obj instanceof Serializable) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(obj);
            out.close();
            
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            Object dest = in.readObject();
            in.close();
            assertEquals("obj != deserialize(serialize(obj))",obj,dest);
        }
    }

    /**
     * Sanity check method, makes sure that any Serializable
     * class can be serialized and de-serialized in memory, 
     * using the handy makeObject() method
     * 
     * @exception IOException
     * @exception ClassNotFoundException
     */
    public void testSimpleSerialization() throws IOException, ClassNotFoundException {
        Object o = makeObject();
        if (o instanceof Serializable) {
            byte[] objekt = writeExternalFormToBytes((Serializable) o);
            Object p = readExternalFormFromBytes(objekt);
        }
    }

    /**
     * If the test object is serializable, confirm that 
     * a canonical form exists in CVS
     * 
     */
    public void testCanonicalEmptyCollectionExists() {
        if(supportsEmptyCollections()) {    
            Object object = makeObject();
            if(object instanceof Serializable) {
                String name = getCanonicalEmptyCollectionName(object);
                assertTrue("Canonical empty collection (" + name + ") is not in CVS",
                           new File(name).exists());
            }
        }
    }

    /**
     * If the test object is serializable, confirm that 
     * a canonical form exists in CVS
     * 
     */
    public void testCanonicalFullCollectionExists() {
        if(supportsFullCollections()) {
            Object object = makeObject();
            if(object instanceof Serializable) {
                String name = getCanonicalFullCollectionName(object);
                assertTrue("Canonical full collection (" + name + ") is not in CVS",
                           new File(name).exists());
            }
        }
    }

    // protected
    // ------------------------------------------------------------------------
    
    /**
     * Get the version of Collections that this object tries to
     * maintain serialization compatibility with. Defaults to 1, the
     * earliest Collections version. (Note: some collections did not
     * even exist in this version).
     * 
     * This constant makes it possible for TestMap (and other subclasses,
     * if necessary) to automatically check CVS for a versionX copy of a
     * Serialized object, so we can make sure that compatibility is maintained.
     * See, for example, TestMap.getCanonicalFullMapName(Map map).
     * Subclasses can override this variable, indicating compatibility
     * with earlier Collections versions.
     * 
     * @return The version, or <code>null</code> if this object shouldn't be
     * tested for compatibility with previous versions.
     */
    protected String getCompatibilityVersion() {
        return "1";
    }

    /**
     * Override this method if a subclass is testing a 
     * Collections that cannot serialize an "empty" Collection
     * (e.g. Comparators have no contents)
     * 
     * @return true
     */
    protected boolean supportsEmptyCollections() {
        return true;
    }

    /**
     * Override this method if a subclass is testing a 
     * Collections that cannot serialize a "full" Collection
     * (e.g. Comparators have no contents)
     * 
     * @return true
     */
    protected boolean supportsFullCollections() {
        return true;
    }

    protected String getCanonicalEmptyCollectionName(Object object) {
        StringBuffer retval = new StringBuffer();
        retval.append("data/test/");
        String colName = object.getClass().getName();
        colName = colName.substring(colName.lastIndexOf(".")+1,colName.length());
        retval.append(colName);
        retval.append(".emptyCollection.version");
        retval.append(getCompatibilityVersion());
        retval.append(".obj");
        return retval.toString();
    }

    protected String getCanonicalFullCollectionName(Object object) {
        StringBuffer retval = new StringBuffer();
        retval.append("data/test/");
        String colName = object.getClass().getName();
        colName = colName.substring(colName.lastIndexOf(".")+1,colName.length());
        retval.append(colName);
        retval.append(".fullCollection.version");
        retval.append(getCompatibilityVersion());
        retval.append(".obj");
        return retval.toString();
    }

    /**
     * Write a Serializable or Externalizable object as
     * a file at the given path.  NOT USEFUL as part
     * of a unit test; this is just a utility method
     * for creating disk-based objects in CVS that can become
     * the basis for compatibility tests using
     * readExternalFormFromDisk(String path)
     * 
     * @param o Object to serialize
     * @param path path to write the serialized Object
     * @exception IOException
     */
    protected void writeExternalFormToDisk(Serializable o, String path) 
    throws IOException {
        FileOutputStream fileStream = new FileOutputStream(path);
        writeExternalFormToStream(o,fileStream);
    }

    /**
     * Converts a Serializable or Externalizable object to
     * bytes.  Useful for in-memory tests of serialization
     * 
     * @param o Object to convert to bytes
     * @return serialized form of the Object
     * @exception IOException
     */
    protected byte[] writeExternalFormToBytes(Serializable o) 
    throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        writeExternalFormToStream(o,byteStream);
        return byteStream.toByteArray();
    }

    private Object readExternalFormFromStream(InputStream stream) 
    throws IOException, ClassNotFoundException {
        ObjectInputStream oStream = new ObjectInputStream(stream);
        return oStream.readObject();
    }

    /**
     * Reads a Serialized or Externalized Object from disk.
     * Useful for creating compatibility tests betweeen
     * different CVS versions of the same class
     * 
     * @param path path to the serialized Object
     * @return the Object at the given path
     * @exception IOException
     * @exception ClassNotFoundException
     */
    protected Object readExternalFormFromDisk(String path) 
    throws IOException, ClassNotFoundException {
        FileInputStream stream = new FileInputStream(path);
        return readExternalFormFromStream(stream);
    }

    /**
     * Read a Serialized or Externalized Object from bytes.
     * Useful for verifying serialization in memory.
     * 
     * @param b byte array containing a serialized Object
     * @return Object contained in the bytes
     * @exception IOException
     * @exception ClassNotFoundException
     */
    protected Object readExternalFormFromBytes(byte[] b) 
    throws IOException, ClassNotFoundException {
        ByteArrayInputStream stream = new ByteArrayInputStream(b);
        return readExternalFormFromStream(stream);
    }

    // private
    // ------------------------------------------------------------------------

    private void writeExternalFormToStream(Serializable o, OutputStream stream) 
    throws IOException {
        ObjectOutputStream oStream = new ObjectOutputStream(stream);
        oStream.writeObject(o);
    }

    // attributes
    // ------------------------------------------------------------------------

    // current major release for Collections
    public static final int COLLECTIONS_MAJOR_VERSION = 2;

}

/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/event/Attic/ModificationEventType.java,v 1.1 2003/08/28 18:31:13 scolebourne Exp $
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
 *    any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
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
package org.apache.commons.collections.event;

/**
 * Defines event constants for event handling and matching.
 * <p>
 * The constants in this class are of two types:
 * <ol>
 * <li>Methods - the base definitions (unique bits)
 * <li>Groups - combination definitions (method bits combined)
 * </ol>
 * <p>
 * Only a method constant may be compared using == to an event type.
 * This can include use in a switch statement
 * <p>
 * Any constant may be used for filtering.
 * They may combined using the bitwise OR, <code>|</code>.
 * They may negated using the bitwise NOT, <code>~</code>.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.1 $ $Date: 2003/08/28 18:31:13 $
 * 
 * @author Stephen Colebourne
 */
public class ModificationEventType {
    
    /** The method add(Object) */
    public static final int ADD =           0x00000001;
    /** The method add(int,Object) */
    public static final int ADD_INDEXED =   0x00000002;
    /** The method addAll(Collection) */
    public static final int ADD_ALL =       0x00000004;
    /** The method addAll(int,Collection) */
    public static final int ADD_ALL_INDEXED=0x00000008;
    /** The method remove(Object) */
    public static final int REMOVE =        0x00000010;
    /** The method remove(int) */
    public static final int REMOVE_INDEXED =0x00000020;
    /** The method removeAll(Collection) */
    public static final int REMOVE_ALL =    0x00000040;
    /** The method retainAll(Collection) */
    public static final int RETAIN_ALL =    0x00000080;
    /** The method clear() */
    public static final int CLEAR =         0x00000100;
    /** The method set(int,Object) */
    public static final int SET_INDEXED =   0x00000200;

    /** All add methods */
    public static final int GROUP_ADD = ADD | ADD_INDEXED | ADD_ALL | ADD_ALL_INDEXED;
    /** All methods that change without structure modification */
    public static final int GROUP_CHANGE = SET_INDEXED;
    /** All remove methods */
    public static final int GROUP_REMOVE = REMOVE | REMOVE_INDEXED | REMOVE_ALL;
    /** All retain methods */
    public static final int GROUP_RETAIN = RETAIN_ALL;
    /** All clear methods */
    public static final int GROUP_CLEAR = CLEAR;
    /** All reducing methods (remove, retain and clear) */
    public static final int GROUP_REDUCE = GROUP_REMOVE | GROUP_CLEAR | GROUP_RETAIN;
    
    /** All indexed methods */
    public static final int GROUP_INDEXED = ADD_INDEXED | ADD_ALL_INDEXED | REMOVE_INDEXED | SET_INDEXED;
    /** All non indexed methods */
    public static final int GROUP_NON_INDEXED = ADD | ADD_ALL | REMOVE | REMOVE_ALL | RETAIN_ALL | CLEAR;
    /** All bulk methods (xxxAll and clear) */
    public static final int GROUP_BULK = ADD_ALL | ADD_ALL_INDEXED | REMOVE_ALL | RETAIN_ALL | CLEAR;
    /** All non bulk methods */
    public static final int GROUP_NON_BULK = ADD | ADD_INDEXED | REMOVE | REMOVE_INDEXED | SET_INDEXED;
    /** All methods that modify the structure */
    public static final int GROUP_STRUCTURE_MODIFIED = 
        ADD | ADD_INDEXED | ADD_ALL | ADD_ALL_INDEXED | REMOVE | REMOVE_INDEXED | REMOVE_ALL | RETAIN_ALL | CLEAR;
    /** All non structure modifying methods */
    public static final int GROUP_NON_STRUCTURE_MODIFIED = SET_INDEXED;

    /** All methods sent by a Collection */
    public static final int GROUP_FROM_COLLECTION = ADD | ADD_ALL | REMOVE | REMOVE_ALL | RETAIN_ALL | CLEAR;
    /** All methods sent by a Set */
    public static final int GROUP_FROM_SET = GROUP_FROM_COLLECTION;
    /** All methods sent by a List */
    public static final int GROUP_FROM_LIST = GROUP_FROM_COLLECTION | ADD_INDEXED | ADD_ALL_INDEXED | REMOVE_INDEXED | SET_INDEXED;

    /** No methods */
    public static final int GROUP_NONE = 0x00000000;
    /** All methods */
    public static final int GROUP_ALL = 0xFFFFFFFF;

    /**
     * Constructor.
     */
    protected ModificationEventType() {
        super();
    }
    
    /**
     * Gets a string version of a method event type.
     * 
     * @param methodType  the method event type constant
     * @return a string description
     */
    public static String toString(final int methodType) {
        switch (methodType) {
            case ADD:
            return "Add";
            case ADD_INDEXED:
            return "AddIndexed";
            case ADD_ALL:
            return "AddAll";
            case ADD_ALL_INDEXED:
            return "AddAllIndexed";
            case REMOVE:
            return "Remove";
            case REMOVE_INDEXED:
            return "RemoveIndexed";
            case REMOVE_ALL:
            return "RemoveAll";
            case RETAIN_ALL:
            return "RetainAll";
            case CLEAR:
            return "Clear";
            case SET_INDEXED:
            return "SetIndexed";
            default:
            return "Unknown";
        }
    }

}

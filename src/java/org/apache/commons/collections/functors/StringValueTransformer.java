/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.functors;

import java.io.Serializable;

import org.apache.commons.collections.Transformer;

/**
 * Transformer implementation that returns the <code>String.valueOf</code>.
 * 
 * @since Commons Collections 3.0
 * @version $Revision: 1.4 $ $Date: 2004/02/18 00:59:20 $
 *
 * @author Stephen Colebourne
 */
public final class StringValueTransformer implements Transformer, Serializable {

    /** Serial version UID */
    static final long serialVersionUID = 7511110693171758606L;

    /** Singleton predicate instance */    
    public static final Transformer INSTANCE = new StringValueTransformer();
    
    /**
     * Restricted constructor.
     */
    private StringValueTransformer() {
        super();
    }

    /**
     * Return the String.valueOf for the object.
     */
    public Object transform(Object input) {
        return String.valueOf(input);
    }
    
}

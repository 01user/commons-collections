/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//collections/src/java/org/apache/commons/collections/MapUtils.java,v 1.20 2003/04/07 16:57:33 rwaldhoff Exp $
 * $Revision: 1.20 $
 * $Date: 2003/04/07 16:57:33 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
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

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/** 
 * A helper class for using {@link Map Map} instances.<P>
 *
 * It contains various typesafe methods
 * as well as other useful features like deep copying.<P>
 *
 * It also provides the following decorators:
 *
 *  <ul>
 *  <li>{@link #fixedSizeMap(Map)}
 *  <li>{@link #fixedSizeSortedMap(SortedMap)}
 *  <li>{@link #lazyMap(Map,Factory)}
 *  <li>{@link #typedMap(Map, Class, Class)}
 *  <li>{@link #lazySortedMap(SortedMap,Factory)}
 *  <li>{@link #predicatedMap(Map,Predicate,Predicate)}
 *  <li>{@link #predicatedSortedMap(SortedMap,Predicate,Predicate)}
 *  <li>{@link #typedSortedMap(Map, Class, Class)}
 *  </ul>
 *
 * @since 1.0
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @author <a href="mailto:knielsen@apache.org">Kasper Nielsen</a>
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Matthew Hawthorne
 */
public class MapUtils {
    
    /**
     * An empty unmodifiable map.
     * This was not provided in JDK1.2.
     */
    public static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(1));
    /**
     * An empty unmodifiable sorted map.
     * This is not provided in the JDK.
     */
    public static final SortedMap EMPTY_SORTED_MAP = Collections.unmodifiableSortedMap(new TreeMap());

    private static int debugIndent = 0;

    /**
     * <code>MapUtils</code> should not normally be instantiated.
     */
    public MapUtils() {
    }    
    
    // Type safe getters
    //-------------------------------------------------------------------------

    /**
     *  Synonym for {@link Map#get(Object)}.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return null if the map is null; or the result of 
     *     <Code>map.get(key)</Code>
     */
    public static Object getObject( Map map, Object key ) {
        if ( map != null ) {
            return map.get( key );
        }
        return null;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a string.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  null if the map is null; null if the value mapped by that
     *    key is null; or the <Code>toString()</Code> 
     *     result of the value for that key
     */
    public static String getString( Map map, Object key ) {
        if ( map != null ) {
            Object answer = map.get( key );
            if ( answer != null ) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Boolean}.  If the map is null, this method returns null.
     *  If the value mapped by the given key is a 
     *  {@link Boolean}, then it is returned as-is.  Otherwise, if the value
     *  is a string, then if that string ignoring case equals "true", then
     *  a true {@link Boolean} is returned.  Any other string value will
     *  result in a false {@link Boolean} being returned.  OR, if the value
     *  is a {@link Number}, and that {@link Number} is 0, then a false
     *  {@link Boolean} is returned.  Any other {@link Number} value results
     *  in a true {@link Boolean} being returned.<P>
     *
     *  Any value that is not a {@link Boolean}, {@link String} or 
     *  {@link Number} results in null being returned.<P>
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Boolean} or null
     */
    public static Boolean getBoolean( Map map, Object key ) {
        if ( map != null ) {
            Object answer = map.get( key );
            if ( answer != null ) {
                if ( answer instanceof Boolean ) {
                    return (Boolean) answer;
                }
                else
                if ( answer instanceof String ) {
                    return new Boolean( (String) answer );
                }
                else
                if ( answer instanceof Number ) {
                    Number n = (Number) answer;
                    return ( n.intValue() != 0 ) ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Number}.  If the map is null, this method returns null.
     *  Otherwise, if the key maps to a {@link Number}, then that number
     *  is returned as-is.  Otherwise, if the key maps to a {@link String},
     *  that string is parsed into a number using the system default
     *  {@link NumberFormat}.<P>
     *
     *  If the value is not a {@link Number} or a {@link String}, or if
     *  the value is a {@link String} that cannot be parsed into a 
     *  {@link Number}, then null is returned.<P>
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Number} or null
     */
    public static Number getNumber( Map map, Object key ) {
        if ( map != null ) {
            Object answer = map.get( key );
            if ( answer != null ) {
                if ( answer instanceof Number ) {
                    return (Number) answer;
                }
                else
                if ( answer instanceof String ) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse( text );
                    }
                    catch (ParseException e) {
                        logInfo( e );
                    }
                }
            }
        }
        return null;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Byte}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  byte value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Byte} or null
     */
    public static Byte getByte( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Byte ) {
            return (Byte) answer;
        }
        return new Byte( answer.byteValue() );
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Short}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  short value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Short} or null
     */
    public static Short getShort( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Short ) {
            return (Short) answer;
        }
        return new Short( answer.shortValue() );
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  an {@link Integer}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  integer value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  an {@link Integer} or null
     */
    public static Integer getInteger( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Integer ) {
            return (Integer) answer;
        }
        return new Integer( answer.intValue() );
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Long}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  long value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Long} or null
     */
    public static Long getLong( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Long ) {
            return (Long) answer;
        }
        return new Long( answer.longValue() );
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Float}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  float value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Float} or null
     */
    public static Float getFloat( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Float ) {
            return (Float) answer;
        }
        return new Float( answer.floatValue() );
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a {@link Double}.  First, {@link #getNumber(Map,Object)} is invoked.
     *  If the result is null, then null is returned.  Otherwise, the 
     *  double value of the resulting {@link Number} is returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Double} or null
     */
    public static Double getDouble( Map map, Object key ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            return null;
        }
        else
        if ( answer instanceof Double ) {
            return (Double) answer;
        }
        return new Double( answer.doubleValue() );
    }

    /**
     *  Looks up the given key in the given map, returning another map.
     *  If the given map is null or if the given key doesn't map to another
     *  map, then this method returns null.  Otherwise the mapped map is
     *  returned.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key whose value to look up in that map
     *  @return  a {@link Map} or null
     */
    public static Map getMap( Map map, Object key ) {
        if ( map != null ) {
            Object answer = map.get( key );
            if ( answer != null && answer instanceof Map ) {
                return (Map) answer;
            }
        }
        return null;
    }

    // Type safe getters with default values
    //-------------------------------------------------------------------------

    /**
     *  Looks up the given key in the given map, converting null into the
     *  given default value.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null
     *  @return  the value in the map, or defaultValue if the original value
     *    is null or the map is null
     */
    public static Object getObject( Map map, Object key, Object defaultValue ) {
        if ( map != null ) {
            Object answer = map.get( key );
            if ( answer != null ) {
                return answer;
            }
        }
        return defaultValue;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a string, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a string, or defaultValue if the 
     *    original value is null, the map is null or the string conversion
     *    fails
     */
    public static String getString( Map map, Object key, String defaultValue ) {
        String answer = getString( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a boolean, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a boolean, or defaultValue if the 
     *    original value is null, the map is null or the boolean conversion
     *    fails
     */
    public static Boolean getBoolean( Map map, Object key, Boolean defaultValue ) {
        Boolean answer = getBoolean( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a number, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Number getNumber( Map map, Object key, Number defaultValue ) {
        Number answer = getNumber( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a byte, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Byte getByte( Map map, Object key, Byte defaultValue ) {
        Byte answer = getByte( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a short, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Short getShort( Map map, Object key, Short defaultValue ) {
        Short answer = getShort( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  an integer, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Integer getInteger( Map map, Object key, Integer defaultValue ) {
        Integer answer = getInteger( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a long, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Long getLong( Map map, Object key, Long defaultValue ) {
        Long answer = getLong( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a float, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Float getFloat( Map map, Object key, Float defaultValue ) {
        Float answer = getFloat( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a double, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static Double getDouble( Map map, Object key, Double defaultValue ) {
        Double answer = getDouble( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a map, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the 
     *    original value is null, the map is null or the map conversion
     *    fails
     */
    public static Map getMap( Map map, Object key, Map defaultValue ) {
        Map answer = getMap( map, key );
        if ( answer == null ) {
            answer = defaultValue;
        }
        return answer;
    }

    // Conversion methods
    //-------------------------------------------------------------------------

    /**
     * Gets a new Properties object initialised with the values from a Map.
     * A null input will return an empty properties object.
     * 
     * @param map  the map to convert to a Properties object
     * @return the properties object
     */
    public static Properties toProperties(Map map) {
        Properties answer = new Properties();
        if (map != null) {
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                answer.put(key, value);
            }
        }
        return answer;
    }

    // Printing methods
    //-------------------------------------------------------------------------

    /**
     *  Prints the given map with nice line breaks.
     *
     *  @param out  the stream to print to
     *  @param key  the key that maps to the map in some other map
     *  @param map  the map to print
     */
    public static synchronized void verbosePrint( PrintStream out, Object key, Map map ) {
        debugPrintIndent( out );
        out.println( key + " = " );

        debugPrintIndent( out );
        out.println( "{" );
        ++debugIndent;

        for ( Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String childKey = (String) entry.getKey();
            Object childValue = entry.getValue();
            if ( childValue instanceof Map ) {
                verbosePrint( out, childKey, (Map) childValue );
            }
            else {
                debugPrintIndent( out );
                out.println( childKey + " = " + childValue);
            }
        }
        --debugIndent;
        debugPrintIndent( out );
        out.println( "}" );
    }

    /**
     *  Prints the given map with nice line breaks.
     *
     *  @param out  the stream to print to
     *  @param key  the key that maps to the map in some other map
     *  @param map  the map to print
     */
    public static synchronized void debugPrint( PrintStream out, Object key, Map map ) {
        debugPrintIndent( out );
        out.println( key + " = " );

        debugPrintIndent( out );
        out.println( "{" );
        ++debugIndent;

        for ( Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String childKey = (String) entry.getKey();
            Object childValue = entry.getValue();
            if ( childValue instanceof Map ) {
                verbosePrint( out, childKey, (Map) childValue );
            }
            else {
                debugPrintIndent( out );

                String typeName = ( childValue != null )
                    ? childValue.getClass().getName()
                    : null;

                out.println( childKey + " = " + childValue + " class: " + typeName );
            }
        }
        --debugIndent;
        debugPrintIndent( out );
        out.println( "}" );
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     *  Writes indentation to the given stream.
     *
     *  @param out   the stream to indent
     */
    protected static void debugPrintIndent( PrintStream out ) {
        for ( int i = 0; i < debugIndent; i++ ) {
            out.print( "    " );
        }
    }
    
    /**
     *  Logs the given exception to <Code>System.out</Code>.
     *
     *  @param e  the exception to log
     */
    protected static void logInfo(Exception e) {
        // mapX: should probably use log4j here instead...
        System.out.println( "INFO: Exception: " + e );
    }


    /**
     * Nice method for adding data to a map in such a way
     * as to not get NPE's. The point being that if the
     * value is null, map.put() will throw an exception.
     * That blows in the case of this class cause you may want to
     * essentially treat put("Not Null", null ) == put("Not Null", "")
     * We will still throw a NPE if the key is null cause that should
     * never happen.
     */
    public static final void safeAddToMap(Map map, Object key, Object value)
        throws NullPointerException
    {
        if (value == null)
        {
            map.put ( key, "" );
        }
        else
        {
           map.put ( key, value );
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Implementation of a map that checks (predicates) additions.
     */
    static class PredicatedMap 
            extends ProxyMap {

        protected final Predicate keyPredicate;
        protected final Predicate valuePredicate;

        public PredicatedMap(Map map, Predicate keyPred, Predicate valuePred) {
            super(map);
            if (map == null) {
                throw new IllegalArgumentException("Map must not be null");
            }
            if (keyPred == null) {
                throw new IllegalArgumentException("Key Predicate must not be null");
            }
            if (valuePred == null) {
                throw new IllegalArgumentException("Value Predicate must not be null");
            }
            this.keyPredicate = keyPred;
            this.valuePredicate = valuePred;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                validate(key, value);
            }
        }

        public Object put(Object key, Object value) {
            validate(key, value);
            return map.put(key, value);
        }

        public void putAll(Map m) {
            Iterator iter = m.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                validate(key, value);
            }
            map.putAll(m);
        }

        public Set entrySet() {
            return new PredicatedMapEntrySet(map.entrySet(), valuePredicate);
        }


        private void validate(Object key, Object value) {
            if (!keyPredicate.evaluate(key)) {
                throw new IllegalArgumentException("Cannot add key - Predicate rejected it");
            }
            if (!valuePredicate.evaluate(value)) {
                throw new IllegalArgumentException("Cannot add value - Predicate rejected it");
            }
        }
    }

    /**
     * Implementation of an entry set that checks (predicates) additions.
     */
    static class PredicatedMapEntrySet 
            extends CollectionUtils.CollectionWrapper
            implements Set {

        private final Predicate predicate;

        public PredicatedMapEntrySet(Set set, Predicate p) {
            super(set);
            this.predicate = p;
        }

        public Iterator iterator() {
            final Iterator iterator = collection.iterator();
            return new Iterator() {
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Object next() {
                    Map.Entry entry = (Map.Entry)iterator.next();
                    return new PredicatedMapEntry(entry, predicate);
                }

                public void remove() {
                    iterator.remove();
                }
            };
        }
    }

    /**
     * Implementation of a map entry that checks (predicates) additions.
     */
    static class PredicatedMapEntry 
            implements Map.Entry {

        private final Map.Entry entry;
        private final Predicate predicate;


        public PredicatedMapEntry(Map.Entry entry, Predicate p) {
            if (entry == null) {
                throw new IllegalArgumentException("Map.Entry must not be null");
            }
            if (p == null) {
                throw new IllegalArgumentException("Predicate must not be null");
            }
            this.entry = entry;
            this.predicate = p;
        }

        public boolean equals(Object o) {
            return entry.equals(o);
        }

        public int hashCode() {
            return entry.hashCode();
        }

        public String toString() {
            return entry.toString();
        }

        public Object getKey() {
            return entry.getKey();
        }

        public Object getValue() {
            return entry.getValue();
        }

        public Object setValue(Object o) {
            if (!predicate.evaluate(o)) {
                throw new IllegalArgumentException("Cannot set value - Predicate rejected it");
            }
            return entry.setValue(o);
        }
    }

    /**
     * Implementation of a map that is fixed in size.
     */
    static class FixedSizeMap 
            extends ProxyMap {

        public FixedSizeMap(Map map) {
            super(map);
            if (map == null) {
                throw new IllegalArgumentException("Map must not be null");
            }
        }


        public Object put(Object key, Object value) {
            if (!map.containsKey(key)) {
                throw new IllegalArgumentException("Cannot put new key/value pair - List is fixed size");
            }
            return map.put(key, value);
        }


        public void putAll(Map m) {
            for (Iterator iter = m.keySet().iterator(); iter.hasNext(); ) {
                if (!map.containsKey(iter.next())) {
                    throw new IllegalArgumentException("Cannot put new key/value pair - List is fixed size");
                }
            }
            map.putAll(m);
        }

    }

    /**
     * Implementation of a map that creates objects on demand.
     */
    static class LazyMap 
            extends ProxyMap {

        protected final Factory factory;

        public LazyMap(Map map, Factory factory) {
            super(map);
            if (map == null) {
                throw new IllegalArgumentException("Map must not be null");
            }
            if (factory == null) {
                throw new IllegalArgumentException("Factory must not be null");
            }
            this.factory = factory;
        }


        public Object get(Object key) {
            if (!map.containsKey(key)) {
                Object value = factory.create();
                map.put(key, value);
                return value;
            }
            return map.get(key);
        }

    }

    /**
     * Implementation of a map that creates objects on demand.
     */
    static class LazyTransformerMap 
            extends ProxyMap {

        protected final Transformer transformer;

        public LazyTransformerMap(Map map, Transformer transformer) {
            super(map);
            if (map == null) {
                throw new IllegalArgumentException("Map must not be null");
            }
            if (transformer == null) {
                throw new IllegalArgumentException("Transformer must not be null");
            }
            this.transformer = transformer;
        }


        public Object get(Object key) {
            if (!map.containsKey(key)) {
                Object value = transformer.transform(key);
                map.put(key, value);
                return value;
            }
            return map.get(key);
        }

    }

    /**
     * Implementation of a sorted map that checks additions.
     */
    static class PredicatedSortedMap 
            extends PredicatedMap 
            implements SortedMap {

        public PredicatedSortedMap(SortedMap map, Predicate k, Predicate v) {
            super(map, k, v);
        }

        public Object firstKey() {
            return getSortedMap().firstKey();
        }

        public Object lastKey() {
            return getSortedMap().lastKey();
        }

        public Comparator comparator() {
            return getSortedMap().comparator();
        }

        public SortedMap subMap(Object o1, Object o2) {
            SortedMap sub = getSortedMap().subMap(o1, o2);
            return new PredicatedSortedMap(sub, keyPredicate, valuePredicate);
        }

        public SortedMap headMap(Object o1) {
            SortedMap sub = getSortedMap().headMap(o1);
            return new PredicatedSortedMap(sub, keyPredicate, valuePredicate);
        }

        public SortedMap tailMap(Object o1) {
            SortedMap sub = getSortedMap().tailMap(o1);
            return new PredicatedSortedMap(sub, keyPredicate, valuePredicate);
        }

        private SortedMap getSortedMap() {
            return (SortedMap)map;
        }

    }

    /**
     * Implementation of a sorted map that is fixed in size.
     */
    static class FixedSizeSortedMap 
            extends FixedSizeMap 
            implements SortedMap {

        public FixedSizeSortedMap(SortedMap m) {
            super(m);
        }

        public Object firstKey() {
            return getSortedMap().firstKey();
        }

        public Object lastKey() {
            return getSortedMap().lastKey();
        }

        public Comparator comparator() {
            return getSortedMap().comparator();
        }

        public SortedMap subMap(Object o1, Object o2) {
            return new FixedSizeSortedMap(getSortedMap().subMap(o1, o2));
        }

        public SortedMap headMap(Object o1) {
            return new FixedSizeSortedMap(getSortedMap().headMap(o1));
        }

        public SortedMap tailMap(Object o1) {
            return new FixedSizeSortedMap(getSortedMap().tailMap(o1));
        }

        private SortedMap getSortedMap() {
            return (SortedMap)map;
        }

    }

    /**
     * Implementation of a sorted map that creates objects on demand.
     */
    static class LazySortedMap 
            extends LazyMap 
            implements SortedMap {

        public LazySortedMap(SortedMap m, Factory factory) {
            super(m, factory);
        }

        public Object firstKey() {
            return getSortedMap().firstKey();
        }

        public Object lastKey() {
            return getSortedMap().lastKey();
        }

        public Comparator comparator() {
            return getSortedMap().comparator();
        }

        public SortedMap subMap(Object o1, Object o2) {
            return new LazySortedMap(getSortedMap().subMap(o1, o2), factory);
        }

        public SortedMap headMap(Object o1) {
            return new LazySortedMap(getSortedMap().headMap(o1), factory);
        }

        public SortedMap tailMap(Object o1) {
            return new LazySortedMap(getSortedMap().tailMap(o1), factory);
        }

        private SortedMap getSortedMap() {
            return (SortedMap)map;
        }

    }

    /**
     * Implementation of a sorted map that creates objects on demand.
     */
    static class LazyTransformerSortedMap 
            extends LazyTransformerMap 
            implements SortedMap {

        public LazyTransformerSortedMap(SortedMap m, Transformer transformer) {
            super(m, transformer);
        }

        public Object firstKey() {
            return getSortedMap().firstKey();
        }

        public Object lastKey() {
            return getSortedMap().lastKey();
        }

        public Comparator comparator() {
            return getSortedMap().comparator();
        }

        public SortedMap subMap(Object o1, Object o2) {
            return new LazyTransformerSortedMap(getSortedMap().subMap(o1, o2), transformer);
        }

        public SortedMap headMap(Object o1) {
            return new LazyTransformerSortedMap(getSortedMap().headMap(o1), transformer);
        }

        public SortedMap tailMap(Object o1) {
            return new LazyTransformerSortedMap(getSortedMap().tailMap(o1), transformer);
        }

        private SortedMap getSortedMap() {
            return (SortedMap)map;
        }

    }

    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized map backed by the given map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to 
     * avoid non-deterministic behavior:
     *  
     * <pre>
     * Map m = MapUtils.synchronizedMap(myMap);
     * Set s = m.keySet();  // outside synchronized block
     * synchronized (m) {  // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()) {
     *         process (i.next());
     *     }
     * }
     * </pre>
     * 
     * This method uses the implementation in {@link java.util.Collections Collections}.
     * 
     * @param map  the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static Map synchronizedMap(Map map) {
        return Collections.synchronizedMap(map);
    }

    /**
     * Returns an unmodifiable map backed by the given map.
     * <p>
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param map  the map to make unmodifiable, must not be null
     * @return an unmodifiable map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static Map unmodifiableMap(Map map) {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Returns a predicated map backed by the given map.  Only keys and
     * values that pass the given predicates can be added to the map.
     * It is important not to use the original map after invoking this 
     * method, as it is a backdoor for adding unvalidated objects.
     *
     * @param map  the map to predicate, must not be null
     * @param keyPred  the predicate for keys, must not be null
     * @param valuePred  the predicate for values, must not be null
     * @return a predicated map backed by the given map
     * @throws IllegalArgumentException  if the Map or Predicates are null
     */
    public static Map predicatedMap(Map map, Predicate keyPred, Predicate valuePred) {
        return new PredicatedMap(map, keyPred, valuePred);
    }

    /**
     * Returns a typed map backed by the given map.
     * <p>
     * Only keys and values of the specified types can be added to the map.
     * 
     * @param map  the map to limit to a specific type, must not be null
     * @param keyType  the type of keys which may be added to the map
     * @param valueType  the type of values which may be added to the map
     * @return a typed map backed by the specified map
     */
    public static Map typedMap(Map map, Class keyType, Class valueType) {
        return predicatedMap(
            map, 
            new CollectionUtils.InstanceofPredicate(keyType),
            new CollectionUtils.InstanceofPredicate(valueType));
    }
    
    /**
     * Returns a fixed-sized map backed by the given map.
     * Elements may not be added or removed from the returned map, but 
     * existing elements can be changed (for instance, via the 
     * {@link Map#put(Object,Object)} method).
     *
     * @param map  the map whose size to fix, must not be null
     * @return a fixed-size map backed by that map
     * @throws IllegalArgumentException  if the Map is null
     */
    public static Map fixedSizeMap(Map map) {
        return new FixedSizeMap(map);
    }

    /**
     * Returns a "lazy" map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key.
     * <p>
     * For instance:
     * <pre>
     * Factory factory = new Factory() {
     *     public Object create() {
     *         return new Date();
     *     }
     * }
     * Map lazyMap = MapUtils.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("test");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
     * instance is the value for the <code>"test"</code> key in the map.
     *
     * @param map  the map to make lazy, must not be null
     * @param factory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Factory is null
     */
    public static Map lazyMap(Map map, Factory factory) {
        return new LazyMap(map, factory);
    }

    /**
     * Returns a "lazy" map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key. The factory is a {@link Transformer}
     * that will be passed the key which it must transform into the value.
     * <p>
     * For instance:
     * <pre>
     * Transformer factory = new Transformer() {
     *     public Object transform(Object mapKey) {
     *         return new File(mapKey);
     *     }
     * }
     * Map lazyMap = MapUtils.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("C:/dev");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>File</code> instance for the C drive dev directory.
     * Furthermore, that <code>File</code> instance is the value for the
     * <code>"C:/dev"</code> key in the map.
     * <p>
     * If a lazy map is wrapped by a synchronized map, the result is a simple
     * synchronized cache. When an object is not is the cache, the cache itself
     * calls back to the factory Transformer to populate itself, all within the
     * same synchronized block.
     *
     * @param map  the map to make lazy, must not be null
     * @param transformerFactory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Transformer is null
     */
    public static Map lazyMap(Map map, Transformer transformerFactory) {
        return new LazyTransformerMap(map, transformerFactory);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized sorted map backed by the given sorted map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to 
     * avoid non-deterministic behavior:
     *  
     * <pre>
     * Map m = MapUtils.synchronizedSortedMap(myMap);
     * Set s = m.keySet();  // outside synchronized block
     * synchronized (m) {  // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()) {
     *         process (i.next());
     *     }
     * }
     * </pre>
     * 
     * This method uses the implementation in {@link java.util.Collections Collections}.
     * 
     * @param map  the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static Map synchronizedSortedMap(SortedMap map) {
        return Collections.synchronizedSortedMap(map);
    }

    /**
     * Returns an unmodifiable sorted map backed by the given sorted map.
     * <p>
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param map  the sorted map to make unmodifiable, must not be null
     * @return an unmodifiable map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static Map unmodifiableSortedMap(SortedMap map) {
        return Collections.unmodifiableSortedMap(map);
    }

    /**
     * Returns a predicated sorted map backed by the given map.  Only keys and
     * values that pass the given predicates can be added to the map.
     * It is important not to use the original map after invoking this 
     * method, as it is a backdoor for adding unvalidated objects.
     *
     * @param map  the map to predicate, must not be null
     * @param keyPred  the predicate for keys, must not be null
     * @param valuePred  the predicate for values, must not be null
     * @return a predicated map backed by the given map
     * @throws IllegalArgumentException  if the SortedMap or Predicates are null
     */
    public static SortedMap predicatedSortedMap(SortedMap map, Predicate keyPred, Predicate valuePred) {
        return new PredicatedSortedMap(map, keyPred, valuePred);
    }

    /**
     * Returns a typed sorted map backed by the given map.
     * <p>
     * Only keys and values of the specified types can be added to the map.
     * 
     * @param map  the map to limit to a specific type, must not be null
     * @param keyType  the type of keys which may be added to the map
     * @param valueType  the type of values which may be added to the map
     * @return a typed map backed by the specified map
     */
    public static SortedMap typedSortedMap(SortedMap map, Class keyType, Class valueType) {
        return predicatedSortedMap(
            map, 
            new CollectionUtils.InstanceofPredicate(keyType),
            new CollectionUtils.InstanceofPredicate(valueType));
    }
    
    /**
     * Returns a fixed-sized sorted map backed by the given sorted map.
     * Elements may not be added or removed from the returned map, but 
     * existing elements can be changed (for instance, via the 
     * {@link Map#put(Object,Object)} method).
     *
     * @param map  the map whose size to fix, must not be null
     * @return a fixed-size map backed by that map
     * @throws IllegalArgumentException  if the SortedMap is null
     */
    public static SortedMap fixedSizeSortedMap(SortedMap map) {
        return new FixedSizeSortedMap(map);
    }

    /**
     * Returns a "lazy" sorted map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key.
     * <p>
     * For instance:
     *
     * <pre>
     * Factory factory = new Factory() {
     *     public Object create() {
     *         return new Date();
     *     }
     * }
     * SortedMap lazy = MapUtils.lazySortedMap(new TreeMap(), factory);
     * Object obj = lazy.get("test");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
     * instance is the value for the <code>"test"</code> key.
     *
     * @param map  the map to make lazy, must not be null
     * @param factory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the SortedMap or Factory is null
     */
    public static SortedMap lazySortedMap(SortedMap map, Factory factory) {
        return new LazySortedMap(map, factory);
    }
    
    /**
     * Returns a "lazy" sorted map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key. The factory is a {@link Transformer}
     * that will be passed the key which it must transform into the value.
     * <p>
     * For instance:
     * <pre>
     * Transformer factory = new Transformer() {
     *     public Object transform(Object mapKey) {
     *         return new File(mapKey);
     *     }
     * }
     * Map lazyMap = MapUtils.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("C:/dev");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>File</code> instance for the C drive dev directory.
     * Furthermore, that <code>File</code> instance is the value for the
     * <code>"C:/dev"</code> key in the map.
     * <p>
     * If a lazy map is wrapped by a synchronized map, the result is a simple
     * synchronized cache. When an object is not is the cache, the cache itself
     * calls back to the factory Transformer to populate itself, all within the
     * same synchronized block.
     *
     * @param map  the map to make lazy, must not be null
     * @param transformerFactory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Transformer is null
     */
    public static SortedMap lazySortedMap(SortedMap map, Transformer transformerFactory) {
        return new LazyTransformerSortedMap(map, transformerFactory);
    }

}

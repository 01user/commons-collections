package org.apache.commons.collections.comparators;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TestObject;

public abstract class TestComparator extends TestObject {

    public TestComparator(String testName) {
        super(testName);
    }

    public abstract Comparator makeComparator();
    public abstract List getComparableObjectsOrdered();

    public Object makeObject() {
        return makeComparator();
    }

    /**
     * There were no Comparators in version 1.x.
     * 
     * @return 2
     */
    public int getCompatibilityVersion() {
        return 2;
    }

    public void reverseObjects(List list) {
        Collections.reverse(list);
    }

    /**
     * Sort object according to the given Comparator.
     * 
     * @param list       List to sort
     * @param comparator sorting comparator
     */
    public void sortObjects(List list, Comparator comparator) {

        Collections.sort(list,comparator);

    }

    public boolean supportsEmptyCollections() {
        return false;
    }

    public boolean supportsFullCollections() {
        return false;
    }

    public void testEmptyListSort() {
        List list = new LinkedList();
        sortObjects(list,makeComparator());

        List list2 = new LinkedList();
        
        assertTrue("Comparator cannot sort empty lists",
                   list2.equals(list));
    }

    public void testRandomListSort() {
        Comparator comparator = makeComparator();

        List randomList = getComparableObjectsOrdered();
        reverseObjects(randomList);
        sortObjects(randomList,comparator);

        List orderedList = getComparableObjectsOrdered();

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));

    }

    /**
     * Nearly all Comparators should be Serializable.
     */
    public void testComparatorIsSerializable() {
        Comparator comparator = makeComparator();
        assertTrue("This comparator should be Serializable.",
                   comparator instanceof Serializable);
    }

    public String getCanonicalComparatorName(Object object) {
        StringBuffer retval = new StringBuffer();
        retval.append("data/test/");
        String colName = object.getClass().getName();
        colName = colName.substring(colName.lastIndexOf(".")+1,colName.length());
        retval.append(colName);
        retval.append(".version");
        retval.append(getCompatibilityVersion());
        retval.append(".obj");
        return retval.toString();
    }

    /**
     * Compare the current serialized form of the Comparator
     * against the canonical version in CVS.
     */
    public void testComparatorCompatibility() throws IOException, ClassNotFoundException {
        Comparator comparator = null;
        /*
         * Create canonical objects with this code
        comparator = makeComparator();
        
        writeExternalFormToDisk((Serializable) comparator, 
                                getCanonicalComparatorName(comparator));
        */

        // test to make sure the canonical form has been preserved
        comparator = 
            (Comparator) readExternalFormFromDisk(getCanonicalComparatorName(makeComparator()));
        
        // make sure the canonical form produces the ordering we currently
        // expect
        List randomList = getComparableObjectsOrdered();
        reverseObjects(randomList);
        sortObjects(randomList,comparator);

        List orderedList = getComparableObjectsOrdered();

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));
    }

}

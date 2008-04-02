package org.apache.commons.collections.functors;

import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Base class for tests of composite predicates.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 468603 $ $Date: 2006-10-27 17:52:37 -0700 (Fri, 27 Oct 2006) $
 *
 * @author Edwin Tellman
 */
public abstract class TestCompositePredicate<T> extends MockPredicateTestBase<T> {

    /**
     * Creates a new <code>TestCompositePredicate</code>.
     *
     * @param testValue the value which the mock predicates should expect to see (may be null).
     */
    protected TestCompositePredicate(final T testValue) {
        super(testValue);
    }

    /**
     * Creates an instance of the predicate to test.
     *
     * @param predicates the arguments to <code>getInstance</code>.
     *
     * @return a predicate to test.
     */
    protected abstract Predicate<T> getPredicateInstance(final Predicate<? super T> ... predicates);

    /**
     * Creates an instance of the predicate to test.
     *
     * @param predicates the argument to <code>getInstance</code>.
     *
     * @return a predicate to test.
     */
    protected abstract Predicate<T> getPredicateInstance(final Collection<Predicate<? super T>> predicates);

    /**
     * Creates an instance of the predicate to test.
     *
     * @param mockReturnValues the return values for the mock predicates, or null if that mock is not expected
     *                         to be called
     *
     * @return a predicate to test.
     */
    protected final Predicate<T> getPredicateInstance(final Boolean ... mockReturnValues)
    {
        final List<Predicate<? super T>> predicates = new ArrayList<Predicate<? super T>>();
        for (Boolean returnValue : mockReturnValues) {
            predicates.add(createMockPredicate(returnValue));
        }        
        return getPredicateInstance(predicates);
    }

    /**
     * Tests whether <code>getInstance</code> with a one element array returns the first element in the array.
     */
    public void singleElementArrayToGetInstance()
    {
        final Predicate<T> predicate = createMockPredicate(null);
        final Predicate<T> allPredicate = getPredicateInstance(predicate);
        Assert.assertSame("expected argument to be returned by getInstance()", predicate, allPredicate);
    }

    /**
     * Tests that passing a singleton collection to <code>getInstance</code> returns the single element in the
     * collection.
     */
    public void singletonCollectionToGetInstance()
    {
        final Predicate<T> predicate = createMockPredicate(null);
        final Predicate<T> allPredicate = getPredicateInstance(
                Collections.<Predicate<? super T>>singleton(predicate));
        Assert.assertSame("expected argument to be returned by getInstance()", predicate, allPredicate);
    }

    /**
     * Tests <code>getInstance</code> with a null predicate array.
     */
    @SuppressWarnings({"unchecked"})
    @Test(expected = IllegalArgumentException.class)
    public final void nullArrayToGetInstance() {
        getPredicateInstance((Predicate<T>[]) null);
    }

    /**
     * Tests <code>getInstance</code> with a single null element in the predicate array.
     */
    @SuppressWarnings({"unchecked"})
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementInArrayToGetInstance() {
        getPredicateInstance(new Predicate[] { null });
    }

    /**
     * Tests <code>getInstance</code> with two null elements in the predicate array.
     */
    @SuppressWarnings({"unchecked"})
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementsInArrayToGetInstance() {
        getPredicateInstance(new Predicate[] { null, null });
    }


    /**
     * Tests <code>getInstance</code> with a null predicate collection
     */
    @Test(expected = IllegalArgumentException.class)
    public final void nullCollectionToGetInstance() {
        getPredicateInstance((Collection<Predicate<? super T>>) null);
    }

    /**
     * Tests <code>getInstance</code> with a predicate collection that contains null elements
     */
    @Test(expected = IllegalArgumentException.class)
    public final void nullElementsInCollectionToGetInstance() {
        final Collection<Predicate<? super T>> coll = new ArrayList<Predicate<? super T>>();
        coll.add(null);
        coll.add(null);
        getPredicateInstance(coll);
    }
}

package dev.simplified.util;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * An immutable range of objects from a minimum to maximum point, both inclusive.
 * <p>
 * The elements must either implement {@link Comparable} (natural ordering) or a custom
 * {@link Comparator} must be supplied at construction time. The factory methods
 * {@link #between(Comparable, Comparable)} and {@link #is(Comparable)} automatically
 * normalize the element order so that {@link #getMinimum()} and {@link #getMaximum()}
 * always return the correct bounds regardless of argument order.
 * <p>
 * This class is thread-safe provided that the contained objects and the comparator are
 * also thread-safe.
 *
 * @param <T> the type of range values
 * @see Comparable
 * @see Comparator
 */
@Getter
public final class Range<T> implements Serializable {

    /**
     * Creates a range with the specified minimum and maximum values (both inclusive),
     * using the natural ordering of the elements.
     * <p>
     * The arguments may be passed in either order; the minimum and maximum will be
     * determined automatically.
     *
     * @param <T> the type of the elements in the range
     * @param fromInclusive the first boundary of the range, inclusive
     * @param toInclusive the second boundary of the range, inclusive
     * @return a new range spanning from the lesser to the greater element
     * @throws ClassCastException if the elements are not {@link Comparable}
     */
    public static <T extends Comparable<T>> @NotNull Range<T> between(@NotNull T fromInclusive, @NotNull T toInclusive) {
        return between(fromInclusive, toInclusive, null);
    }

    /**
     * Creates a range with the specified minimum and maximum values (both inclusive),
     * using the given comparator to determine element order.
     * <p>
     * The arguments may be passed in either order; the minimum and maximum will be
     * determined automatically via the comparator. If the comparator is {@code null},
     * natural ordering is used.
     *
     * @param <T> the type of the elements in the range
     * @param fromInclusive the first boundary of the range, inclusive
     * @param toInclusive the second boundary of the range, inclusive
     * @param comparator the comparator to use for ordering, or {@code null} for natural ordering
     * @return a new range spanning from the lesser to the greater element
     * @throws ClassCastException if using natural ordering and the elements are not {@link Comparable}
     */
    public static <T> @NotNull Range<T> between(@NotNull T fromInclusive, @NotNull T toInclusive, @Nullable Comparator<T> comparator) {
        return new Range<>(fromInclusive, toInclusive, comparator);
    }

    /**
     * Creates a range containing exactly one element, using the natural ordering of the element.
     * <p>
     * The returned range has equal minimum and maximum values.
     *
     * @param <T> the type of the element in the range
     * @param element the single element defining both boundaries of the range
     * @return a new range where minimum and maximum are both the given element
     * @throws ClassCastException if the element is not {@link Comparable}
     */
    public static <T extends Comparable<T>> @NotNull Range<T> is(@NotNull T element) {
        return between(element, element, null);
    }

    /**
     * Creates a range containing exactly one element, using the given comparator.
     * <p>
     * The returned range has equal minimum and maximum values. If the comparator is
     * {@code null}, natural ordering is used.
     *
     * @param <T> the type of the element in the range
     * @param element the single element defining both boundaries of the range
     * @param comparator the comparator to use for ordering, or {@code null} for natural ordering
     * @return a new range where minimum and maximum are both the given element
     * @throws ClassCastException if using natural ordering and the element is not {@link Comparable}
     */
    public static <T> @NotNull Range<T> is(@NotNull T element, @Nullable Comparator<T> comparator) {
        return between(element, element, comparator);
    }

    /**
     * The comparator used to order elements within this range; never {@code null} (natural ordering uses an internal implementation).
     */
    private final @NotNull Comparator<T> comparator;

    /**
     * Cached hash code value (safe because this class is immutable).
     */
    @Getter(AccessLevel.NONE)
    private transient int hashCode;

    /**
     * The maximum (upper bound) value in this range, inclusive.
     */
    private final @NotNull T maximum;

    /**
     * The minimum (lower bound) value in this range, inclusive.
     */
    private final @NotNull T minimum;

    /**
     * Creates a new range between the two given elements, using the given comparator
     * to determine which is the minimum and which is the maximum.
     *
     * @param element1 the first element
     * @param element2 the second element
     * @param comp the comparator to use, or {@code null} for natural ordering
     */
    @SuppressWarnings("unchecked")
    private Range(@NotNull T element1, @NotNull T element2, @Nullable Comparator<T> comp) {
        this.comparator = Objects.requireNonNullElse(comp, ComparableComparator.INSTANCE);

        if (this.comparator.compare(element1, element2) < 1) {
            this.minimum = element1;
            this.maximum = element2;
        } else {
            this.minimum = element2;
            this.maximum = element1;
        }
    }

    /**
     * Checks whether the specified element falls within this range (inclusive on both ends).
     *
     * @param element the element to check, {@code null} returns {@code false}
     * @return {@code true} if the element is within this range, {@code false} otherwise
     */
    public boolean contains(@Nullable final T element) {
        if (element == null)
            return false;

        return comparator.compare(element, minimum) > -1 && comparator.compare(element, maximum) < 1;
    }

    /**
     * Checks whether this range fully contains the specified other range.
     * <p>
     * This method may fail if the ranges use different comparators or element types.
     *
     * @param otherRange the range to check, {@code null} returns {@code false}
     * @return {@code true} if this range contains every element of the other range, {@code false} otherwise
     * @throws RuntimeException if the ranges cannot be compared
     */
    public boolean containsRange(@Nullable final Range<T> otherRange) {
        if (otherRange == null)
            return false;

        return contains(otherRange.minimum) && contains(otherRange.maximum);
    }

    /**
     * Determines the position of the specified element relative to this range.
     * <p>
     * Returns {@code -1} if the element is below the range minimum, {@code 0} if the element
     * is contained within the range, and {@code 1} if the element is above the range maximum.
     *
     * @param element the element to compare against this range
     * @return {@code -1}, {@code 0}, or {@code 1} depending on whether the element is before,
     *         within, or after this range
     */
    public int elementCompareTo(@NotNull final T element) {
        if (isAfter(element))
            return -1;
        else if (isBefore(element))
            return 1;
        else
            return 0;
    }

    /**
     * Compares this range to another object for equality.
     * <p>
     * Two ranges are equal if their minimum and maximum values are equal, regardless of
     * any differences in their comparators.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if the given object is a {@code Range} with equal minimum and maximum values
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;
        else if (obj == null || obj.getClass() != getClass())
            return false;
        else {
            @SuppressWarnings("unchecked") // OK because we checked the class above
            final Range<T> range = (Range<T>) obj;
            return minimum.equals(range.minimum) &&
                    maximum.equals(range.maximum);
        }
    }

    /**
     * Computes a hash code for this range based on the minimum and maximum values.
     * <p>
     * The hash code is cached after the first computation since this class is immutable.
     *
     * @return a hash code value for this range
     */
    @Override
    public int hashCode() {
        int result = hashCode;

        if (hashCode == 0) {
            result = 17;
            result = 37 * result + getClass().hashCode();
            result = 37 * result + minimum.hashCode();
            result = 37 * result + maximum.hashCode();
            hashCode = result;
        }

        return result;
    }

    /**
     * Computes the intersection of this range with the given overlapping range.
     * <p>
     * If the two ranges are equal, this range is returned as-is.
     *
     * @param other the overlapping range to intersect with
     * @return a new range representing the intersection, or this range if the two are equal
     * @throws IllegalArgumentException if the given range does not overlap this range
     */
    public @NotNull Range<T> intersectionWith(@NotNull final Range<T> other) {
        if (!this.isOverlappedBy(other))
            throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", other));

        if (this.equals(other))
            return this;

        final T min = getComparator().compare(minimum, other.minimum) < 0 ? other.minimum : minimum;
        final T max = getComparator().compare(maximum, other.maximum) < 0 ? maximum : other.maximum;
        return between(min, max, getComparator());
    }

    /**
     * Checks whether this range is entirely after the specified element (i.e., the element is
     * below the range minimum).
     *
     * @param element the element to check, {@code null} returns {@code false}
     * @return {@code true} if this range is entirely after the specified element
     */
    public boolean isAfter(@Nullable final T element) {
        if (element == null)
            return false;

        return comparator.compare(element, minimum) < 0;
    }

    /**
     * Checks whether this range is completely after the specified other range.
     * <p>
     * This method may fail if the ranges use different comparators or element types.
     *
     * @param otherRange the range to check, {@code null} returns {@code false}
     * @return {@code true} if this range is entirely after the other range
     * @throws RuntimeException if the ranges cannot be compared
     */
    public boolean isAfterRange(@Nullable final Range<T> otherRange) {
        if (otherRange == null)
            return false;

        return isAfter(otherRange.maximum);
    }

    /**
     * Checks whether this range is entirely before the specified element (i.e., the element is
     * above the range maximum).
     *
     * @param element the element to check, {@code null} returns {@code false}
     * @return {@code true} if this range is entirely before the specified element
     */
    public boolean isBefore(@Nullable final T element) {
        if (element == null)
            return false;

        return comparator.compare(element, maximum) > 0;
    }

    /**
     * Checks whether this range is completely before the specified other range.
     * <p>
     * This method may fail if the ranges use different comparators or element types.
     *
     * @param otherRange the range to check, {@code null} returns {@code false}
     * @return {@code true} if this range is entirely before the other range
     * @throws RuntimeException if the ranges cannot be compared
     */
    public boolean isBeforeRange(@Nullable final Range<T> otherRange) {
        if (otherRange == null)
            return false;

        return isBefore(otherRange.minimum);
    }

    /**
     * Checks whether the specified element is equal to the maximum of this range.
     *
     * @param element the element to check, {@code null} returns {@code false}
     * @return {@code true} if the element equals the range maximum
     */
    public boolean isEndedBy(@Nullable final T element) {
        if (element == null)
            return false;

        return comparator.compare(element, maximum) == 0;
    }

    /**
     * Checks whether this range uses the natural ordering of its elements.
     * <p>
     * Natural ordering is indicated by the use of an internal {@link Comparable}-based comparator.
     * This method is the only way to determine whether a {@code null} comparator was supplied
     * at construction time.
     *
     * @return {@code true} if this range uses natural ordering, {@code false} if a custom comparator was provided
     */
    public boolean isNaturalOrdering() {
        return comparator == Range.ComparableComparator.INSTANCE;
    }

    /**
     * Checks whether this range is overlapped by the specified other range.
     * <p>
     * Two ranges overlap if they share at least one element in common.
     * This method may fail if the ranges use different comparators or element types.
     *
     * @param otherRange the range to test, {@code null} returns {@code false}
     * @return {@code true} if the specified range overlaps with this range
     * @throws RuntimeException if the ranges cannot be compared
     */
    public boolean isOverlappedBy(@Nullable final Range<T> otherRange) {
        if (otherRange == null)
            return false;

        return otherRange.contains(minimum)
            || otherRange.contains(maximum)
            || contains(otherRange.minimum);
    }

    /**
     * Checks whether the specified element is equal to the minimum of this range.
     *
     * @param element the element to check, {@code null} returns {@code false}
     * @return {@code true} if the element equals the range minimum
     */
    public boolean isStartedBy(@Nullable final T element) {
        if (element == null)
            return false;

        return comparator.compare(element, minimum) == 0;
    }

    /**
     * Clamps the given element to this range by returning the element itself if it falls
     * within the range, the range minimum if the element is below it, or the range maximum
     * if the element is above it.
     *
     * <pre><code>
     * Range&lt;Integer&gt; range = Range.between(16, 64);
     * range.fit(-9) --&gt;  16
     * range.fit(0)  --&gt;  16
     * range.fit(15) --&gt;  16
     * range.fit(16) --&gt;  16
     * range.fit(17) --&gt;  17
     * ...
     * range.fit(63) --&gt;  63
     * range.fit(64) --&gt;  64
     * range.fit(99) --&gt;  64
     * </code></pre>
     *
     * @param element the element to clamp
     * @return the minimum, the element, or the maximum depending on the element's position relative to this range
     * @throws NullPointerException if the element is {@code null}
     */
    public @NotNull T fit(@NotNull final T element) {
        if (isAfter(element))
            return minimum;
        else if (isBefore(element))
            return maximum;
        else
            return element;
    }

    /**
     * Returns a string representation of this range in the format {@code [min..max]}.
     *
     * @return a string representation of this range
     */
    @Override
    public @NotNull String toString() {
        return "[" + minimum + ".." + maximum + "]";
    }

    /**
     * Returns a formatted string representation of this range using the given format string.
     * <p>
     * The format string may contain the following placeholders:
     * <ul>
     *     <li>{@code %1$s} for the minimum element</li>
     *     <li>{@code %2$s} for the maximum element</li>
     *     <li>{@code %3$s} for the comparator</li>
     * </ul>
     * The default format used by {@link #toString()} is {@code [%1$s..%2$s]}.
     *
     * @param format the format string
     * @return the formatted string representation
     */
    public @NotNull String toString(@NotNull final String format) {
        return String.format(format, minimum, maximum, comparator);
    }

    /**
     * Internal comparator that delegates to {@link Comparable#compareTo(Object)} for natural ordering.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private enum ComparableComparator implements Comparator {

        /**
         * Singleton instance.
         */
        INSTANCE;

        /**
         * Compares two {@link Comparable} objects using their natural ordering.
         *
         * @param obj1 the first object to compare
         * @param obj2 the second object to compare
         * @return a negative integer, zero, or a positive integer as the first object is less than,
         *         equal to, or greater than the second
         */
        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }

    }

}

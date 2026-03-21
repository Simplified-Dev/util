package dev.sbs.api.util.mutable;

import dev.sbs.api.util.NumberUtil;

/**
 * A mutable {@code long} wrapper.
 * <p>
 * Note that as {@code MutableLong} does not extend {@link Long}, it is not treated by
 * {@link String#format} as a {@link Long} parameter.
 *
 * @see Long
 */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {

    /** The mutable value. */
    private long value;

    /**
     * Constructs a new {@code MutableLong} with the default value of zero.
     */
    public MutableLong() { }

    /**
     * Constructs a new {@code MutableLong} with the specified value.
     *
     * @param value the initial value to store
     */
    public MutableLong(final long value) {
        this.value = value;
    }

    /**
     * Constructs a new {@code MutableLong} with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the value is null
     */
    public MutableLong(final Number value) {
        this.value = value.longValue();
    }

    /**
     * Constructs a new {@code MutableLong} by parsing the given string.
     *
     * @param value the string to parse, not null
     * @throws NumberFormatException if the string cannot be parsed into a long
     */
    public MutableLong(final String value) {
        this.value = Long.parseLong(value);
    }

    /**
     * Returns the value as a {@link Long} instance.
     *
     * @return the value as a {@link Long}, never null
     */
    @Override
    public Long get() {
        return this.value;
    }

    /**
     * Sets the value from a primitive {@code long}.
     *
     * @param value the value to set
     */
    public void set(final long value) {
        this.value = value;
    }

    /**
     * Sets the value from a {@link Number} instance.
     *
     * @param value the value to set, not null
     * @throws NullPointerException if the value is null
     */
    @Override
    public void set(final Number value) {
        this.value = value.longValue();
    }

    /**
     * Increments the value by one.
     */
    public void increment() {
        value++;
    }

    /**
     * Returns the current value and then increments by one. This method is not thread safe.
     *
     * @return the value before incrementing
     */
    public long getAndIncrement() {
        final long last = value;
        value++;
        return last;
    }

    /**
     * Increments by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after incrementing
     */
    public long incrementAndGet() {
        value++;
        return value;
    }

    /**
     * Decrements the value by one.
     */
    public void decrement() {
        value--;
    }

    /**
     * Returns the current value and then decrements by one. This method is not thread safe.
     *
     * @return the value before decrementing
     */
    public long getAndDecrement() {
        final long last = value;
        value--;
        return last;
    }

    /**
     * Decrements by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after decrementing
     */
    public long decrementAndGet() {
        value--;
        return value;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add
     */
    public void add(final long operand) {
        this.value += operand;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add, not null
     * @throws NullPointerException if the operand is null
     */
    public void add(final Number operand) {
        this.value += operand.longValue();
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract
     */
    public void subtract(final long operand) {
        this.value -= operand;
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract, not null
     * @throws NullPointerException if the operand is null
     */
    public void subtract(final Number operand) {
        this.value -= operand.longValue();
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value after adding the operand
     */
    public long addAndGet(final long operand) {
        this.value += operand;
        return value;
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws NullPointerException if {@code operand} is null
     * @return the value after adding the operand
     */
    public long addAndGet(final Number operand) {
        this.value += operand.longValue();
        return value;
    }

    /**
     * Returns the current value and then adds the given operand. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value before adding the operand
     */
    public long getAndAdd(final long operand) {
        final long last = value;
        this.value += operand;
        return last;
    }

    /**
     * Returns the current value and then adds the given operand. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws NullPointerException if {@code operand} is null
     * @return the value before adding the operand
     */
    public long getAndAdd(final Number operand) {
        final long last = value;
        this.value += operand.longValue();
        return last;
    }

    /** {@inheritDoc} */
    @Override
    public int intValue() {
        return (int) value;
    }

    /** {@inheritDoc} */
    @Override
    public long longValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public float floatValue() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public double doubleValue() {
        return value;
    }

    /**
     * Converts this mutable to a {@link Long} instance.
     *
     * @return a {@link Long} containing this mutable's value, never null
     */
    public Long toLong() {
        return longValue();
    }

    /**
     * Compares this object against the specified object. The result is {@code true} if and only if the argument
     * is not {@code null} and is a {@code MutableLong} object that contains the same {@code long}
     * value as this object.
     *
     * @param obj the object to compare with, {@code null} returns {@code false}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableLong) {
            return value == ((MutableLong) obj).longValue();
        }
        return false;
    }

    /**
     * Returns a hash code for this mutable.
     *
     * @return the hash code of the stored value
     */
    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    /**
     * Compares this mutable to another in ascending order.
     *
     * @param other the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final MutableLong other) {
        return NumberUtil.compare(this.value, other.value);
    }

    /**
     * Returns the string representation of this mutable's value.
     *
     * @return the mutable value as a string
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}

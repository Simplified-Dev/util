package dev.sbs.api.util.mutable;

import dev.sbs.api.util.NumberUtil;

/**
 * A mutable {@code int} wrapper.
 * <p>
 * Note that as {@code MutableInt} does not extend {@link Integer}, it is not treated by
 * {@link String#format} as an {@link Integer} parameter.
 *
 * @see Integer
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    /** The mutable value. */
    private int value;

    /**
     * Constructs a new {@code MutableInt} with the default value of zero.
     */
    public MutableInt() { }

    /**
     * Constructs a new {@code MutableInt} with the specified value.
     *
     * @param value the initial value to store
     */
    public MutableInt(final int value) {
        this.value = value;
    }

    /**
     * Constructs a new {@code MutableInt} with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the value is null
     */
    public MutableInt(final Number value) {
        this.value = value.intValue();
    }

    /**
     * Constructs a new {@code MutableInt} by parsing the given string.
     *
     * @param value the string to parse, not null
     * @throws NumberFormatException if the string cannot be parsed into an int
     */
    public MutableInt(final String value) {
        this.value = Integer.parseInt(value);
    }

    /**
     * Returns the value as an {@link Integer} instance.
     *
     * @return the value as an {@link Integer}, never null
     */
    @Override
    public Integer get() {
        return this.value;
    }

    /**
     * Sets the value from a primitive {@code int}.
     *
     * @param value the value to set
     */
    public void set(final int value) {
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
        this.value = value.intValue();
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
    public int getAndIncrement() {
        final int last = value;
        value++;
        return last;
    }

    /**
     * Increments by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after incrementing
     */
    public int incrementAndGet() {
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
    public int getAndDecrement() {
        final int last = value;
        value--;
        return last;
    }

    /**
     * Decrements by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after decrementing
     */
    public int decrementAndGet() {
        value--;
        return value;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add
     */
    public void add(final int operand) {
        this.value += operand;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add, not null
     * @throws NullPointerException if the operand is null
     */
    public void add(final Number operand) {
        this.value += operand.intValue();
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract
     */
    public void subtract(final int operand) {
        this.value -= operand;
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract, not null
     * @throws NullPointerException if the operand is null
     */
    public void subtract(final Number operand) {
        this.value -= operand.intValue();
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value after adding the operand
     */
    public int addAndGet(final int operand) {
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
    public int addAndGet(final Number operand) {
        this.value += operand.intValue();
        return value;
    }

    /**
     * Returns the current value and then adds the given operand. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value before adding the operand
     */
    public int getAndAdd(final int operand) {
        final int last = value;
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
    public int getAndAdd(final Number operand) {
        final int last = value;
        this.value += operand.intValue();
        return last;
    }

    /** {@inheritDoc} */
    @Override
    public int intValue() {
        return value;
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
     * Converts this mutable to an {@link Integer} instance.
     *
     * @return an {@link Integer} containing this mutable's value, never null
     */
    public Integer toInteger() {
        return intValue();
    }

    /**
     * Compares this object against the specified object. The result is {@code true} if and only if the argument
     * is not {@code null} and is a {@code MutableInt} object that contains the same {@code int} value
     * as this object.
     *
     * @param obj the object to compare with, {@code null} returns {@code false}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableInt) {
            return value == ((MutableInt) obj).intValue();
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
        return value;
    }

    /**
     * Compares this mutable to another in ascending order.
     *
     * @param other the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final MutableInt other) {
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

package dev.sbs.api.util.mutable;

import dev.sbs.api.util.NumberUtil;

/**
 * A mutable {@code short} wrapper.
 * <p>
 * Note that as {@code MutableShort} does not extend {@link Short}, it is not treated by
 * {@link String#format} as a {@link Short} parameter.
 *
 * @see Short
 */
public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {

    /** The mutable value. */
    private short value;

    /**
     * Constructs a new {@code MutableShort} with the default value of zero.
     */
    public MutableShort() { }

    /**
     * Constructs a new {@code MutableShort} with the specified value.
     *
     * @param value the initial value to store
     */
    public MutableShort(final short value) {
        this.value = value;
    }

    /**
     * Constructs a new {@code MutableShort} with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the value is null
     */
    public MutableShort(final Number value) {
        this.value = value.shortValue();
    }

    /**
     * Constructs a new {@code MutableShort} by parsing the given string.
     *
     * @param value the string to parse, not null
     * @throws NumberFormatException if the string cannot be parsed into a short
     */
    public MutableShort(final String value) {
        this.value = Short.parseShort(value);
    }

    /**
     * Returns the value as a {@link Short} instance.
     *
     * @return the value as a {@link Short}, never null
     */
    @Override
    public Short get() {
        return this.value;
    }

    /**
     * Sets the value from a primitive {@code short}.
     *
     * @param value the value to set
     */
    public void set(final short value) {
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
        this.value = value.shortValue();
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
    public short getAndIncrement() {
        final short last = value;
        value++;
        return last;
    }

    /**
     * Increments by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after incrementing
     */
    public short incrementAndGet() {
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
    public short getAndDecrement() {
        final short last = value;
        value--;
        return last;
    }

    /**
     * Decrements by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after decrementing
     */
    public short decrementAndGet() {
        value--;
        return value;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add
     */
    public void add(final short operand) {
        this.value += operand;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add, not null
     * @throws NullPointerException if the operand is null
     */
    public void add(final Number operand) {
        this.value += operand.shortValue();
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract
     */
    public void subtract(final short operand) {
        this.value -= operand;
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract, not null
     * @throws NullPointerException if the operand is null
     */
    public void subtract(final Number operand) {
        this.value -= operand.shortValue();
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value after adding the operand
     */
    public short addAndGet(final short operand) {
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
    public short addAndGet(final Number operand) {
        this.value += operand.shortValue();
        return value;
    }

    /**
     * Returns the current value and then adds the given operand. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value before adding the operand
     */
    public short getAndAdd(final short operand) {
        final short last = value;
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
    public short getAndAdd(final Number operand) {
        final short last = value;
        this.value += operand.shortValue();
        return last;
    }

    /** {@inheritDoc} */
    @Override
    public short shortValue() {
        return value;
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
     * Converts this mutable to a {@link Short} instance.
     *
     * @return a {@link Short} containing this mutable's value, never null
     */
    public Short toShort() {
        return shortValue();
    }

    /**
     * Compares this object against the specified object. The result is {@code true} if and only if the argument
     * is not {@code null} and is a {@code MutableShort} object that contains the same {@code short}
     * value as this object.
     *
     * @param obj the object to compare with, {@code null} returns {@code false}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableShort) {
            return value == ((MutableShort) obj).shortValue();
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
    public int compareTo(final MutableShort other) {
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

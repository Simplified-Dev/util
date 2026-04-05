package dev.simplified.util.mutable;

import dev.simplified.util.NumberUtil;

/**
 * A mutable {@code byte} wrapper.
 *
 * <p>
 * Note that as {@code MutableByte} does not extend {@link Byte}, it is not treated by
 * {@link String#format} as a {@link Byte} parameter.
 *
 * @see Byte
 */
public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {

    /** The mutable value. */
    private byte value;

    /**
     * Constructs a new {@code MutableByte} with the default value of zero.
     */
    public MutableByte() { }

    /**
     * Constructs a new {@code MutableByte} with the specified value.
     *
     * @param value the initial value to store
     */
    public MutableByte(final byte value) {
        this.value = value;
    }

    /**
     * Constructs a new {@code MutableByte} with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the value is null
     */
    public MutableByte(final Number value) {
        this.value = value.byteValue();
    }

    /**
     * Constructs a new {@code MutableByte} by parsing the given string.
     *
     * @param value the string to parse, not null
     * @throws NumberFormatException if the string cannot be parsed into a byte
     */
    public MutableByte(final String value) {
        this.value = Byte.parseByte(value);
    }

    /**
     * Returns the value as a {@link Byte} instance.
     *
     * @return the value as a {@link Byte}, never null
     */
    @Override
    public Byte get() {
        return this.value;
    }

    /**
     * Sets the value from a primitive {@code byte}.
     *
     * @param value the value to set
     */
    public void set(final byte value) {
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
        this.value = value.byteValue();
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
    public byte getAndIncrement() {
        final byte last = value;
        value++;
        return last;
    }

    /**
     * Increments by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after incrementing
     */
    public byte incrementAndGet() {
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
    public byte getAndDecrement() {
        final byte last = value;
        value--;
        return last;
    }

    /**
     * Decrements by one and then returns the new value. This method is not thread safe.
     *
     * @return the value after decrementing
     */
    public byte decrementAndGet() {
        value--;
        return value;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add
     */
    public void add(final byte operand) {
        this.value += operand;
    }

    /**
     * Adds the given operand to this instance's value.
     *
     * @param operand the value to add, not null
     * @throws NullPointerException if the operand is null
     */
    public void add(final Number operand) {
        this.value += operand.byteValue();
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract
     */
    public void subtract(final byte operand) {
        this.value -= operand;
    }

    /**
     * Subtracts the given operand from this instance's value.
     *
     * @param operand the value to subtract, not null
     * @throws NullPointerException if the operand is null
     */
    public void subtract(final Number operand) {
        this.value -= operand.byteValue();
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value after adding the operand
     */
    public byte addAndGet(final byte operand) {
        this.value += operand;
        return value;
    }

    /**
     * Adds the given operand and then returns the new value. This method is not thread safe.
     *
     * @param operand the quantity to add, not null
     * @throws NullPointerException if the operand is null
     * @return the value after adding the operand
     */
    public byte addAndGet(final Number operand) {
        this.value += operand.byteValue();
        return value;
    }

    /**
     * Returns the current value and then adds the given operand. This method is not thread safe.
     *
     * @param operand the quantity to add
     * @return the value before adding the operand
     */
    public byte getAndAdd(final byte operand) {
        final byte last = value;
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
    public byte getAndAdd(final Number operand) {
        final byte last = value;
        this.value += operand.byteValue();
        return last;
    }

    /** {@inheritDoc} */
    @Override
    public byte byteValue() {
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
     * Converts this mutable to a {@link Byte} instance.
     *
     * @return a {@link Byte} containing this mutable's value, never null
     */
    public Byte toByte() {
        return byteValue();
    }

    /**
     * Compares this object against the specified object. The result is {@code true} if and only if the argument
     * is not {@code null} and is a {@code MutableByte} object that contains the same {@code byte} value
     * as this object.
     *
     * @param obj the object to compare with, {@code null} returns {@code false}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableByte) {
            return value == ((MutableByte) obj).byteValue();
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
        return Byte.hashCode(value);
    }

    /**
     * Compares this mutable to another in ascending order.
     *
     * @param other the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final MutableByte other) {
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

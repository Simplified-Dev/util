package dev.sbs.api.util.mutable;

import java.io.Serializable;

/**
 * A mutable {@code boolean} wrapper.
 *
 * <p>
 * Note that as {@code MutableBoolean} does not extend {@link Boolean}, it is not treated by
 * {@link String#format} as a {@link Boolean} parameter.
 *
 * @see Boolean
 */
public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean> {

    /** The mutable value. */
    private boolean value;

    /**
     * Constructs a new {@code MutableBoolean} with the default value of {@code false}.
     */
    public MutableBoolean() { }

    /**
     * Constructs a new {@code MutableBoolean} with the specified value.
     *
     * @param value the initial value to store
     */
    public MutableBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Constructs a new {@code MutableBoolean} with the specified value.
     *
     * @param value the initial value to store, not null
     * @throws NullPointerException if the value is null
     */
    public MutableBoolean(final Boolean value) {
        this.value = value;
    }

    /**
     * Returns the value as a {@link Boolean} instance.
     *
     * @return the value as a {@link Boolean}, never null
     */
    @Override
    public Boolean get() {
        return this.value;
    }

    /**
     * Sets the value from a primitive {@code boolean}.
     *
     * @param value the value to set
     */
    public void set(final boolean value) {
        this.value = value;
    }

    /**
     * Sets the value to {@code false}.
     */
    public void setFalse() {
        this.value = false;
    }

    /**
     * Sets the value to {@code true}.
     */
    public void setTrue() {
        this.value = true;
    }

    /**
     * Sets the value from a {@link Boolean} instance.
     *
     * @param value the value to set, not null
     * @throws NullPointerException if the value is null
     */
    @Override
    public void set(final Boolean value) {
        this.value = value;
    }

    /**
     * Checks if the current value is {@code true}.
     *
     * @return {@code true} if the current value is {@code true}
     */
    public boolean isTrue() {
        return value;
    }

    /**
     * Checks if the current value is {@code false}.
     *
     * @return {@code true} if the current value is {@code false}
     */
    public boolean isFalse() {
        return !value;
    }

    /**
     * Returns the primitive {@code boolean} value of this mutable.
     *
     * @return the boolean value represented by this object
     */
    public boolean booleanValue() {
        return value;
    }

    /**
     * Converts this mutable to a {@link Boolean} instance.
     *
     * @return a {@link Boolean} containing this mutable's value, never null
     */
    public Boolean toBoolean() {
        return booleanValue();
    }

    /**
     * Compares this object to the specified object. The result is {@code true} if and only if the argument is
     * not {@code null} and is a {@code MutableBoolean} object that contains the same
     * {@code boolean} value as this object.
     *
     * @param obj the object to compare with, {@code null} returns {@code false}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableBoolean) {
            return value == ((MutableBoolean) obj).booleanValue();
        }
        return false;
    }

    /**
     * Returns a hash code consistent with {@link Boolean#hashCode()}.
     *
     * @return the hash code of {@code Boolean.TRUE} or {@code Boolean.FALSE}
     */
    @Override
    public int hashCode() {
        return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    /**
     * Compares this mutable to another in ascending order, where {@code false} is less than {@code true}.
     *
     * @param other the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final MutableBoolean other) {
        return (this.value == other.value) ? 0 : (this.value ? 1 : -1);
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

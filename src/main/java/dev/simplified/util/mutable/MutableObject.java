package dev.sbs.api.util.mutable;

import java.io.Serializable;

/**
 * A mutable {@code Object} wrapper.
 *
 * @param <T> the type to set and get
 */
public class MutableObject<T> implements Mutable<T>, Serializable {

    /** The mutable value. */
    private T value;

    /**
     * Constructs a new MutableObject with the default value of {@code null}.
     */
    public MutableObject() {
        super();
    }

    /**
     * Constructs a new MutableObject with the specified value.
     *
     * @param value  the initial value to store
     */
    public MutableObject(final T value) {
        super();
        this.value = value;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value.
     *
     * @return the value, may be null
     */
    @Override
    public T get() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value  the value to set
     */
    @Override
    public void set(final T value) {
        this.value = value;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>
     * Compares this object against the specified object. The result is {@code true} if and only if the argument
     * is not {@code null} and is a {@code MutableObject} object that contains the same {@code T}
     * value as this object.
     * </p>
     *
     * @param obj  the object to compare with, {@code null} returns {@code false}
     * @return  {@code true} if the objects are the same;
     *          {@code true} if the objects have equivalent {@code value} fields;
     *          {@code false} otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() == obj.getClass()) {
            final MutableObject<?> that = (MutableObject<?>) obj;
            return this.value.equals(that.value);
        }
        return false;
    }

    /**
     * Returns the value's hash code or {@code 0} if the value is {@code null}.
     *
     * @return the value's hash code or {@code 0} if the value is {@code null}.
     */
    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the String value of this mutable.
     *
     * @return the mutable value as a string
     */
    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }

}

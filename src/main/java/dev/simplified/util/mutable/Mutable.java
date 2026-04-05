package dev.simplified.util.mutable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A generic interface providing mutable access to a value.
 * <p>
 * A typical use case is to pass a primitive or string to a method and allow that method to
 * effectively change the value. Another use case is to store a frequently changing primitive in
 * a collection (for example a total in a map) without creating new wrapper objects.
 *
 * @param <T> the type to set and get
 */
public interface Mutable<T> {

    /** The stored value of this mutable. */
    T get();

    /**
     * Sets the value of this mutable.
     *
     * @param value the value to store
     * @throws NullPointerException if the value is null and null is not permitted
     * @throws ClassCastException if the value type is incompatible
     */
    void set(T value);

    /**
     * Creates a new {@link Mutable} with a {@code null} initial value.
     *
     * @param <T> the type of the value to be stored
     * @return a new mutable instance
     */
    static <T> @NotNull Mutable<T> of() {
        return new Impl<>();
    }

    /**
     * Creates a new {@link Mutable} with the given initial value.
     *
     * @param <T> the type of the value to be stored
     * @param value the initial value, may be null
     * @return a new mutable instance containing the specified value
     */
    static <T> @NotNull Mutable<T> of(@Nullable T value) {
        return new Impl<>(value);
    }

    /**
     * A mutable {@link Object} wrapper implementing {@link Mutable} and {@link Serializable}.
     *
     * @param <T> the type to set and get
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(fluent = true)
    class Impl<T> implements Mutable<T>, Serializable {

        private @Nullable T value;

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public void set(final T value) {
            this.value = value;
        }

        /**
         * Compares this object against the specified object. The result is {@code true} if and only if the argument
         * is not {@code null} and is an {@code Impl} object that contains an equal value.
         *
         * @param obj the object to compare with, {@code null} returns {@code false}
         * @return {@code true} if the objects are equal; {@code false} otherwise
         */
        @Override
        public boolean equals(final Object obj) {
            if (obj == null)
                return false;

            if (this == obj)
                return true;

            if (this.getClass() == obj.getClass()) {
                final Impl<?> that = (Impl<?>) obj;
                return Objects.equals(this.value, that.value);
            }

            return false;
        }

        /**
         * Returns the value's hash code, or {@code 0} if the value is {@code null}.
         *
         * @return the hash code of the stored value
         */
        @Override
        public int hashCode() {
            return this.value == null ? 0 : this.value.hashCode();
        }

        /**
         * Returns the string representation of the stored value, or {@code "null"} if the value is {@code null}.
         *
         * @return the string form of this mutable's value
         */
        @Override
        public String toString() {
            return this.value == null ? "null" : this.value.toString();
        }

    }

}

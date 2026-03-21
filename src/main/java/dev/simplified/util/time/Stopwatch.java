package dev.sbs.api.util.time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Immutable timing snapshot capturing when an operation started, when it completed,
 * and how long it took.
 * <p>
 * Use {@link #of(Instant)} for live timing (captures {@link Instant#now()} as the
 * completion time) or {@link #of(Instant, Instant)} for pre-existing timestamp pairs
 * (e.g. reconstructed from serialized headers).
 *
 * @see Instant
 * @see Duration
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Stopwatch {

    /** The instant at which the timed operation started. */
    private final @NotNull Instant startedAt;

    /** The instant at which the timed operation completed. */
    private final @NotNull Instant completedAt;

    /** The elapsed time of the operation in nanoseconds. */
    private final long durationNanos;

    /**
     * Returns the elapsed time of the operation in milliseconds.
     *
     * @return the duration in milliseconds
     */
    public long getDurationMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.durationNanos);
    }

    /**
     * Creates a stopwatch that completed at the current instant, measuring the elapsed
     * time from the given start instant.
     *
     * @param startedAt the instant the operation started
     * @return a completed stopwatch
     */
    public static @NotNull Stopwatch of(@NotNull Instant startedAt) {
        return of(startedAt, Instant.now());
    }

    /**
     * Creates a stopwatch from two pre-existing instants, computing the duration
     * as the difference between them.
     *
     * @param startedAt   the instant the operation started
     * @param completedAt the instant the operation completed
     * @return a completed stopwatch
     */
    public static @NotNull Stopwatch of(@NotNull Instant startedAt, @NotNull Instant completedAt) {
        return new Stopwatch(startedAt, completedAt, Duration.between(startedAt, completedAt).toNanos());
    }

}

package dev.simplified.util.time;

import dev.simplified.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * An immutable-time, mutable-format date wrapper around an {@link Instant}.
 *
 * <p>
 * Provides zero-allocation access to date components (year, month, day, hour, minute,
 * second) via a cached {@link ZonedDateTime} that is recomputed only when the
 * {@link #zoneId} changes. Also provides conversions to {@link Calendar}, {@link Date},
 * {@link Instant}, and {@link Timestamp}, as well as ISO-formatted string output.
 *
 * <p>
 * Duration strings can be parsed via {@link #getUnixDuration(String)} using the format
 * {@code 1y2w3d4h5m6s} (years, weeks, days, hours, minutes, seconds).
 */
@Getter
public class SimpleDate {

    private static final @NotNull DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * The immutable point in time this date represents.
     */
    private final @NotNull Instant instant;

    /**
     * The cached zone-resolved date-time, recomputed when {@link #zoneId} changes.
     */
    private transient @NotNull ZonedDateTime zonedDateTime;

    /**
     * The time zone used for component access and formatting.
     * <p>
     * Defaults to America/New_York.
     */
    private transient @NotNull ZoneId zoneId;

    /**
     * The DateTimeFormatter used in {@link #toLocalISO()} and {@link #toString()}.
     * <p>
     * Defaults to {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     */
    @Setter
    private transient @NotNull DateTimeFormatter dateFormat;

    /**
     * Creates a {@link SimpleDate} offset from the current time by the given duration string.
     *
     * @param duration the duration string (e.g. {@code "1d12h30m"})
     * @see #getUnixDuration(String)
     */
    public SimpleDate(@NotNull String duration) {
        this(System.currentTimeMillis() + getUnixDuration(duration));
    }

    /**
     * Creates a {@link SimpleDate} from the given epoch timestamp in milliseconds.
     *
     * @param realTime the epoch timestamp in milliseconds
     */
    public SimpleDate(long realTime) {
        this(Instant.ofEpochMilli(realTime));
    }

    /**
     * Creates a {@link SimpleDate} from the given {@link Instant}.
     *
     * @param instant the instant to wrap
     */
    public SimpleDate(@NotNull Instant instant) {
        this.instant = instant;
        this.dateFormat = DEFAULT_DATE_FORMAT;
        this.zoneId = ZoneId.of("America/New_York");
        this.zonedDateTime = this.instant.atZone(this.zoneId);
    }

    /**
     * Returns the epoch timestamp in milliseconds.
     *
     * @return the epoch timestamp in milliseconds
     */
    public long getRealTime() {
        return this.instant.toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleDate that = (SimpleDate) o;

        return this.getRealTime() == that.getRealTime()
            && Objects.equals(this.getZoneId(), that.getZoneId())
            && Objects.equals(this.getDateFormat(), that.getDateFormat());
    }

    /**
     * Returns the millisecond duration represented by the given duration string.
     * Months are unsupported - use days instead.
     *
     * <p>
     * Valid format: {@code 1y2w3d4h5m6s} - 1 year, 2 weeks, 3 days, 4 hours, 5 minutes, 6 seconds.
     *
     * @param duration the duration string to convert
     * @return the duration in milliseconds
     */
    public static long getUnixDuration(@NotNull String duration) {
        duration = StringUtil.stripToEmpty(duration);
        long durationMillis = 0;
        long component = 0;

        if (StringUtil.isNotEmpty(duration)) {
            for (int i = 0; i < duration.length(); i++) {
                char chr = duration.charAt(i);

                if (Character.isDigit(chr)) {
                    component *= 10;
                    component += chr - '0';
                } else {
                    switch (Character.toLowerCase(chr)) {
                        case 'y':
                            component *= 52;
                        case 'w':
                            component *= 7;
                        case 'd':
                            component *= 24;
                        case 'h':
                            component *= 60;
                        case 'm':
                            component *= 60;
                        case 's':
                            component *= 1000;
                    }

                    durationMillis += component;
                    component = 0;
                }
            }
        }

        return durationMillis;
    }

    /**
     * Returns the day of the month for this date (1-31).
     *
     * @return the day of the month
     */
    public int getDay() {
        return this.zonedDateTime.getDayOfMonth();
    }

    /**
     * Returns the hour of the day for this date (0-23).
     *
     * @return the hour of the day
     */
    public int getHour() {
        return this.zonedDateTime.getHour();
    }

    /**
     * Returns the minute of the hour for this date (0-59).
     *
     * @return the minute of the hour
     */
    public int getMinute() {
        return this.zonedDateTime.getMinute();
    }

    /**
     * Returns the month of the year for this date (1-12).
     *
     * @return the month of the year
     */
    public int getMonth() {
        return this.zonedDateTime.getMonthValue();
    }

    /**
     * Returns the second of the minute for this date (0-59).
     *
     * @return the second of the minute
     */
    public int getSecond() {
        return this.zonedDateTime.getSecond();
    }

    /**
     * Returns the year for this date.
     *
     * @return the year
     */
    public int getYear() {
        return this.zonedDateTime.getYear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getRealTime(), this.getZoneId(), this.getDateFormat());
    }

    /**
     * Sets the time zone used for component access and formatting, resolving the
     * given ID using {@link ZoneId#SHORT_IDS} for short-form aliases.
     *
     * @param timeZoneId the time zone ID string
     */
    public final void setZoneId(@NotNull String timeZoneId) {
        this.setZoneId(ZoneId.of(timeZoneId, ZoneId.SHORT_IDS));
    }

    /**
     * Sets the time zone used for component access and formatting.
     *
     * @param timeZoneId the time zone
     */
    public final void setZoneId(@NotNull ZoneId timeZoneId) {
        this.zoneId = timeZoneId;
        this.zonedDateTime = this.instant.atZone(this.zoneId);
    }

    /**
     * Converts this date to a {@link Calendar} using the configured {@link #zoneId}.
     *
     * @return a new calendar instance
     */
    public final @NotNull Calendar toCalendar() {
        return GregorianCalendar.from(this.zonedDateTime);
    }

    /**
     * Converts this date to a {@link Date}.
     *
     * @return a new date instance
     */
    public final @NotNull Date toDate() {
        return Date.from(this.instant);
    }

    /**
     * Returns the {@link Instant} this date represents.
     *
     * @return the stored instant
     */
    public final @NotNull Instant toInstant() {
        return this.instant;
    }

    /**
     * Formats this date as a string using the configured {@link #dateFormat}
     * and {@link #zoneId}.
     *
     * @return the formatted date string
     */
    public final @NotNull String toLocalISO() {
        return this.getDateFormat()
            .withZone(this.getZoneId())
            .format(this.instant);
    }

    /**
     * Converts this date to a {@link Timestamp}.
     *
     * @return a new timestamp instance
     */
    public final @NotNull Timestamp toTimestamp() {
        return Timestamp.from(this.instant);
    }

    @Override
    public final @NotNull String toString() {
        return this.toLocalISO();
    }

}

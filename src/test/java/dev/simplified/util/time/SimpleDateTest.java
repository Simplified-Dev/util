package dev.simplified.util.time;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SimpleDateTest {

    // 2025-06-15T14:30:45Z
    private static final long KNOWN_EPOCH_MS = 1750000245000L;
    private static final ZoneId UTC = ZoneId.of("UTC");

    @Nested
    class Construction {

        @Test
        void fromEpochMillis_ok() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            assertEquals(KNOWN_EPOCH_MS, date.getRealTime());
            assertNotNull(date.getInstant());
            assertEquals(KNOWN_EPOCH_MS, date.getInstant().toEpochMilli());
        }

        @Test
        void fromInstant_ok() {
            Instant instant = Instant.ofEpochMilli(KNOWN_EPOCH_MS);
            SimpleDate date = new SimpleDate(instant);
            assertEquals(KNOWN_EPOCH_MS, date.getRealTime());
            assertEquals(instant.toEpochMilli(), date.getInstant().toEpochMilli());
        }

        @Test
        void fromDuration_offsetsFromNow() {
            long before = System.currentTimeMillis();
            SimpleDate date = new SimpleDate("1h");
            long after = System.currentTimeMillis();

            long expected1h = 60 * 60 * 1000;
            assertTrue(date.getRealTime() >= before + expected1h);
            assertTrue(date.getRealTime() <= after + expected1h);
        }

        @Test
        void defaultZoneId_isNewYork() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            assertEquals(ZoneId.of("America/New_York"), date.getZoneId());
        }
    }

    @Nested
    class Components {

        @Test
        void componentsMatchZonedDateTime_utc() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId(UTC);

            ZonedDateTime expected = Instant.ofEpochMilli(KNOWN_EPOCH_MS).atZone(UTC);
            assertEquals(expected.getYear(), date.getYear());
            assertEquals(expected.getMonthValue(), date.getMonth());
            assertEquals(expected.getDayOfMonth(), date.getDay());
            assertEquals(expected.getHour(), date.getHour());
            assertEquals(expected.getMinute(), date.getMinute());
            assertEquals(expected.getSecond(), date.getSecond());
        }

        @Test
        void componentsMatchZonedDateTime_defaultZone() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);

            ZonedDateTime expected = Instant.ofEpochMilli(KNOWN_EPOCH_MS).atZone(ZoneId.of("America/New_York"));
            assertEquals(expected.getYear(), date.getYear());
            assertEquals(expected.getMonthValue(), date.getMonth());
            assertEquals(expected.getDayOfMonth(), date.getDay());
            assertEquals(expected.getHour(), date.getHour());
            assertEquals(expected.getMinute(), date.getMinute());
            assertEquals(expected.getSecond(), date.getSecond());
        }

        @Test
        void monthIs1Based() {
            // January 1, 2025 00:00:00 UTC
            SimpleDate date = new SimpleDate(Instant.parse("2025-01-01T00:00:00Z").toEpochMilli());
            date.setZoneId(UTC);
            assertEquals(1, date.getMonth());
        }

        @Test
        void monthIs12ForDecember() {
            // December 15, 2025 00:00:00 UTC
            SimpleDate date = new SimpleDate(Instant.parse("2025-12-15T00:00:00Z").toEpochMilli());
            date.setZoneId(UTC);
            assertEquals(12, date.getMonth());
        }

        @Test
        void componentsUpdateOnZoneChange() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId(UTC);
            int utcHour = date.getHour();

            date.setZoneId(ZoneId.of("Asia/Tokyo")); // UTC+9
            int tokyoHour = date.getHour();

            assertNotEquals(utcHour, tokyoHour);

            ZonedDateTime tokyoExpected = Instant.ofEpochMilli(KNOWN_EPOCH_MS).atZone(ZoneId.of("Asia/Tokyo"));
            assertEquals(tokyoExpected.getHour(), tokyoHour);
        }
    }

    @Nested
    class Conversions {

        @Test
        void toInstant_returnsSameEpoch() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            Instant instant = date.toInstant();
            assertEquals(KNOWN_EPOCH_MS, instant.toEpochMilli());
        }

        @Test
        void toDate_matchesEpoch() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            Date javaDate = date.toDate();
            assertEquals(KNOWN_EPOCH_MS, javaDate.getTime());
        }

        @Test
        void toTimestamp_matchesEpoch() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            Timestamp timestamp = date.toTimestamp();
            assertEquals(KNOWN_EPOCH_MS, timestamp.getTime());
        }

        @Test
        void toCalendar_matchesComponents() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId(UTC);
            Calendar calendar = date.toCalendar();

            assertEquals(date.getYear(), calendar.get(Calendar.YEAR));
            assertEquals(date.getMonth() - 1, calendar.get(Calendar.MONTH)); // Calendar is 0-based
            assertEquals(date.getDay(), calendar.get(Calendar.DAY_OF_MONTH));
            assertEquals(date.getHour(), calendar.get(Calendar.HOUR_OF_DAY));
            assertEquals(date.getMinute(), calendar.get(Calendar.MINUTE));
            assertEquals(date.getSecond(), calendar.get(Calendar.SECOND));
        }

        @Test
        void toLocalISO_producesNonEmptyString() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            String iso = date.toLocalISO();
            assertNotNull(iso);
            assertFalse(iso.isEmpty());
        }

        @Test
        void toString_matchesToLocalISO() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            assertEquals(date.toLocalISO(), date.toString());
        }
    }

    @Nested
    class ZoneHandling {

        @Test
        void setZoneIdByString_ok() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId("UTC");
            assertEquals(ZoneId.of("UTC"), date.getZoneId());
        }

        @Test
        void setZoneIdByShortAlias_ok() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId("EST");
            assertNotNull(date.getZoneId());
        }

        @Test
        void setZoneId_recomputesZonedDateTime() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            date.setZoneId(UTC);
            ZonedDateTime zdt1 = date.getZonedDateTime();

            date.setZoneId(ZoneId.of("Asia/Tokyo"));
            ZonedDateTime zdt2 = date.getZonedDateTime();

            // Same instant, different zone
            assertEquals(zdt1.toInstant(), zdt2.toInstant());
            assertNotEquals(zdt1.getZone(), zdt2.getZone());
        }
    }

    @Nested
    class Duration {

        @Test
        void seconds_ok() {
            assertEquals(30_000, SimpleDate.getUnixDuration("30s"));
        }

        @Test
        void minutes_ok() {
            assertEquals(5 * 60 * 1000, SimpleDate.getUnixDuration("5m"));
        }

        @Test
        void hours_ok() {
            assertEquals(2 * 60 * 60 * 1000, SimpleDate.getUnixDuration("2h"));
        }

        @Test
        void days_ok() {
            assertEquals(3L * 24 * 60 * 60 * 1000, SimpleDate.getUnixDuration("3d"));
        }

        @Test
        void weeks_ok() {
            assertEquals(1L * 7 * 24 * 60 * 60 * 1000, SimpleDate.getUnixDuration("1w"));
        }

        @Test
        void combined_ok() {
            long expected = (1L * 52 * 7 * 24 * 60 * 60 * 1000) // 1y
                + (2L * 7 * 24 * 60 * 60 * 1000)                // 2w
                + (3L * 24 * 60 * 60 * 1000);                   // 3d
            assertEquals(expected, SimpleDate.getUnixDuration("1y2w3d"));
        }

        @Test
        void empty_returnsZero() {
            assertEquals(0, SimpleDate.getUnixDuration(""));
        }
    }

    @Nested
    class EqualsHashCode {

        @Test
        void sameEpochAndZone_areEqual() {
            SimpleDate a = new SimpleDate(KNOWN_EPOCH_MS);
            SimpleDate b = new SimpleDate(KNOWN_EPOCH_MS);
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        void differentEpoch_areNotEqual() {
            SimpleDate a = new SimpleDate(KNOWN_EPOCH_MS);
            SimpleDate b = new SimpleDate(KNOWN_EPOCH_MS + 1000);
            assertNotEquals(a, b);
        }

        @Test
        void differentZone_areNotEqual() {
            SimpleDate a = new SimpleDate(KNOWN_EPOCH_MS);
            SimpleDate b = new SimpleDate(KNOWN_EPOCH_MS);
            b.setZoneId(UTC);
            assertNotEquals(a, b);
        }

        @Test
        void reflexive() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            assertEquals(date, date);
        }

        @Test
        void nullNotEqual() {
            SimpleDate date = new SimpleDate(KNOWN_EPOCH_MS);
            assertNotEquals(null, date);
        }
    }

}

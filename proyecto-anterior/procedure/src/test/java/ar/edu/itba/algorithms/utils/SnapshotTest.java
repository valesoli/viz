package ar.edu.itba.algorithms.utils;

import ar.edu.itba.algorithms.operations.Snapshot;
import ar.edu.itba.algorithms.utils.interval.Interval;
import ar.edu.itba.algorithms.utils.interval.IntervalParser;
import ar.edu.itba.algorithms.utils.time.DateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotTest {

    private Snapshot snapshot;

    @Nested
    @DisplayName("When interval has Year granularity")
    class YearGranularity {

        final Interval interval = IntervalParser.fromString("2014—2019");

        @Test
        void snapshotWithYearGranularity() {
            assertFalse(new Snapshot("2013").shouldApply(interval));
            assertTrue(new Snapshot("2014").shouldApply(interval));
            assertTrue(new Snapshot("2016").shouldApply(interval));
        }

        @Test
        void snapshotWithDateTimeGranularity() {
            LocalDateTime t = LocalDateTime.of(2014, Month.JULY, 29, 19, 30);
            DateTime dateTimeSnapshot = new DateTime(t);

            assertTrue(new Snapshot(dateTimeSnapshot.toString()).shouldApply(interval));
            assertFalse(new Snapshot(new DateTime(t.withYear(2013)).toString()).shouldApply(interval));
        }

        @Test
        void snapshotWithYearMonthGranularity() {
            assertTrue(new Snapshot("2014-01").shouldApply(interval));
            assertTrue(new Snapshot("2016-02").shouldApply(interval));
            assertTrue(new Snapshot("2019-05").shouldApply(interval));
            assertFalse(new Snapshot("2013-05").shouldApply(interval));
        }

        @Test
        void snapshotWithDateGranularity() {
            assertTrue(new Snapshot("2014-01-20").shouldApply(interval));
            assertTrue(new Snapshot("2016-02-15").shouldApply(interval));
            assertTrue(new Snapshot("2019-05-12").shouldApply(interval));
            assertFalse(new Snapshot("2013-05-20").shouldApply(interval));
        }
    }

    @Nested
    @DisplayName("When interval has DateTime granularity")
    class DateTimeGranularity {

        final DateTime start = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30));
        final DateTime end = new DateTime(LocalDateTime.of(2018, Month.JANUARY, 29, 12, 15));
        final Interval interval = new Interval(start, end);

        @Test
        void snapshotWithDateTimeGranularity() {
            DateTime dateTimeSnapshot = new DateTime(LocalDateTime.of(2015, Month.AUGUST, 18, 19, 30));
            assertTrue(new Snapshot(dateTimeSnapshot.toString()).shouldApply(interval));

            dateTimeSnapshot = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30));
            assertTrue(new Snapshot(dateTimeSnapshot.toString()).shouldApply(interval));

            dateTimeSnapshot = new DateTime(LocalDateTime.of(2018, Month.JANUARY, 29, 12, 30));
            assertFalse(new Snapshot(dateTimeSnapshot.toString()).shouldApply(interval));
        }

        @Test
        void snapshotWithDateGranularity() {
            assertFalse(new Snapshot("2015-07-28").shouldApply(interval));
            assertTrue(new Snapshot("2015-07-29").shouldApply(interval));
            assertTrue(new Snapshot("2015-07-30").shouldApply(interval));
        }

        @Test
        void snapshotWithYearGranularity() {
            assertTrue(new Snapshot("2015").shouldApply(interval));
            assertTrue(new Snapshot("2016").shouldApply(interval));
            assertFalse(new Snapshot("2019").shouldApply(interval));
        }

        @Test
        void snapshotWithYearMonthGranularity() {
            assertFalse(new Snapshot("2015-04").shouldApply(interval));
            assertTrue(new Snapshot("2015-07").shouldApply(interval));
            assertTrue(new Snapshot("2016-09").shouldApply(interval));
            assertTrue(new Snapshot("2018-01").shouldApply(interval));
        }
    }
}

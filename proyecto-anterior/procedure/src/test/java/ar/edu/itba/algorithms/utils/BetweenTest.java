package ar.edu.itba.algorithms.utils;

import ar.edu.itba.algorithms.operations.Between;
import ar.edu.itba.algorithms.utils.interval.Interval;
import ar.edu.itba.algorithms.utils.time.Date;
import ar.edu.itba.algorithms.utils.time.DateTime;
import ar.edu.itba.algorithms.utils.time.Year;
import ar.edu.itba.algorithms.utils.time.YearMonth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BetweenTest {

    private Between between;

    @Nested
    @DisplayName("When interval has Year granularity")
    class YearGranularity {

        @BeforeEach
        void setup() {
            final Interval interval = new Interval(Year.parse("2014"), Year.parse("2019"));
            between = new Between(interval);
        }

        @Test
        void queryIntervalWithYearGranularity() {
            Interval queryInterval = new Interval(Year.parse("2015"), Year.parse("2018"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(Year.parse("2011"), Year.parse("2014"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(Year.parse("2012"), Year.parse("2013"));
            assertFalse(between.shouldApply(queryInterval));
        }

        @Test
        void queryIntervalWithDateTimeGranularity() {
            DateTime start = new DateTime(LocalDateTime.of(2015, Month.JULY, 12, 19, 30));
            DateTime end = new DateTime(LocalDateTime.of(2018, Month.JANUARY, 29, 12, 15));
            Interval queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));

            start = new DateTime(LocalDateTime.of(2013, Month.JULY, 12, 19, 30));
            end = new DateTime(LocalDateTime.of(2014, Month.JANUARY, 29, 12, 15));
            queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));
        }

        @Test
        void queryIntervalWithDateGranularity() {
            assertShouldApplyDate(true, "2014-07-12", "2018-01-29");
            assertShouldApplyDate(true, "2015-07-12", "2018-01-29");
            assertShouldApplyDate(true, "2001-07-12", "2014-01-29");
            assertShouldApplyDate(false, "2012-07-12", "2013-01-15");
        }

        private void assertShouldApplyDate(boolean condition, String start, String end) {
            Interval queryInterval = new Interval(Date.parse(start), Date.parse(end));
            assertBoolean(condition, queryInterval);
        }

        @Test
        void queryIntervalWithYearMonthGranularity() {
            assertShouldApplyYearMonth(true, "2014-02", "2015-01");
            assertShouldApplyYearMonth(true, "2015-07", "2018-01");
            assertShouldApplyYearMonth(true, "2013-02", "2014-01");
            assertShouldApplyYearMonth(false, "2012-07", "2013-01");
        }

        private void assertShouldApplyYearMonth(boolean condition, String start, String end) {
            Interval queryInterval = new Interval(YearMonth.parse(start), YearMonth.parse(end));
            assertBoolean(condition, queryInterval);
        }

        private void assertBoolean(boolean condition, Interval queryInterval){
            assertEquals(condition, between.shouldApply(queryInterval));
        }
    }

    @Nested
    @DisplayName("When interval has DateTime granularity")
    class DateTimeGranularity {

        @BeforeEach
        void setup() {
            DateTime start = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30));
            DateTime end = new DateTime(LocalDateTime.of(2018, Month.JANUARY, 29, 12, 15));
            Interval interval = new Interval(start, end);
            between = new Between(interval);
        }

        @Test
        void betweenWithDateTimeGranularity() {
            DateTime start = new DateTime(LocalDateTime.of(2015, Month.AUGUST, 1, 12,0));
            DateTime end = new DateTime(LocalDateTime.of(2016, Month.FEBRUARY, 1, 12, 0));
            Interval queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));

            start = new DateTime(LocalDateTime.of(2012, Month.AUGUST, 1, 12,0));
            end = new DateTime(LocalDateTime.of(2015, Month.JULY, 29, 19, 30));
            queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));
        }

        @Test
        void betweenWithYearGranularity() {
            Interval queryInterval = new Interval(Year.parse("2015"), Year.parse("2016"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(Year.parse("2013"), Year.parse("2015"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(Year.parse("2012"), Year.parse("2014"));
            assertFalse(between.shouldApply(queryInterval));
        }

        @Test
        void betweenWithYearMonthGranularity() {
            Interval queryInterval = new Interval(YearMonth.parse("2015-01"), YearMonth.parse("2016-05"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(YearMonth.parse("2012-04"), YearMonth.parse("2015-07"));
            assertTrue(between.shouldApply(queryInterval));

            queryInterval = new Interval(YearMonth.parse("2012-04"), YearMonth.parse("2013-06"));
            assertFalse(between.shouldApply(queryInterval));
        }

        @Test
        void betweenWithDateGranularity() {
            Date start = new Date(LocalDate.of(2015, Month.AUGUST, 1));
            Date end = new Date(LocalDate.of(2016, Month.FEBRUARY, 1));
            Interval queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));

            start = new Date(LocalDate.of(2002, Month.AUGUST, 1));
            end = new Date(LocalDate.of(2015, Month.JULY, 29));
            queryInterval = new Interval(start, end);
            assertTrue(between.shouldApply(queryInterval));

            start = new Date(LocalDate.of(2002, Month.AUGUST, 1));
            end = new Date(LocalDate.of(2015, Month.MARCH, 29));
            queryInterval = new Interval(start, end);
            assertFalse(between.shouldApply(queryInterval));
        }
    }
}

package ar.edu.itba.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void getQuotedString() {
        String quoted = "'Quoted'";
        assertEquals(Utils.getQuotedString(quoted), "Quoted");
    }

    @Test
    public void randomConsecutiveTrivial() {
        List<Integer> consecutive = Utils.randomConsecutive(1, 2, 1);
        Integer number = consecutive.get(0);
        assertEquals(1, consecutive.size());
        assertEquals(Integer.valueOf(1), number);
    }

    @Test
    public void twoRandomConsecutive() {
        List<Integer> consecutive = Utils.randomConsecutive(1, 5, 2);
        assertEquals(2, consecutive.size());
        assertTrue(consecutive.get(0) < consecutive.get(1));
    }

    @Test
    public void threeRandomConsecutive() {
        List<Integer> consecutive = Utils.randomConsecutive(1, 10, 3);
        assertEquals(3, consecutive.size());
        assertTrue(consecutive.get(0) < consecutive.get(1));
        assertTrue(consecutive.get(1) < consecutive.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomConsecutiveStartBiggerThanEnd() {
        Utils.randomConsecutive(5, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomConsecutiveIllegalTotalNumbers() {
        Utils.randomConsecutive(1, 3, 3);
    }
}
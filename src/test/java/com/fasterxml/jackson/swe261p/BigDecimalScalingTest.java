package com.fasterxml.jackson.swe261p;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.io.BigDecimalParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BigDecimalScalingTest {

    BigDecimalParser parser = new BigDecimalParser();
    BigDecimalParser.AdjustScale as = parser.new AdjustScale();
    long exp_min = Integer.MIN_VALUE, exp_max = Integer.MAX_VALUE;

    @Test
    public void testAdjustScale() {
        //general case
        assertEquals(Integer.MAX_VALUE, as._adjustScale(-1, exp_min));
        assertEquals(Integer.MIN_VALUE, as._adjustScale(-1, exp_max));
    }

    @Test(expected = NumberFormatException.class)
    public void testAdjustScaleUnderflow() {
        as._adjustScale(-2, exp_max);
    }

    @Test(expected = NumberFormatException.class)
    public void testAdjustScaleOverflow() {
        as._adjustScale(0, exp_min);
    }
}

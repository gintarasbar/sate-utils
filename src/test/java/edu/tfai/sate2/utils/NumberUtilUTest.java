package edu.tfai.sate2.utils;

import org.junit.Test;

import static edu.tfai.sate2.utils.NumberUtil.stringIsNan;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class NumberUtilUTest {

    @Test
    public void testStringIsNan() throws Exception {
        boolean value = stringIsNan("0.25656");
        assertThat(value, is(false));

        value = stringIsNan("NaN");
        assertThat(value, is(true));

        value = stringIsNan("nan");
        assertThat(value, is(true));
    }

    @Test
    public void testFormat() throws Exception {

    }

    @Test
    public void testFormatWithNull() throws Exception {

    }

    @Test
    public void testFormat1() throws Exception {

    }

    @Test
    public void testExpFormat() throws Exception {

    }

    @Test
    public void testFormatInt() throws Exception {

    }

    @Test
    public void testConvertToRomain() throws Exception {

    }

    @Test
    public void testConvertFromRomain() throws Exception {

    }

    @Test
    public void testGetGamrad() throws Exception {

    }

    @Test
    public void testParse() throws Exception {

    }

    @Test
    public void testParseInteger() throws Exception {

    }

    @Test
    public void testTryParseFloat() throws Exception {

    }

    @Test
    public void testTryParseDouble() throws Exception {

    }

    @Test
    public void testNextLong() throws Exception {

    }
}
package edu.tfai.sate2.utils;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RandomUtilsUTest {

    @Test
    public void testStringWithLength() throws Exception {
        assertThat(RandomUtils.string(10).length(),is(10));
        assertThat(RandomUtils.string(100).length(),is(100));
        assertThat(RandomUtils.string(255).length(),is(255));
    }
}
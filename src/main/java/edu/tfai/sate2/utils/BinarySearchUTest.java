package edu.tfai.sate2.utils;

import edu.tfai.sate2.exceptions.ProfileException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BinarySearchUTest {

    private double x[] = {1, 2, 3, 4, 5, 6, 7, 8};

    @Test
    public void testSearch() {

        int result = BinarySearch.search(x, 1.1);
        Assert.assertThat(result, CoreMatchers.is(0));

        result = BinarySearch.search(x, 1.0);
        Assert.assertThat(result, CoreMatchers.is(0));

        result = BinarySearch.search(x, 6.9);
        Assert.assertThat(result, CoreMatchers.is(6));

        result = BinarySearch.search(x, 8.0);
        Assert.assertThat(result, CoreMatchers.is(7));

        try {
            BinarySearch.search(x, 8.1);
            Assert.fail();
        } catch (ProfileException e) {

        }

        try {
            BinarySearch.search(x, 0.9);
            Assert.fail();
        } catch (ProfileException e) {

        }

    }

}
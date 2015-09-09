package edu.tfai.sate2.utils;

import edu.tfai.sate2.exceptions.ProfileException;
import edu.tfai.sate2.exceptions.SpectraOutOfRangeBinary;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BinarySearchUTest {

    private double x[] = {1, 2, 3, 4, 5, 6, 7, 8};

    @Test
    public void testSearch() {

        int result = BinarySearch.search(x, 1.1);
        assertThat(result, is(0));

        result = BinarySearch.search(x, 1.0);
        assertThat(result, is(0));

        result = BinarySearch.search(x, 6.9);
        assertThat(result, is(6));

        result = BinarySearch.search(x, 8.0);
        assertThat(result, is(7));

        try {
            BinarySearch.search(x, 8.1);
            fail();
        } catch (SpectraOutOfRangeBinary e) {

        }

        try {
            BinarySearch.search(x, 0.9);
            fail();
        } catch (SpectraOutOfRangeBinary e) {

        }

    }

}
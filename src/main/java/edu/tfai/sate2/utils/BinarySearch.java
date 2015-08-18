package edu.tfai.sate2.utils;

import edu.tfai.sate2.exceptions.SpectraOutOfRangeBinary;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static java.lang.String.format;

@Slf4j
public class BinarySearch {

    public static int search(double[] x, double searchValue) {
        if (x[0] > searchValue || x[x.length - 1] < searchValue) {
            //handle when value is outside
//            if (x.length > 0) {
//                String message = format("Binary search adjusted. SearchVal=%2.4f with range [%.3f,%.3f], count=%s", searchValue, x[0], x[x.length - 1], x.length);
//                log.warn(message);
//                if (searchValue < x[0]) {
//                    return 0;
//                } else if (searchValue > x[x.length - 1]) {
//                    return x.length - 1;
//                }
//            }
//            String message = format("No binary search match found for %2.4f with range [%.3f,%.3f], count=%s", searchValue, x[0], x[x.length - 1], x.length);
            throw new SpectraOutOfRangeBinary(searchValue, x[0], x[x.length - 1]);
        }

        int pos = Arrays.binarySearch(x, searchValue);
        Integer newPos = null;
        if (pos < 0) {
            pos = -(pos) - 1;
            if (pos <= 0 || pos >= x.length) {
                // Do nothing. This point is outside the array bounds return value will be null
            } else {
                // Find nearest point
                double d0 = Math.abs(x[pos - 1] - searchValue);
                double d1 = Math.abs(x[pos] - searchValue);
                newPos = (d0 <= d1) ? pos - 1 : pos;
            }
        } else {
            newPos = pos;
        }
        return newPos;
    }
}

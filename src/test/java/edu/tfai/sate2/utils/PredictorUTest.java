package edu.tfai.sate2.utils;

import com.google.common.base.Optional;
import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;

public class PredictorUTest {

    @Test
    public void testPredictZeroPrecision() throws Exception {
        ArrayList<Double> x = newArrayList(1.0, 4.0, 5.0, 2.0);
        ArrayList<Double> y = newArrayList(1.0, 2.0, 4.0, 5.0);
        Predictor predictor = new Predictor(x, y, 1);
        Optional<Double> predict = predictor.predict(3.111, 0);
        assertThat(predict.get(), closeTo(3.0, 0.001));
    }


    @Test
    public void testPredictPrecisionTwoPlaces() throws Exception {
        ArrayList<Double> x = newArrayList(1.0, 2.0, 5.0, 4.0);
        ArrayList<Double> y = newArrayList(1.111, 2.222, 4.444, 5.555);
        Predictor predictor = new Predictor(x, y, 1);
        Optional<Double> predict = predictor.predict(3.0, 2);
        assertThat(predict.get(), closeTo(3.33, 0.001));
    }
}
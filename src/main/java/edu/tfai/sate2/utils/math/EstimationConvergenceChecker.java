//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package edu.tfai.sate2.utils.math;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;

public class EstimationConvergenceChecker extends AbstractConvergenceChecker<UnivariatePointValuePair> {
    private static final int ITERATION_CHECK_DISABLED = -1;
    private final int maxIterationCount;

    public EstimationConvergenceChecker(double relativeThreshold, double absoluteThreshold) {
        super(relativeThreshold, absoluteThreshold);
        this.maxIterationCount = -1;
    }

    public EstimationConvergenceChecker(double relativeThreshold, double absoluteThreshold, int maxIter) {
        super(relativeThreshold, absoluteThreshold);
        if (maxIter <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIter));
        } else {
            this.maxIterationCount = maxIter;
        }
    }

    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        if (this.maxIterationCount != -1 && iteration >= this.maxIterationCount) {
            return true;
        } else {
            double p = previous.getValue();
            double c = current.getValue();
            double difference = FastMath.abs(p - c);
            double size = FastMath.max(FastMath.abs(p), FastMath.abs(c));
            double minValue = FastMath.min(FastMath.abs(p), FastMath.abs(c));
            return difference <= size * this.getRelativeThreshold() || minValue <= this.getAbsoluteThreshold();
        }
    }
}

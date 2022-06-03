package math;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Arrays;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class InterpolatedPolyEvaluatorTest extends TestCase {

    public void testEvaluate() {
        final long[] x = {0, 1, 2};
        final long[] y = {0, 1, 4}; // y = x^2
        final InterpolatedPolyEvaluator evaluator = new InterpolatedPolyEvaluator(x,
                                                                                  y,
                                                                                  10,
                                                                                  new ModuloCalculator(1_000_000_007));
        final long actualResult = evaluator.evaluate();
        Assert.assertEquals(100L, actualResult);
    }
}
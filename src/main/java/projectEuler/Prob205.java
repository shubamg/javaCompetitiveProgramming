package projectEuler;

import math.ModuloCalculator;
import math.PolynomialCalculator;

import java.util.Arrays;

public class Prob205 {
    private static final long[] _4_ONES = {1L, 1L, 1L, 1L, 0L};
    private static final long[] _6_ONES = {1L, 1L, 1L, 1L, 1L, 1L, 0L};
    private static final long TOTAL_NUM_WAYS = 12230590464L;
    private final PolynomialCalculator polyCal;

    private Prob205(final PolynomialCalculator polyCal) {
        this.polyCal = polyCal;
    }

    private static void print(final Iterable<long[]> longArrays) {
        longArrays.forEach(array -> System.out.println(Arrays.toString(array)));
    }

    private static long[] cumulativeSum(final long[] arr) {
        final long[] result = new long[arr.length];
        Arrays.fill(result, 0);
        result[arr.length - 1] = arr[arr.length - 1];
        for (int i = arr.length - 2; i >= 0; i--) {
            result[i] = result[i + 1] + arr[i];
        }
        return result;
    }

    public static void main(String[] args) {
        final PolynomialCalculator polyCal = new PolynomialCalculator(ModuloCalculator.getWithoutMod());
        final Prob205 solution = new Prob205(polyCal);
        System.out.println(solution.solve());
    }

    private double solve() {
        final long[] pyramid = polyCal.getAllPowersTill(_4_ONES, 9).get(9);
        final long[] hexaCumulative = cumulativeSum(polyCal.getAllPowersTill(_6_ONES, 6).get(6));
        long numWays = 0L;
        for (int i = 0; i < pyramid.length - 2; i++) {
            numWays += (pyramid[i] * hexaCumulative[i + 1]);
        }
        return numWays * 1.0 / TOTAL_NUM_WAYS;
    }
}

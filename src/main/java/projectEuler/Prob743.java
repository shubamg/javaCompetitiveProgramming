package projectEuler;

import com.google.common.base.Preconditions;
import math.ModuloCalculator;

import java.util.Arrays;

public class Prob743 {
    public static final int MAX_LINES_TO_PRINT = 1_000_000;
    public static final int STAGE_COUNT = 8;
    private final int k;
    private final long N;
    private final ModuloCalculator moduloCalculator;
    private final long[] overAllTime;

    private Prob743(final int k, final long n, final ModuloCalculator moduloCalculator) {
        Preconditions.checkArgument(n % k == 0);
        this.k = k;
        this.N = n;
        this.moduloCalculator = moduloCalculator;
        overAllTime = new long[STAGE_COUNT];
    }

    public static void main(String[] args) {
        final Prob743 prob743 = new Prob743(100_000_000, 10_000_000_000_000_000L, new ModuloCalculator(1000_000_007));
        System.out.println(prob743.solve());
    }

    private long solve() {
        final long nByK = N / k;
        final long powBase = moduloCalculator.power(2, nByK);
        final long printGap = k / MAX_LINES_TO_PRINT;
        long res = 0;
        long factorialOfT = 1L;
        final long factorialOfK = getFactorial(k);
        long inverseFactorialOfOnes = moduloCalculator.getInverse(factorialOfK);
        for (int t = 0; 2 * t <= k; t++) {
            final long[] currentIterTs = new long[STAGE_COUNT + 1];
            Arrays.fill(currentIterTs, 0L);
            final int ones = k - 2 * t;

            currentIterTs[0] = System.nanoTime();
            if (t > 0) {
                factorialOfT = moduloCalculator.multiply(factorialOfT, t);
                currentIterTs[1] = System.nanoTime();
                inverseFactorialOfOnes = moduloCalculator.multiply(inverseFactorialOfOnes,
                                                                   moduloCalculator.multiply(k - 2L * t + 1,
                                                                                             k - 2L * t + 2));
                currentIterTs[2] = System.nanoTime();
            } else {
                currentIterTs[1] = currentIterTs[0];
                currentIterTs[2] = currentIterTs[0];
            }

            final long invFactorialOfT = moduloCalculator.getInverse(factorialOfT);
            currentIterTs[3] = System.nanoTime();

            final long powOf2 = moduloCalculator.power(powBase, ones);
            currentIterTs[4] = System.nanoTime();

            final long termA = moduloCalculator.multiply(invFactorialOfT, invFactorialOfT);
            currentIterTs[5] = System.nanoTime();

            final long termB = moduloCalculator.multiply(powOf2, inverseFactorialOfOnes);
            currentIterTs[6] = System.nanoTime();

            long toAdd = moduloCalculator.multiply(termA, termB);
            currentIterTs[7] = System.nanoTime();

            res = moduloCalculator.add(res, toAdd);
            currentIterTs[8] = System.nanoTime();

            updateTimeStats(currentIterTs);
            if (printGap == 0 || t % printGap == 0) {
                System.out.printf("t=%d, factorialOfT = %d, res without factorial of k = %d, timeStats = %s\n",
                                  t,
                                  factorialOfT,
                                  res,
                                  Arrays.toString(overAllTime));
            }
        }
        return moduloCalculator.multiply(res, factorialOfK);
    }

    private void updateTimeStats(final long[] currentIterTs) {
        for (int i = 0; i < STAGE_COUNT; i++) {
            overAllTime[i] += (currentIterTs[i + 1] - currentIterTs[i]);
        }
    }

    private long getFactorial(final long x) {
        long res = 1;
        for (long i = 2; i <= x; i++) {
            res = moduloCalculator.multiply(res, i);
        }
        return res;
    }
}

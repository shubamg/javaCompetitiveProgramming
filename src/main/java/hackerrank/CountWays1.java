package hackerrank;

import com.google.common.collect.Lists;
import math.ModuloCalculator;
import math.PolynomialCalculator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class CountWays1 {
    private final int commonMultiple;
    private final List<Integer> coins;
    private final long l;
    private final long r;
    private final int D;
    private final int maxDpAmount;
    private final ModuloCalculator moduloCalculator;
    private final PolynomialCalculator polynomialCalculator;

    private CountWays1(final List<Integer> coins,
                       final int commonMultiple,
                       final long l,
                       final long r,
                       final ModuloCalculator moduloCalculator, final PolynomialCalculator polynomialCalculator) {
        this.commonMultiple = commonMultiple;
        this.coins = coins;
        this.l = l;
        this.r = r;
        this.D = coins.size();
        this.polynomialCalculator = polynomialCalculator;
        this.maxDpAmount = this.commonMultiple * (this.D + 1);
        this.moduloCalculator = moduloCalculator;
    }

    private long solve() {
        final long[] dpState = getFinalDpState();
        long runningSum = 0L;
        for (int reminder = 0; reminder < commonMultiple; reminder++) {
            final long reminderContribution = getReminderContribution(dpState, reminder);
            runningSum = moduloCalculator.add(runningSum, reminderContribution);
        }
        return runningSum;
    }

    private long getReminderContribution(final long[] dpState, final int reminder) {
        final long[] poly = getSumPoly(dpState, reminder);
        /*
        commonMultiple * higherQIncl + reminder <= r
        higherQIncl <= (r - reminder) / commonMultiple
         */
        final long higherQIncl = (r - reminder) / commonMultiple;
        final long lowerQExcl = ((l - reminder) / commonMultiple) - 1;
        if (lowerQExcl < 0) {
            return polynomialCalculator.evaluateAt(poly, higherQIncl);
        } else {
            final long higherInclEval = polynomialCalculator.evaluateAt(poly, higherQIncl);
            final long lowerExclEval = polynomialCalculator.evaluateAt(poly, lowerQExcl);
            return moduloCalculator.subtract(higherInclEval, lowerExclEval);
        }
    }

    private long[] getSumPoly(final long[] dpState, final int reminder) {
        final long[] x = new long[D + 1];
        final long[] y = new long[D + 1];
        for (int q = 0; q <= D; q++) {
            x[q] = q;
            final int denomination = q * commonMultiple + reminder;
            y[q] = dpState[denomination];
        }
        return polynomialCalculator.doLagarangeInterpolation(x, y);
    }

    private long[] getFinalDpState() {
        long[] state = getInitDpState();
        for (int i = 0; i < D; i++) {
            state = getNextDpState(coins.get(i), state);
        }
        return state;
    }

    private long[] getInitDpState() {
        final long[] initDpState = new long[maxDpAmount];
        Arrays.fill(initDpState, 0L);
        initDpState[0] = 1;
        return initDpState;
    }

    private long[] getNextDpState(final int coin, final long[] prevState) {
        assert prevState.length == maxDpAmount;
        final long[] newState = prevState.clone();
        for (int i = coin; i < maxDpAmount; i++) {
            newState[i] = moduloCalculator.add(newState[i], newState[i - coin]);
        }
        return newState;
    }

    public static int countWays(List<Integer> arr, long l, long r) {
        final ModuloCalculator moduloCalculator = new ModuloCalculator(1_000_000_007L);
        final PolynomialCalculator polynomialCalculator = new PolynomialCalculator(moduloCalculator);
        final CountWays1 solver = new CountWays1(arr, getCommonMultiple(arr), l, r, moduloCalculator, polynomialCalculator);
        return (int) solver.solve();
    }

    private static int getCommonMultiple(final List<Integer> arr) {
        assert arr.size() > 0;
        return new HashSet<>(arr).stream().mapToInt(e -> e).reduce((a, b) -> a * b).getAsInt();
    }

    public static void main(String[] args) {
        System.out.println(countWays(Lists.newArrayList(1, 2, 3), 1, 6));
    }
}

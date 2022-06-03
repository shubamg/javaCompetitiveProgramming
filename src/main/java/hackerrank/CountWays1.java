package hackerrank;

import com.google.common.collect.Lists;
import math.InterpolatedPolyEvaluator;
import math.ModuloCalculator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.LongStream;

/**
 * Created by Shubham Gupta
 * on 03 Jun 2022.
 */
public class CountWays1 {
    private static final boolean DEBUG = true;
    private final int commonMultiple;
    private final List<Integer> coins;
    private final long l;
    private final long r;
    private final int D;
    private final int maxDpAmount;
    private final ModuloCalculator moduloCalculator;

    private CountWays1(final List<Integer> coins,
                       final int commonMultiple,
                       final long l,
                       final long r,
                       final ModuloCalculator moduloCalculator) {
        this.commonMultiple = commonMultiple;
        this.coins = coins;
        this.l = l;
        this.r = r;
        this.D = coins.size();
        this.maxDpAmount = this.commonMultiple * (this.D + 1);
        this.moduloCalculator = moduloCalculator;
        printf("commonMultiple=%d, coins=%s, l=%d, r=%d, D=%d%n", commonMultiple, coins, l, r, D);
    }

    private long solve() {
        final long[] dpState = getDpState();
        for (int i = 0; i < maxDpAmount; i++) {
            printf("%d -> %d%n", i, dpState[i]);
        }
        long runningSum = 0L;
        for (int reminder = 0; reminder < commonMultiple; reminder++) {
            final long reminderContribution = getReminderContribution(dpState, reminder);
            printf("For reminder=%d, contribution=%d%n", reminder, reminderContribution);
            runningSum = moduloCalculator.add(runningSum, reminderContribution);
        }
        return runningSum;
    }

    private long getReminderContribution(final long[] dpState, final int reminder) {
        /*
        commonMultiple * higherQIncl + reminder <= r
        higherQIncl <= (r - reminder) / commonMultiple
        */
        final long higherQIncl = (r - reminder) / commonMultiple;
        final long[] y = getYForInterpolation(dpState, reminder);
        final long[] q = LongStream.range(0, D + 1L).toArray();
        if (l - reminder - 1 < 0) {
            printf("For reminder %d, lowerQExcl is -ve, higherQIncl=%d%n", reminder, higherQIncl);
            return new InterpolatedPolyEvaluator(q, y, higherQIncl, moduloCalculator).evaluate();
        }
        final long lowerQExcl = (l - reminder - 1) / commonMultiple;
        printf("For reminder %d, lowerQExcl=%d, higherQIncl=%d%n", reminder, lowerQExcl, higherQIncl);
        final long higherInclEval = new InterpolatedPolyEvaluator(q, y, higherQIncl, moduloCalculator).evaluate();
        final long lowerExclEval = new InterpolatedPolyEvaluator(q, y, lowerQExcl, moduloCalculator).evaluate();
        return moduloCalculator.subtract(higherInclEval, lowerExclEval);
    }

    private long[] getYForInterpolation(final long[] dpState, final int reminder) {
        final long[] y = new long[D + 1];
        y[0] = dpState[reminder];
        for (int q = 1; q <= D; q++) {
            final int index = q * commonMultiple + reminder;
            y[q] = moduloCalculator.add(dpState[index], y[q - 1]);
        }
        return y;
    }

    private long[] getDpState() {
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
        final CountWays1 solver = new CountWays1(arr, getCommonMultiple(arr), l, r, moduloCalculator);
        return (int) solver.solve();
    }

    private static int getCommonMultiple(final List<Integer> arr) {
        assert arr.size() > 0;
        return new HashSet<>(arr).stream().mapToInt(e -> e).reduce((a, b) -> a * b).getAsInt();
    }

    private void printf(String format, Object... args) {
        if (DEBUG) {
            System.out.printf(format, args);
        }
    }

    public static void main(String[] args) {
        System.out.println(countWays(Lists.newArrayList(1, 2, 3), 1, 6));
    }
}

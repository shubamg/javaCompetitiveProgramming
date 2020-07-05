package leetcode;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/probability-of-a-two-boxes-having-the-same-number-of-distinct-balls/">Problem link</a>
 */
public class _1467 {
    private final int[] balls;
    private final int totalBalls;
    private final int numColors;

    public _1467(final int[] balls) {
        this.balls = balls;
        this.totalBalls = Arrays.stream(balls).sum();
        numColors = balls.length;
    }

    double solve() {
        final double numerator = getNumerator(new int[balls.length], 0);
        final double denominator = getNumArrangements(balls);
        return numerator / denominator;
    }

    private double getNumerator(final int[] ballsInFirstBox, final int colorToProcess) {
        if (colorToProcess == numColors) {
            return getBaseCaseNumerator(ballsInFirstBox);
        }
        double numWays = 0f;
        for (int i = 0; i <= balls[colorToProcess]; i++) {
            // i is number of balls of colorToProcess going in first box
            ballsInFirstBox[colorToProcess] = i;
            numWays += getNumerator(ballsInFirstBox, colorToProcess + 1);
            ballsInFirstBox[colorToProcess] = 0;
        }
        return numWays;
    }

    private double getBaseCaseNumerator(final int[] ballsInFirstBox) {
        final int[] ballsInSecondBox = subtractArrays(balls, ballsInFirstBox);
        final int c1 = countNonZeroMembers(ballsInFirstBox);
        final int c2 = countNonZeroMembers(ballsInSecondBox);
        if (c1 != c2) {
            return 0;
        }
        final int sum1 = Arrays.stream(ballsInFirstBox).sum();
        if (sum1 << 1 != totalBalls) {
            return 0;
        }
        final int sum2 = totalBalls - sum1;
        final double denominator = getDenominator(ballsInFirstBox, ballsInSecondBox);
        return (getLargeFactorial(sum1) * getLargeFactorial(sum2)) / denominator;
    }

    private double getDenominator(final int[] ballsInFirstBox, final int[] ballsInSecondBox) {
        assert ballsInFirstBox.length == ballsInSecondBox.length;
        double denominator = 1;
        for (int i = 0; i < ballsInFirstBox.length; i++) {
            denominator *= getAccurateFactorial(ballsInFirstBox[i]);
            denominator *= getAccurateFactorial(ballsInSecondBox[i]);
        }
        return denominator;
    }

    private static double getNumArrangements(final int[] balls) {
        long denominator = 1;
        int sum = 0;
        for (final int ball : balls) {
            denominator *= getAccurateFactorial(ball);
            sum += ball;
        }
        return getLargeFactorial(sum) / denominator;
    }

    private static int[] subtractArrays(final int[] big, final int[] small) {
        final int[] result = new int[big.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = big[i] - small[i];
        }
        return result;
    }

    private static int countNonZeroMembers(final int[] arr) {
        int result = 0;
        for (final int value : arr) {
            if (value == 0) {
                result++;
            }
        }
        return result;
    }

    private static long getAccurateFactorial(final int n) {
        assert n <= 20 && n >= 0;
        return n == 0 ? 1 : n * getAccurateFactorial(n - 1);
    }

    private static double getLargeFactorial(final int n) {
        return n == 0 ? 1 : n * getLargeFactorial(n - 1);
    }

    public static void main(String[] args) {
        final int[] input = {6, 6, 6, 6, 6, 6};
        final _1467 solver = new _1467(input);
        System.out.println(solver.solve());
    }
}

package codeForces;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class _1032C {

    private final int[] A;
    private final int N;
    /**
     * B[i][j] = -1 if it is not possible to have a assignment of fingers to A[0..i] such that A[i] is mapped to j.
     * B[i][j] = k if there exists a assignment of fingers to A[0..i] such that A[i] is mapped to j and A[i-1] is
     * mapped to k.
     * B[i][j] = 0 if i = 0
     */
    private final int[][] B;
    private static final int NUM_FINGERS = 5;
    private static final int NO_SOLUTION = -1;

    private _1032C(final int[] a) {
        A = a;
        N = A.length;
        B = new int[N][NUM_FINGERS];
    }

    private boolean isSolvable() {
        for (int j = 0; j < NUM_FINGERS; j++) {
            B[0][j] = 0;
        }
        for (int i = 1; i < N; i++) {
            if (A[i - 1] < A[i]) {
                if (!calculateHigherSolution(i)) {
                    return false;
                }
                continue;
            }
            if (A[i - 1] > A[i]) {
                if (!calculateLowerSolution(i)) {
                    return false;
                }
                continue;
            }
            if (!calculateUnequalSolution(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean calculateUnequalSolution(final int i) {
        final int smallestFinger = getSmallestFinger(B[i - 1]);
        if (smallestFinger == NO_SOLUTION) {
            return false;
        }
        for (int j = 0; j < NUM_FINGERS; j++) {
            if (j != smallestFinger) {
                B[i][j] = smallestFinger;
            }
        }
        final int largestFinger = getLargestFinger(B[i - 1]);
        B[i][smallestFinger] = largestFinger != smallestFinger ? largestFinger : NO_SOLUTION;
        return true;
    }

    private boolean calculateHigherSolution(final int i) {
        final int smallestFinger = getSmallestFinger(B[i - 1]);
        if (smallestFinger == NO_SOLUTION) {
            return false;
        }
        for (int j = 0; j <= smallestFinger; j++) {
            B[i][j] = NO_SOLUTION;
        }
        for (int j = smallestFinger + 1; j < NUM_FINGERS; j++) {
            B[i][j] = smallestFinger;
        }
        return true;
    }

    private boolean calculateLowerSolution(final int i) {
        final int largestFinger = getLargestFinger(B[i - 1]);
        if (largestFinger == NO_SOLUTION) {
            return false;
        }
        for (int j = 0; j < largestFinger; j++) {
            B[i][j] = largestFinger;
        }
        for (int j = largestFinger; j < NUM_FINGERS; j++) {
            B[i][j] = NO_SOLUTION;
        }
        return true;
    }

    private int getSmallestFinger(int[] arr) {
        for (int j = 0; j < NUM_FINGERS; j++) {
            if (arr[j] != NO_SOLUTION) {
                return j;
            }
        }
        return NO_SOLUTION;
    }

    private int getLargestFinger(int[] arr) {
        for (int j = NUM_FINGERS - 1; j >= 0; j--) {
            if (arr[j] != NO_SOLUTION) {
                return j;
            }
        }
        return NO_SOLUTION;
    }

    private int[] getSolution() {
        int[] solution = new int[N];
        int previousIndexFinger = getLargestFinger(B[N-1]);
        for (int i = N-1; i >= 0; i --) {
            solution[i] = previousIndexFinger;
            previousIndexFinger = B[i][solution[i]];
        }
        for (int i = 0; i < N; i++) {
            solution[i]++;
        }
        return solution;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int N = reader.nextInt();
        final int[] A = new int[N];
        for (int i = 0; i < N; i++) {
            A[i] = reader.nextInt();
        }
        final _1032C solver = new _1032C(A);
        final boolean isSolvable = solver.isSolvable();
        if (!isSolvable) {
            System.out.println(-1);
            return;
        }
        final int[] solution = solver.getSolution();
        String result = Arrays.stream(solution)
                              .mapToObj(String::valueOf)
                              .collect(Collectors.joining(" "));
        System.out.println(result);
    }
}

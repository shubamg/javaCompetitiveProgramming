package codeJam._2021;

import io.InputReader;
import math.MatrixCalculator;
import math.ModuloCalculator;
import util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham Gupta
 * on 15 Apr 2022.
 */
public class InfiniTree {
    private static final boolean DEBUG = true;
    final int N;
    final long A;
    final long B;
    final long[][] multiplierMatrix;
    final long[] initState;
    final MatrixCalculator matrixCalculator;

    public InfiniTree(final int n, final long a, final long b, final List<Pair<Integer, Integer>> childNodePairs) {
        assert a == 1;
        N = n;
        A = a;
        B = b;
        this.multiplierMatrix = MatrixCalculator.getZeroMatrix(2 * (N + 1));
        this.initState = new long[2 * (N + 1)];
        populateMultiplierMatrix(childNodePairs);
        populateInitState();
        matrixCalculator = new MatrixCalculator(ModuloCalculator.getWithoutMod());
    }

    private long solve() {
        dprinf("Multiplier matrix %s%n", Arrays.deepToString(multiplierMatrix));
        final long upperBoundOnExpo = getUpperLimit();
        dprinf("Upperlimit = %d%n", upperBoundOnExpo);
        final long resultExpo = binSearch(upperBoundOnExpo);
        return resultExpo - 1;
    }

    private long binSearch(long upperBoundOnExpo) {
        long lowerBoundOnExpo = upperBoundOnExpo / 2 + 1;
        while(lowerBoundOnExpo < upperBoundOnExpo) {
            long middle = (lowerBoundOnExpo + upperBoundOnExpo) / 2;
            final long[][] matrixPower = matrixCalculator.power(multiplierMatrix, middle);
            final long total = getTotal(matrixPower);
            if (total < B) {
                lowerBoundOnExpo = middle + 1;
            } else{
                upperBoundOnExpo = middle;
            }
        }
        return lowerBoundOnExpo;
    }

    private long getUpperLimit() {
        long[][] matrixPower = multiplierMatrix;
        long expo = 1;
        while(true) {
            final long total = getTotal(matrixPower);
            dprinf("expo = %d, total = %d, matrix=%s%n", expo, total, Arrays.deepToString(matrixPower));
            if (total >= B) {
                break;
            }
            matrixPower = matrixCalculator.power(matrixPower, 2);
            expo *= 2;
        }
        return expo;
    }

    private long getTotal(final long[][] matrixPower) {
        long sum = 0L;
        for (int i = 0; i <= N; i++) {
            sum += matrixPower[i + (N + 1)][1];
        }
        return sum;
    }

    private void populateInitState() {
        Arrays.fill(initState, 0);
        initState[1] = 1;
    }

    private void populateMultiplierMatrix(final List<Pair<Integer, Integer>> childNodePairs) {
        populateTopLeft(childNodePairs);
        populateBottom();
    }

    private void populateBottom() {
        for (int i = 0; i <= N; i++) {
            final int row = i + (N + 1);
            multiplierMatrix[row][i] = 1L;
            multiplierMatrix[row][row] = 1L;
        }
    }

    private void populateTopLeft(final List<Pair<Integer, Integer>> childNodePairs) {
        int parent = 1;
        for (final Pair<Integer, Integer> childNodePair : childNodePairs) {
            // 0 is leaf (White) and 1 is root
            final int leftNode = childNodePair.getKey();
            final int rightNode = childNodePair.getValue();
            dprinf("parent=%d, leftNode=%d, rightNode=%d%n", parent, leftNode, rightNode);
            multiplierMatrix[leftNode][parent] += 1;
            multiplierMatrix[rightNode][parent] += 1;
            parent++;
        }
    }

    public static void main(final String[] args) {
        final InputReader reader = new InputReader(new BufferedReader(new InputStreamReader(System.in)));
        final int T = reader.nextInt();
        for (int caseNo = 1; caseNo <= T; caseNo++) {
            final InfiniTree infiniTree = solveCase(reader);
            final long answer = infiniTree.solve();
            System.out.printf("Case #%d: %d%n", caseNo, answer);
        }
    }

    private static void dprinf(String format, Object ... args) {
        if (DEBUG) {
            System.out.printf(format, args);
        }
    }

    private static InfiniTree solveCase(final InputReader reader) {
        final int N = reader.nextInt();
        final long A = reader.nextLong();
        final long B = reader.nextLong();
        final int[] leftChildren = new int[N];
        final int[] rightChildren = new int[N];
        for (int i = 0; i < N; i++) {
            leftChildren[i] = reader.nextInt();
        }
        for (int i = 0; i < N; i++) {
            rightChildren[i] = reader.nextInt();
        }

        final List<Pair<Integer, Integer>> parent2Children = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            parent2Children.add(new Pair<>(leftChildren[i], rightChildren[i]));
        }
        return new InfiniTree(N, A, B, parent2Children);
    }
}

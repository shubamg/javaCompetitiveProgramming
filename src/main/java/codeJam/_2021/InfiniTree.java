package codeJam._2021;

import math.MatrixCalculator;
import math.ModuloCalculator;
import util.Pair;

import java.util.List;

/**
 * Created by Shubham Gupta
 * on 15 Apr 2022.
 */
public class InfiniTree {
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
        this.multiplierMatrix = MatrixCalculator.getZeroMatrix(2 * N);
        this.initState = new long[2 * N];
        populateMultiplierMatrix(childNodePairs);
        populateInitState();
        matrixCalculator = new MatrixCalculator(ModuloCalculator.getWithoutMod());
    }

    long solve() {
        final long upperBoundOnExpo = getUpperLimit();
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
        while(getTotal(matrixPower) < B) {
            matrixPower = matrixCalculator.power(matrixPower, 2);
            expo *= 2;
        }
        return expo;
    }

    private long getTotal(final long[][] matrixPower) {
        long sum = 0L;
        for (int i = 0; i < N; i++) {
            sum += matrixPower[i + N][0];
        }
        return sum;
    }

    private void populateInitState() {
        for (int i = 1; i < initState.length; i++) {
            initState[i] = 0;
        }
        initState[0] = 1;
    }

    private void populateMultiplierMatrix(final List<Pair<Integer, Integer>> childNodePairs) {
        populateTopLeft(childNodePairs);
        populateBottom();
    }

    private void populateBottom() {
        for (int i = 0; i < N; i++) {
            final int row = i + N;
            multiplierMatrix[row][i] = 1L;
            multiplierMatrix[row][row] = 1L;
        }
    }

    private void populateTopLeft(final List<Pair<Integer, Integer>> childNodePairs) {
        int parent = 0;
        for (final Pair<Integer, Integer> childNodePair : childNodePairs) {
            // 0 is leaf (White) and 1 is root
            final int leftNode = childNodePair.getKey() - 1;
            final int rightNode = childNodePair.getValue() - 1;
            multiplierMatrix[leftNode][parent] += 1;
            multiplierMatrix[rightNode][parent] += 1;
            parent++;
        }
    }
}

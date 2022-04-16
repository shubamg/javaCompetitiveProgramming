package codeJam._2021;

import math.MatrixCalculator;
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

    public InfiniTree(final int n, final long a, final long b, final List<Pair<Integer, Integer>> childNodePairs) {
        N = n;
        A = a;
        B = b;
        this.multiplierMatrix = MatrixCalculator.getZeroMatrix(2 * N);
        this.initState = new long[2 * N];
        populateMultiplierMatrix(childNodePairs);
        populateInitState();
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

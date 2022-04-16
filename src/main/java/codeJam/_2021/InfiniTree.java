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
    // 0 is leaf (White) and 1 is root
    final List<Pair<Integer, Integer>> childNodePais;

    public InfiniTree(final int n, final long a, final long b, final List<Pair<Integer, Integer>> childNodePais) {
        N = n;
        A = a;
        B = b;
        this.childNodePais = childNodePais;
    }

    long[][] getInitMatrix() {
        final long[][] multiplierMatrix = MatrixCalculator.getZeroMatrix(N);
        int parent = 0;
        for (final Pair<Integer, Integer> childNodePair : childNodePais) {
            final int leftNode = childNodePair.getKey() - 1;
            final int rightNode = childNodePair.getValue() - 1;
            multiplierMatrix[leftNode][parent] += 1;
            multiplierMatrix[rightNode][parent] += 1;
            parent++;
        }
        return multiplierMatrix;
    }

}

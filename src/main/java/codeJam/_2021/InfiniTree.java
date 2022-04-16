package codeJam._2021;

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
    final List<Pair<Integer, Integer>> childNodes;

    public InfiniTree(final int n, final long a, final long b, final List<Pair<Integer, Integer>> childNodes) {
        N = n;
        A = a;
        B = b;
        this.childNodes = childNodes;
    }
}

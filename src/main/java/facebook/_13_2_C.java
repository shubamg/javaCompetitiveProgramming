package facebook;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class _13_2_C {
    private final static String OUTPUT_TMPL = "Case #%d: %d\n";
    private final static long MOD_BASE = 1_000_000_007L;
    private final Graph graph;

    private _13_2_C(final Graph graph) {
        this.graph = graph;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader reader = new InputReader(br);
        final int T = reader.nextInt();
        for (int i = 1; i <= T; i++) {
            final long ans = solveTestCase(reader);
            System.out.printf(OUTPUT_TMPL, i, ans);
        }
    }

    private static long solveTestCase(final InputReader reader) {
        final int N = reader.nextInt();
        final Graph graph = new Graph(N);
        for (int i = 1; i < N; i++) {
            final int left = reader.nextInt();
            final char sign = reader.nextChar();
            final int right = reader.nextInt();
            switch (sign) {
                case '<':
                    graph.addHeavyLightPair(right, left);
                    break;
                case '>':
                    graph.addHeavyLightPair(left, right);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("%c is not a valid sign", sign));
            }
        }
        return new _13_2_C(graph).solve();
    }

    private long solve() {
        return -1L;
    }

    private long[] dfs(final int node) {
        // TODO: Populate this method
        return null;
    }

    private static class Graph {
        private final Map<Integer, Collection<Integer>> heavyNeighbors;
        private final Map<Integer, Collection<Integer>> lightNeighbors;

        private Graph(final int nodeCount) {
            heavyNeighbors = new HashMap<>(nodeCount);
            lightNeighbors = new HashMap<>(nodeCount);
        }

        private void addHeavyLightPair(final int heavyNode, final int lightNode) {
            addHeavyNeighbor(lightNode, heavyNode);
            addLightNeighbor(heavyNode, lightNode);
        }

        private void addHeavyNeighbor(final int node, final int heavyNeighbor) {
            final Collection<Integer> heavyNeighbors = this.heavyNeighbors.computeIfAbsent(node, k -> new HashSet<>(1));
            heavyNeighbors.add(heavyNeighbor);
        }

        private void addLightNeighbor(final int node, final int lightNeighbor) {
            final Collection<Integer> lightNeighbors = this.lightNeighbors.computeIfAbsent(node, k -> new HashSet<>(1));
            lightNeighbors.add(lightNeighbor);
        }

        @Override
        public String toString() {
            return "Graph{" + "heavyNeighbors=" + heavyNeighbors + ", lightNeighbors=" + lightNeighbors + '}';
        }
    }
}

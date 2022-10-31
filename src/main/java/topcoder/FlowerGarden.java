package topcoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlowerGarden {
    private final Map<Integer, Integer> height2InDegree = new HashMap<>();
    private final Map<Integer, Set<Integer>> height2OutEdges = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new FlowerGarden().getOrdering(new int[]{3, 2, 5, 4},
                                                                          new int[]{1, 2, 11, 10},
                                                                          new int[]{4, 3, 12, 13})));
    }

    public int[] getOrdering(int[] height, int[] bloom, int[] wilt) {
        final int[] ordering = new int[height.length];
        for (final int h : height) {
            height2InDegree.put(h, 0);
        }
        addAllEdges(height, bloom, wilt);
        for (int i = 0; i < height.length; i++) {
            assert height2InDegree.size() == height.length - i;
            final int nextElement = popNextElement();
            ordering[i] = nextElement;
            removeOutEdgesFrom(nextElement);
        }
        assert height2InDegree.isEmpty();
        return ordering;
    }

    private void removeOutEdgesFrom(final int nextElement) {
        for (final int outEdge : height2OutEdges.getOrDefault(nextElement, Collections.emptySet())) {
            height2InDegree.compute(outEdge, (k, v) -> reduceByOne(v));
        }
    }

    private int reduceByOne(final Integer v) {
        assert v != null && v > 0;
        return v - 1;
    }

    private int popNextElement() {
        final int nextElement = height2InDegree.entrySet()
                                               .stream()
                                               .filter(e -> e.getValue() == 0)
                                               .mapToInt(Map.Entry::getKey)
                                               .max()
                                               .getAsInt();
        height2InDegree.remove(nextElement);
        return nextElement;
    }

    private void addAllEdges(final int[] height, final int[] bloom, final int[] wilt) {
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                if (intersect(bloom[i], wilt[i], bloom[j], wilt[j])) {
                    final int smaller = Math.min(height[i], height[j]);
                    final int larger = Math.max(height[i], height[j]);
                    addEdge(smaller, larger);
                }
            }
        }
    }

    private void addEdge(final int smaller, final int larger) {
        height2InDegree.compute(larger, (k, oldV) -> incrementBy1(oldV));
        height2OutEdges.computeIfAbsent(smaller, k -> new HashSet<>(1)).add(larger);
    }

    private int incrementBy1(final Integer oldV) {
        if (oldV == null) {
            return 1;
        }
        return oldV + 1;
    }

    private boolean intersect(final int l1, final int r1, final int l2, final int r2) {
        final boolean twoIsAfterOne = l2 > r1;
        final boolean oneIsAfterTwo = l1 > r2;
        final boolean areDisjoint = oneIsAfterTwo || twoIsAfterOne;
        return !areDisjoint;
    }
}

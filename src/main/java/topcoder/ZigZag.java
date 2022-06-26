package topcoder;

import java.util.Comparator;
import java.util.Map;
import java.util.OptionalInt;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.IntPredicate;

public class ZigZag {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        final ZigZag solver = new ZigZag();
        System.out.println(solver.longestZigZag(new int[]{1, 7, 4, 9, 2, 5}));
    }

    public int longestZigZag(final int[] sequence) {
        if (sequence.length == 0) {
            return 0;
        }
        final SortedMap<Integer, Integer> lastElem2NumDecSeq = new TreeMap<>();
        final SortedMap<Integer, Integer> lastElem2NumIncSeq = new TreeMap<>(Comparator.<Integer>naturalOrder()
                                                                                       .reversed());
        for (final int currElem : sequence) {
            final int decSeqEndingAtX = getNumValidSeq(lastElem2NumIncSeq, lastElem -> lastElem > currElem);
            final int incSeqEndingAtX = getNumValidSeq(lastElem2NumDecSeq, lastElem -> lastElem < currElem);
            lastElem2NumDecSeq.compute(currElem, (_k, oldV) -> getMax(oldV, decSeqEndingAtX));
            lastElem2NumIncSeq.compute(currElem, (_k, oldV) -> getMax(oldV, incSeqEndingAtX));
            if (DEBUG) {
                System.out.printf("%d%ndec: %s%ninc: %s%n%n", currElem, lastElem2NumDecSeq, lastElem2NumIncSeq);
            }
        }
        final int longestDecSeq = lastElem2NumDecSeq.values().stream().mapToInt(e -> e).max().getAsInt();
        final int longestIncSeq = lastElem2NumIncSeq.values().stream().mapToInt(e -> e).max().getAsInt();
        return Math.max(longestDecSeq, longestIncSeq);
    }

    private static int getMax(final Integer oldV, final int decSeqEndingAtX) {
        return oldV == null ? decSeqEndingAtX : Math.max(decSeqEndingAtX, oldV);
    }

    private static int getNumValidSeq(final SortedMap<Integer, Integer> lastElem2NumValidSeq,
                                      final IntPredicate filter) {
        final OptionalInt maxPrevLen = lastElem2NumValidSeq.entrySet()
                                                           .stream()
                                                           .filter(e -> filter.test(e.getKey()))
                                                           .mapToInt(Map.Entry::getValue)
                                                           .max();
        return maxPrevLen.isPresent() ? maxPrevLen.getAsInt() + 1 : 1;
    }
}
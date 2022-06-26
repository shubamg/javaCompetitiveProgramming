package topcoder;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ZigZag {
    public int longestZigZag(final int[] sequence) {
        if (sequence.length == 0) {
            return 0;
        }
        final SortedMap<Integer, Integer> lastElem2NumDecSeq = new TreeMap<>();
        final SortedMap<Integer, Integer> lastElem2NumIncSeq = new TreeMap<>(Comparator.<Integer>naturalOrder().reversed());
        for (final int x : sequence) {
            final int decSeqEndingAtX = getDecSeqEndingAtX(lastElem2NumIncSeq, x);
            final int incSeqEndingAtX = getIncSeqEndingAtX(lastElem2NumDecSeq, x);
            lastElem2NumDecSeq.compute(x, (_k, oldV) -> oldV == null ? decSeqEndingAtX : decSeqEndingAtX + oldV);
            lastElem2NumIncSeq.compute(x, (_k, oldV) -> oldV == null ? incSeqEndingAtX : incSeqEndingAtX + oldV);
        }
        final int longestDecSeq = lastElem2NumDecSeq.values().stream().mapToInt(e -> e).max().getAsInt();
        final int longestIncSeq = lastElem2NumIncSeq.values().stream().mapToInt(e -> e).max().getAsInt();
        return Math.max(longestDecSeq, longestIncSeq);
    }

    private int getIncSeqEndingAtX(final SortedMap<Integer, Integer> lastElem2NumDecSeq, final int x) {
        int incSeqEndingAtX = 1;
        for (final Map.Entry<Integer, Integer> entry : lastElem2NumDecSeq.entrySet()) {
            final int lastElement = entry.getKey();
            if (lastElement < x) {
                incSeqEndingAtX += entry.getValue();
            } else {
                break;
            }
        }
        return incSeqEndingAtX;
    }

    private int getDecSeqEndingAtX(final SortedMap<Integer, Integer> lastElem2NumIncSeq, final int x) {
        int decSeqEndingAtX = 1;
        for (final Map.Entry<Integer, Integer> entry : lastElem2NumIncSeq.entrySet()) {
            final int lastElement = entry.getKey();
            if (lastElement > x) {
                decSeqEndingAtX += entry.getValue();
            } else {
                break;
            }
        }
        return decSeqEndingAtX;
    }
}
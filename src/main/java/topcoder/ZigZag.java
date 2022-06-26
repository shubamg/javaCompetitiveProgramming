package topcoder;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ZigZag {
    public int longestZigZag(int[] sequence) {
        final SortedMap<Integer, Integer> lastElem2NumDecSeq = new TreeMap<>();
        final SortedMap<Integer, Integer> lastElem2NumIncSeq = new TreeMap<>(Comparator.<Integer>naturalOrder().reversed());
        for (final int x : sequence) {
            final int decSeqEndingAtX = getDecSeqEndingAtX(lastElem2NumIncSeq, x);
            final int incSeqEndingAtX = getIncSeqEndingAtX(lastElem2NumDecSeq, x);
            lastElem2NumDecSeq.compute(x, (_k, oldV) -> oldV == null ? decSeqEndingAtX : decSeqEndingAtX + oldV);
            lastElem2NumIncSeq.compute(x, (_k, oldV) -> oldV == null ? incSeqEndingAtX : incSeqEndingAtX + oldV);
        }
        return 1;
    }

    private int getIncSeqEndingAtX(final SortedMap<Integer, Integer> lastElem2NumDecSeq, final int x) {
        int incSeqEndingAtX = 0;
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
        int decSeqEndingAtX = 0;
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
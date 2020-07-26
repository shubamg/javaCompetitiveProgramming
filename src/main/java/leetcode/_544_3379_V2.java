package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class _544_3379_V2 {
    final static List<Integer> index2BitSet = new ArrayList<>();
    final static Set<Integer> allBitSets = new HashSet<>();
    private final static int TRIMMED_LEN = 6;

    public static void main(String[] args) {
        final _544_3379_V2 x = new _544_3379_V2();
        final int[] cell = {1, 0, 0, 1, 0, 0, 1, 0};
        System.out.println(Arrays.toString(x.prisonAfterNDays(cell, 1000000000)));
    }

    public int[] prisonAfterNDays(int[] cell, int N) {
        if (cell == null || N == 0) {
            return cell;
        }
        int bitSet = getBitSet(cell);
        while (index2BitSet.size() < N && !allBitSets.contains(bitSet)) {
            index2BitSet.add(bitSet);
            allBitSets.add(bitSet);
            System.out.println((index2BitSet.size() - 1) + " Old: " + Arrays.toString(getCell(bitSet)));
            bitSet = getNextState(bitSet);
            System.out.println((index2BitSet.size() - 1) + " New : " + Arrays.toString(getCell(bitSet)));
        }
        if (index2BitSet.size() == N) {
            return getCell(index2BitSet.size() - 1);
        }
        final int cycleStart = index2BitSet.indexOf(bitSet);
        final int cycleLen = index2BitSet.size() - cycleStart;
        final int x = ((N - 1 - cycleStart) % cycleLen) + cycleStart;
        System.out.println(x);
        System.out.println(cycleStart);
        System.out.println(cycleLen);
        for (int i = 0; i < index2BitSet.size(); i++) {
            final int _bitSet = index2BitSet.get(i);
            System.out.println(i + ": " + Arrays.toString(getCell(_bitSet)));
        }
        return getCell(index2BitSet.get(x));
    }

    private int getNextState(final int prevState) {
        return (~((prevState >> 1) ^ (prevState << 1))) & ((1 << TRIMMED_LEN) - 1);
    }

    private int getBitSet(final int[] cells) {
        int bitSet = 0;
        for (int i = 0; i < TRIMMED_LEN; i++) {
            bitSet |= ((cells[i] == cells[i + 2]) ? (1 << i) : 0);
        }
        return bitSet;
    }

    private int[] getCell(final int bitSet) {
        final int[] ret = new int[8];
        for (int i = 0; i < TRIMMED_LEN; i++) {
            ret[i + 1] = (bitSet & (1 << i)) > 0 ? 1 : 0;
        }
        return ret;
    }
}

/*
INPUT:
{1,0,0,1,0,0,1,0}

OUTPUT:
19
7
14
0: [0, 1, 1, 0, 0, 0, 0, 0]
1: [0, 0, 0, 0, 1, 1, 1, 0]
2: [0, 1, 1, 0, 0, 1, 0, 0]
3: [0, 0, 0, 0, 0, 1, 0, 0]
4: [0, 1, 1, 1, 0, 1, 0, 0]
5: [0, 0, 1, 0, 1, 1, 0, 0]
6: [0, 0, 1, 1, 0, 0, 0, 0]
7: [0, 0, 0, 1, 0, 0, 1, 0]
8: [0, 1, 0, 1, 0, 0, 1, 0]
9: [0, 1, 1, 1, 0, 0, 1, 0]
10: [0, 0, 1, 0, 0, 0, 1, 0]
11: [0, 0, 1, 0, 1, 0, 1, 0]
12: [0, 0, 1, 1, 1, 1, 1, 0]
13: [0, 0, 0, 1, 1, 1, 0, 0]
14: [0, 1, 0, 0, 1, 0, 0, 0]
15: [0, 1, 0, 0, 1, 0, 1, 0]
16: [0, 1, 0, 0, 1, 1, 1, 0]
17: [0, 1, 0, 0, 0, 1, 0, 0]
18: [0, 1, 0, 1, 0, 1, 0, 0]
19: [0, 1, 1, 1, 1, 1, 0, 0]
20: [0, 0, 1, 1, 1, 0, 0, 0]*/

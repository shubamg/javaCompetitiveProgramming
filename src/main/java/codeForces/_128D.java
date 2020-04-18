package codeForces;

import io.InputReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Edge Case: If total length of the array is <=1, then answer is always true by default
 * Assume lowest number is 0 and highest is m
 * We build from lower number to larger one
 * Let count of 0 = 3.
 * Put all 0's on the circle like 0 __ 0 __ 0 __ 0
 * Last 0 is same as first 0.
 * Each __ must be filled by a 1. We say there are 3 unseparated pairs of 0
 * So count(1) should be >= 3
 * Let count(1) = 5
 * Then we will place 1's as 0 1 __ 1 __ 1 0 1 0 1 0
 * Each unseperated pair of 0 is seperated by 1
 * Extra 1's can be placed anywhere, doesn't matter (proof left as an exercise)
 * Now we get unseperated pairs of 1's.
 * We can forget about other 1's and all 0's
 * So we only need to consider: 1 __ 1 __ 1 , i.e 2 pairs of 1's [2 = count(1) - numPairs(0)]
 * Now we need atleast 2 2's to seperate pair of 1's
 * We proceed as above till we have exhausted the entire array of numbers OR number of pairs <= 0
 * If num pairs of m == 0 and we have exhausted the array, we return true
 * else if num pairs <= 0, it means, the quantity of a number is < that required
 * else m is in higher quantity than can be supported
 */
public class _128D {
    private final Map<Integer, Integer> freqMap = new HashMap<>();
    private int minNum = Integer.MAX_VALUE - 1;
    private int numNumbers;

    private void addNum(final int num) {
        freqMap.compute(num, (k, oldVal) -> oldVal == null ? 1 : oldVal + 1);
        minNum = Math.min(num, minNum);
        numNumbers++;
    }

    private boolean isSolvable() {
        if (numNumbers <= 1) {
            return true;
        }
        int n = minNum;
        int numsRemaining = numNumbers;
        int pairsOfN = freqMap.get(n);
        numsRemaining -= pairsOfN;
        while (numsRemaining > 0 && pairsOfN > 0) {
            ++n;
            int countOfN = freqMap.getOrDefault(n, 0);
            pairsOfN = countOfN - pairsOfN;
            numsRemaining -= countOfN;
        }
        return numsRemaining == 0 && pairsOfN == 0;
    }

    public static void main(String[] args) {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        final InputReader inputReader = new InputReader(br);
        final String YES = "YES";
        final String NO = "NO";
        final int N = inputReader.nextInt();
        final _128D solver = new _128D();
        for (int i = 0; i < N; i++) {
            solver.addNum(inputReader.nextInt());
        }
        System.out.println(solver.isSolvable() ? YES : NO);
    }
}

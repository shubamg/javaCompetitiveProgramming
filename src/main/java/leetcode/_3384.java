package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class _3384 {
    public static void main(String[] args) {
        final int[] num = {-1, 0, 1, 2, -1, -4};
        System.out.println(new _3384().threeSum(num));
    }

    private List<List<Integer>> threeSum(int[] nums) {
        final List<List<Integer>> triplets = new ArrayList<>();
        Arrays.sort(nums);
        final NavigableMap<Integer, Long> nums2Freq = Arrays.stream(nums).boxed().
                collect(Collectors.groupingBy(Integer::intValue, TreeMap::new, Collectors.counting()));
        for (final int num1 : nums2Freq.keySet()) {
            for (final int num2 : nums2Freq.descendingKeySet()) {
                final int thirdReq = -(num1 + num2);
                if (thirdReq < num1) {
                    continue;
                }
                if (thirdReq > num2) {
                    break;
                }
                if (!nums2Freq.containsKey(thirdReq)) {
                    continue;
                }
                final int usedCount = ((num1 == thirdReq) ? 1 : 0) + ((num2 == thirdReq) ? 1 : 0);
                if (nums2Freq.get(thirdReq) <= usedCount) {
                    continue;
                }
                triplets.add(Arrays.asList(num1, thirdReq, num2));
            }
        }
        return triplets;
    }
}

package _99_strategy_pattern;

import org.junit.Test;

/**
 * create by lwj on 2019/10/12
 */
public class Algo {
    @Test
    public void test() {
        System.out.println(search(new int[]{1, 2, 3, 3, 4, 5, 6, 7}, 4));
    }

    public int search(int[] nums, int target) {
        int l = 0, h = nums.length - 1,mid = 0;
        while (l <= h) {
            mid = l + ((h - l) >>> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                h = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return -1;
    }
}

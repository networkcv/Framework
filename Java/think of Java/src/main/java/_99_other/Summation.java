package _99_other;

import java.util.HashSet;

public class Summation {
    public static void main(String[] args) {
        int[] nums = {0, 3, 4, 2, 1, 5, 3};
        boolean flag = checkTwoSum(nums, 9);
        System.out.println(flag);
    }

    public static boolean checkTwoSum(int[] nums, int k) {
        HashSet<Integer> set = new HashSet<>();
        int temp = 0;
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (k % 2 == 0 && nums[i] == k / 2) {
                index++;
            }
            set.add(nums[i]);
        }
        for (int i = 0; i < nums.length; i++) {
            if (index == 1) {
                set.remove(k / 2);
            }
            temp = k - nums[i];
            if (set.add(temp)) {
                set.remove(temp);
            } else {
                return true;
            }
        }
        return false;
    }
}

package lwj.bk._03_数组中的重复数字;

/**
 * create by lwj on 2019/2/3
 */
public class Test1 {
    /*
    时间复杂度O(n),空间复杂度0(1)
    核心点：每一个数字都会存在唯一的索引位置

    n个数，大小在0～n-1之间，找出重复的数
    每个数都会对应唯一的一个索引位置，例如2，3，1，2 中 两个2都对应的是1这个位置，因此将1的位置作为2的标志位，
    遍历数组，到第一个2时，对应的索引位置(标志位)为1，给1加上大于等于长度length的值，遍历到第二个2的时候，该位置大于等
    于length，所以判定该数字重复。
    如果遍历到索引为2的数时，因为之前第一个数(2)的操作，所以此时索引为2的值不是1，而是1+length,那么不能直接将该值作为索
    引，而是要先要减去length，还原本来的值，再进行该值对应索引位置的操作
    */
    public static boolean duplicate(int numbers[], int length, int[] duplication) {
        if (numbers == null || length == 0) return false;
        for (int i = 0; i < length; i++) {
            int index = numbers[i];
            if (index >= length) {
                index -= length;
            }
            if (numbers[index] >= length) {
                duplication[0] = index;
                return true;
            }
            numbers[index] += length;
        }
        return false;
    }

    /*
    和上面的方法类似，不过没有采用标志位，而是进行位置互换,
    先判断该值是否和索引相等，相等的话判断下一个值
    不等的话，比较该值和将该值作为索引的值是否相等，相等的话，证明数字重复
    不等的话，交换位置，继续判断该值是否和索引相等
    */
    public static boolean duplicate1(int[] nums , int length, int[] duplication) {
        for(int i=0;i<length;i++){
            while(i!=nums[i]){
                if(nums[i]==nums[nums[i]]){
                    duplication[0]=nums[i];
                    return true;
                }else{
                    int tmp=nums[i];
                    nums[i]=nums[nums[i]];
                    nums[tmp]=tmp;
                }
            }
        }
        return false;
    }

    public static void main(String[] args){
        int [] nums={2,3,1,2};
        int []duplication=new int[1];
        duplicate1(nums,nums.length,duplication);
        System.out.println(duplication[0]);
    }
}

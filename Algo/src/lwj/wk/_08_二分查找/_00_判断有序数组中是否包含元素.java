package lwj.wk._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _00_判断有序数组中是否包含元素 {
    public static void main(String[] args) {
        int [] arr=new int []{1,2,3,4,5,6,7};
        //使用for循环实现二分查找
//        int index = BinarySearchByFor(arr, arr.length, 2);
        //使用递归实现二分查找
         int index= BinarySearchByRecursion(arr,0,arr.length-1,3);
        System.out.println(index);

    }

    private static int BinarySearchByFor(int[] arr, int n, int val) {
        int low=0;
        int high=n-1;
        while(low<=high){
            int mid=low+((high-low)>>1);
            if(arr[mid]==val){
                return mid;
            }else if(arr[mid]<val){
                low=mid+1;
            }else {
                high=mid-1;
            }
        }
        return -1;
    }

    private static int BinarySearchByRecursion(int[] arr, int low, int high, int val) {
        if(low>high) return -1;
        int mid=low+((high-low)>>1);
        if(val==mid){
            return mid;
        }else if(val>mid){
            return  BinarySearchByRecursion(arr,low+1,high,val);
        }else{
            return  BinarySearchByRecursion(arr,low,high-1,val);
        }
    }
}

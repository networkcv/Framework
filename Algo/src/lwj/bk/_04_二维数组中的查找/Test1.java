package lwj.bk._04_二维数组中的查找;

/**
 * create by lwj on 2019/2/3
 */
public class Test1 {
    public static void main(String[] args) {
        int target = 14;
        int[][] matrix = {{1, 2, 8, 9},
                         { 2, 4, 9, 12},
                         {4, 7, 10, 13},
        };
        System.out.println(fun1(matrix, target));
        System.out.println(fun2(matrix, target));
    }
    //循环遍历所有元素
    public static boolean fun1(int[][] matrix, int target) {
        int x = matrix[0].length - 1;
        int y = matrix.length - 1;
        for (int i = x; i >= 0; i--) {
            for (int j = 0; j <=y; j++) {
                if (target == matrix[j][i]) {
                    return true;
                }
            }
        }
        return  false;
    }

    //右上角比较法
    public static boolean fun2(int[][] matrix, int target){
        boolean flag=false;
        int x= 0;
        int y = matrix[0].length - 1;
        while(x<matrix.length &&y>0){
            if(matrix[x][y]==target){
                return true;
            }else if(matrix[x][y]>target){
                y--;
            }else{
                x++;
            }
        }
        return false;
    }
}

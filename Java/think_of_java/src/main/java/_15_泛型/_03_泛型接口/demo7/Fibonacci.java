package _15_泛型._03_泛型接口.demo7;

/**
 * create by lwj on 2020/3/21
 * 斐波数列生成器
 */
public class Fibonacci implements Generator<Integer> {
    private int count = 0;

    public  int fib(int n) {
        if (n < 2) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    @Override
    public Integer next() {
        return fib(count++);
    }
    public static void main(String[] args){
        Fibonacci fibonacci = new Fibonacci();
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacci.next()+" ");
        }
    }
}

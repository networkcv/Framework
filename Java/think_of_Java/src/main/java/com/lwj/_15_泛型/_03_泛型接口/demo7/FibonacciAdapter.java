package com.lwj._15_泛型._03_泛型接口.demo7;

import java.util.Iterator;

/**
 * create by lwj on 2020/3/22
 * 使用组合代替继承，适配Fibonacci使其成为Iterable
 */
public class FibonacciAdapter implements Iterable<Integer> {
    private int size;
    private Fibonacci fibonacci = new Fibonacci();

    public FibonacciAdapter(int size) {
        this.size = size;
    }

    private class FibonacciIterator implements Iterator {
        private int size;
        private int count = 0;
        private Fibonacci fibonacci;

        public FibonacciIterator(int size, Fibonacci fibonacci) {
            this.size = size;
            this.fibonacci = fibonacci;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Object next() {
            return fibonacci.fib(count++);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new FibonacciIterator(size, fibonacci);
    }

    public static void main(String[] args) {
        for (Integer i : new FibonacciAdapter(5)) {
            System.out.println(i);
        }
    }
}

package com.lwj.arthas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2023/5/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ArthasQuickStart2 {
    private static Random random = new Random();

    private int illegalArgumentCount = 0;

    public static void main(String[] args) throws InterruptedException {
        ArthasQuickStart2 game = new ArthasQuickStart2();
        Thread thread = new Thread(game::run2, "wb");
        thread.start();
        thread.join();
    }

    public void run2() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                ArrayList<ArthasQuickStart2> obj = createObj();
                int number = random.nextInt() / 10000;
                List<Integer> primeFactors = primeFactors(number);
                if (obj.size() % 2 == 0) {
                    print(number, primeFactors);
                }
            } catch (Exception e) {
            }
        }
    }

    private ArrayList<ArthasQuickStart2> createObj() {
        ArrayList<ArthasQuickStart2> objects = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            ArthasQuickStart2 obj = new ArthasQuickStart2();
            objects.add(obj);
        }
        return objects;
    }

    public static void print(int number, List<Integer> primeFactors) {
        StringBuffer sb = new StringBuffer(number + "=");
        for (int factor : primeFactors) {
            sb.append(factor).append('*');
        }
        if (sb.charAt(sb.length() - 1) == '*') {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }

    public List<Integer> primeFactors(int number) {
        if (number < 2) {
            illegalArgumentCount++;
            throw new IllegalArgumentException("number is: " + number + ", need >= 2");
        }

        List<Integer> result = new ArrayList<Integer>();
        int i = 2;
        while (i <= number) {
            if (number % i == 0) {
                result.add(i);
                number = number / i;
                i = 2;
            } else {
                i++;
            }
        }

        return result;
    }
}

package com.lwj.algo.leetcode.editor.cn.custom._08_二分查找;

import java.math.BigDecimal;

/**
 * create by lwj on 2019/3/12.
 */
public class _02_求一个数的平方根_保留n位小数 {
    public static void main(String[] args) {
        for (int j = 1; j < 10; j++) {
            System.out.println(j + ": " + fun(j, 6) + "   ");
            System.out.println(j + ": " + fun1(j, 6) + "   可能精度丢弃");
        }
    }

    //  n   为精确到小数点后n位
    private static double fun1(double x, int n) {
        double low = 0;
        double high = x;
        double i = 0;
        double mid = 0;
        //此处i<(n+1) 因为每二分确定一位i+1 ，包括整数位(但整数位只需要一次就可以确定)
        while (low <= high && i < (n + 1)) {
            mid = (low + high) / 2;
            if (mid * mid == x) {
                return new BigDecimal(mid).setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if (mid * mid > x) {
                if (Math.pow((mid - Math.pow(10, -i)), 2) < x) {
                    i++;
                    continue;
                }
                high = (mid - Math.pow(10, -i));
            } else {
                if (Math.pow((mid + Math.pow(10, -i)), 2) > x) {
                    i++;
                    continue;
                }
                low = (mid + Math.pow(10, -i));
            }
        }
        return new BigDecimal(mid).setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 都每一位都使用二分法查找     防止精度丢失，使用自定义的double运算类
     *
     * @param x 要开方的值
     * @param n 精确到的位数
     */
    private static double fun(double x, int n) {
        double low = 0;
        double high = x;
        double i = 0;
        double mid = 0;
        while (low <= high && i < 15) {
            mid = calc(calc(low, high, "+"), 2, "/");
            if ((calc(mid, mid, "*")) == x) {
                return new BigDecimal(mid).setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else if ((calc(mid, mid, "*")) < x) {
                if (calc((calc(mid, (Math.pow(10, -i)), "+")), (calc(mid, (Math.pow(10, -i)), "+")), "*") > x) {
                    i++;
                    continue;
                }
                low = calc(mid, Math.pow(10, -i), "+");
            } else {
                if (calc((calc(mid, (Math.pow(10, -i)), "-")), (calc(mid, (Math.pow(10, -i)), "-")), "*") < x) {
                    i++;
                    continue;
                }
                high = calc(mid, Math.pow(10, -i), "-");
            }
        }
        return new BigDecimal(mid).setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //自定义的doubel运算类
    private static double calc(double a, double b, String operator) {
        if ("+".equals(operator)) {
            BigDecimal a2 = new BigDecimal(Double.toString(a));
            BigDecimal b2 = new BigDecimal(Double.toString(b));
            return a2.add(b2).doubleValue();
        } else if ("-".equals(operator)) {
            BigDecimal a2 = new BigDecimal(Double.toString(a));
            BigDecimal b2 = new BigDecimal(Double.toString(b));
            return a2.subtract(b2).doubleValue();
        } else if ("/".equals(operator)) {
            BigDecimal a2 = new BigDecimal(Double.toString(a));
            BigDecimal b2 = new BigDecimal(Double.toString(b));
            return a2.divide(b2).doubleValue();
        } else {
            BigDecimal a2 = new BigDecimal(Double.toString(a));
            BigDecimal b2 = new BigDecimal(Double.toString(b));
            return a2.multiply(b2).doubleValue();
        }
    }

    //牛顿迭代法
    public static double SqrtByNewton(int n, double precision) {
        double val = n; //最终
        double last; //保存上一个计算的值
        do {
            last = val;
            val = (val + n / val) / 2;
        } while (Math.abs(val - last) > precision);
        return val;
    }

}

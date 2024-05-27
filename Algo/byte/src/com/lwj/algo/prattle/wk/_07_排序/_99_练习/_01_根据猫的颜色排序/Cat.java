package com.lwj.algo.prattle.wk._07_排序._99_练习._01_根据猫的颜色排序;

/**
 * create by lwj on 2019/3/13.
 */
public class Cat {
    private int no;
    private String color;

    public Cat() {
    }

    public Cat(int no, String color) {
        this.no = no;
        this.color = color;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "no=" + no +
                ", color='" + color + '\'' +
                '}';
    }
}


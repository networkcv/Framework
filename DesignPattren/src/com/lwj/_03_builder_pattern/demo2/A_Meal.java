package com.lwj._03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public class A_Meal extends Meal {
    @Override
    public void setFood(Food food) {
        this.food=food;
    }

    @Override
    public void setDrink(Drink drink) {
        this.drink=drink;
    }
}

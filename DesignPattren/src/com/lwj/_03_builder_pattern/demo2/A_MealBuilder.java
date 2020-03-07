package com.lwj._03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public class A_MealBuilder implements MealBuilder {

    public Meal getMeal(){
        return new A_Meal();
    }

    @Override
    public Food createFood() {
        return new A_Food();
    }

    @Override
    public Drink createDrink() {
        return new A_Drink();
    }
}

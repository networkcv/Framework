package com.lwj._03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public interface MealBuilder {
     Meal getMeal();
     Food createFood();
     Drink createDrink();
}

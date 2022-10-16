package _03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public class Driector {
    MealBuilder mealBuilder;

    public void setMealBuilder(MealBuilder mealBuilder) {
        this.mealBuilder = mealBuilder;
    }

    public  Meal  construct(){
        Meal  meal=mealBuilder.getMeal();
        meal.setDrink(mealBuilder.createDrink());
        meal.setFood(mealBuilder.createFood());
        return  meal;
    }

}

package _03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public class Client {
    public static void main(String[] args){
        Driector driector=new Driector();
        driector.setMealBuilder(new A_MealBuilder());
        Meal A_Meal = driector.construct();
        A_Meal.eat();
        A_Meal.drink();
    }

}

package _03_builder_pattern.demo2;

/**
 * create by lwj on 2019/1/13
 */
public abstract class Meal {
    Food food = null;
    Drink drink = null;
     void setFood(Food food){}
    void setDrink(Drink drink){}
    void eat(){
         food.eat();
    }
    void drink(){
         drink.drink();
    }


}

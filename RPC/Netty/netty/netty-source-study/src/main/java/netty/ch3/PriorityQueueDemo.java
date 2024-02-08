package netty.ch3;

import java.util.PriorityQueue;

/**
 * Date: 2024/2/8
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class PriorityQueueDemo {
    public static void main(String[] args) {
        PriorityQueue<Person> queue = new PriorityQueue<>();
        queue.offer(new Person(2));
        queue.offer(new Person(3));
        queue.offer(new Person(1));
        queue.offer(new Person(4));
        System.out.println(queue);
        //[Person{age=1}, Person{age=3}, Person{age=2}, Person{age=4}]
        while (!queue.isEmpty()) {
            Person person = queue.remove();
            System.out.print(person + " ");
        }
        //Person{age=1} Person{age=2} Person{age=3} Person{age=4}
    }

    public static class Person implements Comparable<Person> {

        private int age;

        public int getAge() {
            return age;
        }

        public Person(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    '}';
        }


        @Override
        public int compareTo(Person o) {
            return Integer.compare(this.getAge(), o.getAge());
        }
    }
}

package _14_类型信息._01_JTTI;

import org.junit.Test;

import java.util.ArrayList;

/**
 * create by lwj on 2020/3/20
 */
public class Test1 {
    @Test
    public void test() {
        Student student = new Student();
        ArrayList<Person> arrayList = new ArrayList();
        arrayList.add(student);
        System.out.println(arrayList.get(0));
    }

    class Person {
    }

    class Student extends Person {
    }
}

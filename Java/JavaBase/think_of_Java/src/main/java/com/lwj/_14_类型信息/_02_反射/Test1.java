package com.lwj._14_类型信息._02_反射;


import org.junit.Test;

/**
 * create by lwj on 2020/3/20
 */
public class Test1 {
    public static void main(String[] args) throws ClassNotFoundException {
        Person person = new Person();
        Class<? extends Person> aClass = person.getClass();
        Class<Person> personClass = Person.class;
        Class<Test1> test1Class = Test1.class;
        ClassLoader classLoader = Test1.class.getClassLoader();
        Class.forName("_14_类型信息._02_反射.Person", false, classLoader);
        Class<?> aClass1 = Class.forName("_14_类型信息._02_反射.Person");
    }

    class T extends Student {
        Class clazz = T.class;
    }

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        Class<? super Student> clazz;
        clazz = Person.class;
//        clazz=T.class;    //error

        Class personClass = Person.class;
        Class studentClass = Student.class;
        System.out.println(personClass.isInstance(studentClass.newInstance()));
        System.out.println(personClass.isAssignableFrom(studentClass));
        System.out.println(studentClass.isInstance(personClass.newInstance()));
        System.out.println(studentClass.isAssignableFrom(personClass));


    }
}

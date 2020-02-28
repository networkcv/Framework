package util;

import java.util.Objects;

public class Student {
        private String name;
        private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Student() {
            System.out.println(" Student() ");
        }

        public Student(String name) {
            System.out.println(" Student(String) ");
            this.name = name;
        }

        public Student(String name, Integer age) {
            System.out.println(" Student(String，Integer) ");
            this.name = name;
            this.age = age;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) &&
                Objects.equals(age, student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
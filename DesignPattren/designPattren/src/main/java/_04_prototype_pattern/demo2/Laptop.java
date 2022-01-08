package _04_prototype_pattern.demo2;

public class Laptop implements  Cloneable{

    public Laptop() throws InterruptedException {
        Thread.sleep(10);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

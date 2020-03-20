package _97_设计模式._03_工厂模式.demo14;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * create by lwj on 2020/3/20
 */
public class Part {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    static List<Class<? extends Part>> partFactories = new ArrayList<>();

    static {
        partFactories.add(AFilter.class);
        partFactories.add(BFilter.class);
    }

    private static Random rand = new Random(47);

    public static Part createRandom() {
        int n = rand.nextInt(partFactories.size());
        try {
            return partFactories.get(n).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(Part.createRandom());
        }
    }
}

interface Factory<T> {
    T create();
}

class Filter extends Part {
}

class AFilter extends Filter {
    public static class Factory implements _97_设计模式._03_工厂模式.demo14.Factory {
        @Override
        public Object create() {
            return new AFilter();
        }
    }
}

class BFilter extends Filter {
    public static class Factory implements _97_设计模式._03_工厂模式.demo14.Factory {
        @Override
        public Object create() {
            return new BFilter();
        }
    }
}



package com.lwj.mapper;

import com.lwj.utils.ConcurrentStopWatch;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Date: 2021/11/20
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class OtherTest {

    public static void main(String[] args) {
        Object o = new Object();
        Object o2 = new Object();
        String str = "hello";
        for (int i = 0; i < 10; i++) {
            str += str;
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        System.out.println();
    }

    @Test
    public void debugTest() {
        Math.random();
        System.out.println();
    }

    static class AccessibleClass {

        public void publicMethod() {
        }

        private void privateMethod() {
        }
    }

    @Test
    public void accessibleTest2() throws Exception {
        Method publicMethod = AccessibleClass.class.getMethod("publicMethod");
        Method privateMethod = AccessibleClass.class.getDeclaredMethod("privateMethod");
        System.out.println(publicMethod.isAccessible());
        System.out.println(privateMethod.isAccessible());
    }

    @Test
    public void accessibleTest() throws Exception {
        ConcurrentStopWatch sw = new ConcurrentStopWatch();
        AccessibleClass obj = new AccessibleClass();
        Method publicMethod = AccessibleClass.class.getMethod("publicMethod");
        publicMethod.setAccessible(false);
        sw.start("checked use time");
        for (int i = 0; i < 100000000; i++) {
            publicMethod.invoke(obj, null);
        }

        sw.stop("checked use time");
        publicMethod.setAccessible(true);
        sw.start("no checked use time");
        for (int i = 0; i < 100000000; i++) {
            publicMethod.invoke(obj, null);
        }
        sw.stop("no checked use time");
        System.out.println(sw.prettyPrint());
    }

}

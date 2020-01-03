package tmp;

import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * create by lwj on 2020/1/2
 */
public class ContainerTest {
    private static class Node {
        public Integer p1;
        public Integer p2;
        private Integer age;

        public Node(Integer p1, Integer p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public Integer getAge() {
            return age;
        }
    }

    @Test
    public void test() {
        CopyOnWriteArrayList<Node> list = new CopyOnWriteArrayList<>();
        list.add(new Node(1,1));
        Iterator<Node> iterator = list.iterator();
        while (iterator.hasNext()){
            iterator.remove();
        }
        System.out.println(list.size());

    }
}

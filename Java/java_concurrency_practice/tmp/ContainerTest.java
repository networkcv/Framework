package tmp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * create by lwj on 2020/1/2
 */
public class ContainerTest {
    @Test
    public void test(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator<Object> iterator = list.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}

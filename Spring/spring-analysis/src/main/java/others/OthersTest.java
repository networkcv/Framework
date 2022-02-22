package others;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Date: 2022/2/21
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class OthersTest {

    private Set<Long> set = Sets.newHashSet(1L,10L);

    public void fun(Consumer<Set<Long>> consumer, Predicate<Set<Long>> predicate) {
        if (predicate.test(set)){
            consumer.accept(set);
        }
    }

    @Test
    public void test1() {
        fun(System.out::println,l->l.equals(10L));
    }
}

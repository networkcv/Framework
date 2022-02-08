import com.lagou.source.TestBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringSourceTest {
	/**
	 * Spring源码搭建测试案例
	 */
	@Test
	public void testSpringSource(){
		//容器初始化
		ApplicationContext applicationContext=
				new ClassPathXmlApplicationContext("classpath:application-context.xml");
		TestBean testBean=applicationContext.getBean(TestBean.class);
		System.out.println(testBean);
	}
}

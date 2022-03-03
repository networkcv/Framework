package instance;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Date: 2022/3/3
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BootStart {
    public static void main(String[] args) {
//        MyContext context = new MyContext("instance_config.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("instance_config.xml");
        InstanceBean bean = context.getBean(InstanceBean.class);
        System.out.println(bean);
    }
}

class MyContext extends ClassPathXmlApplicationContext {

    public MyContext(String configLocation) throws BeansException {
        super(configLocation);
    }

    @Override
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.prepareBeanFactory(beanFactory);
        AbstractAutowireCapableBeanFactory factory = (AbstractAutowireCapableBeanFactory) beanFactory;
        factory.setInstantiationStrategy(new SimpleInstantiationStrategy());
    }
}

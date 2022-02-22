package java_config;

import base.Teacher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Date: 2022/2/15
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
//@Component
public class TeacherConfig implements BeanFactoryPostProcessor, BeanPostProcessor {
    @Bean
    public Teacher teacher() {
        return new Teacher("lwj", 25);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("invoke customer postProcessBeanFactory");
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        System.out.println("invoke customer postProcessBeforeInitialization");
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("invoke customer postProcessAfterInitialization");
        return o;
    }
}

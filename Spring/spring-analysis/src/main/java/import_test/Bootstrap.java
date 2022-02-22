package import_test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author skywalker
 */
public class Bootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfig.class);
        JavaBeanA bean = context.getBean(JavaBeanA.class);
        bean.func();
        System.out.println(bean);
        JavaBeanB beanB = context.getBean(JavaBeanB.class);
        beanB.func();
        System.out.println(beanB);
        ImportConfig importConfig = context.getBean(ImportConfig.class);
        System.out.println(importConfig);
    }

}

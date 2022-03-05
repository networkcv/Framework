package import_test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author skywalker
 */
public class Bootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ImportConfig.class);
        JavaBeanA bean = context.getBean(JavaBeanA.class);
        System.out.println(bean);
    }

}

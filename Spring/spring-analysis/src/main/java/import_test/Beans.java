package import_test;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * Date: 2022/3/4
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Beans {
}

@Component("javaBeanA")
class JavaFactoryBeanA implements FactoryBean<JavaBeanA> {
    public JavaFactoryBeanA() {
        System.out.println("JavaFactoryBeanA的无参构造");
    }

    @Override
    public JavaBeanA getObject() throws Exception {
        return new JavaBeanA();
    }

    @Override
    public Class<?> getObjectType() {
        return JavaBeanA.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

@Data
class JavaBeanA {
    public JavaBeanA() {
        System.out.println("JavaBeanA的无参构造");
    }
}

class JavaBeanB {
    public JavaBeanB() {
        System.out.println("JavaBeanB的无参构造");
    }
}

class JavaBeanC {
    public JavaBeanC() {
        System.out.println("JavaBeanC的无参构造");
    }
}

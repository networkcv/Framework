package import_test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Date: 2022/2/19
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Configuration
//@Import({MyImportBeanDefinitionRegistrar.class})
//@Import({MyImportSelector.class})
//@Import({JavaBeanA.class})
@Import(JavaFactoryBeanA.class)
public class ImportConfig {
}


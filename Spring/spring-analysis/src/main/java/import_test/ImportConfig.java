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
@Import({MyImportBeanDefinitionRegistrar.class, MyImportSelector.class})
public class ImportConfig {
    private Integer age=1;
}

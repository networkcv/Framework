package import_test;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Date: 2022/2/19
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"import_test.Beans.JavaBeanB"};
    }
}

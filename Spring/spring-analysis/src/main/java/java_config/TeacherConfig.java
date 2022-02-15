package java_config;

import base.Teacher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Date: 2022/2/15
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Component
public class TeacherConfig {
    @Bean
    public Teacher teacher() {
        return new Teacher("lwj",25);
    }
}

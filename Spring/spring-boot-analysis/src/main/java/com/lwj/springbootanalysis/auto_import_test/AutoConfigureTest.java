package com.lwj.springbootanalysis.auto_import_test;

import com.lwj.springbootanalysis.base.Age;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Date: 2022/3/4
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Configuration
public class AutoConfigureTest {

    @Bean
    public Age age() {
        return new Age();
    }
}

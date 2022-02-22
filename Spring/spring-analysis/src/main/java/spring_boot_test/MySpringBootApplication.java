package spring_boot_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Date: 2022/2/21
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@SpringBootApplication //来标注一个主程序类，说明这是一个Spring Boot应用
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

}

package com.lwj.springbootanalysis.circularDependencies;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Date: 2022/3/29
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Aspect
@Component
public class AOP {
    @Before("execution(public void com.lwj.springbootanalysis.circularDependencies.BeanA.methodA())")
    public void beforeA() {
        System.out.println("before method");
    }
}

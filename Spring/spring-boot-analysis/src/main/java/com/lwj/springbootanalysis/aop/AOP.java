package com.lwj.springbootanalysis.aop;

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
    @Before("execution(public void com.lwj.springbootanalysis.aop.BeanA.methodA())")
    public void beforeA() {
        System.out.println("before method");
    }
}

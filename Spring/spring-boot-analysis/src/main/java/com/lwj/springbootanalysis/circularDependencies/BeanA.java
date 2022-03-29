package com.lwj.springbootanalysis.circularDependencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Date: 2022/3/29
 * <p>
 * Description:
 *
 * @author liuWangjie
 */

@Component("a")
public class BeanA {

    @Autowired
    private BeanB beanB;

    public void methodA(){
        System.out.println("methodA");
    }
}


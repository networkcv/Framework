package com.lwj.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

/**
 * Date: 2021/11/17
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MyPlugin implements Interceptor {
    /**
     * 只要被拦截的目标对象的目标方法被执行时，就会执行该方法
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("对方法进行了增强");
        return invocation.proceed();
    }

    /**
     * 将当前拦截器生成代理存到拦截器链中
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    /**
     * 获取配置参数
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("获取到对配置文件参数时：" + properties);
    }
}

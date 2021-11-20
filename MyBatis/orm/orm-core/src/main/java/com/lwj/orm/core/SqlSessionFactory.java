package com.lwj.orm.core;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public interface SqlSessionFactory {
    /**
     * 开启数据库连接会话
     */
    SqlSession openSession();
}

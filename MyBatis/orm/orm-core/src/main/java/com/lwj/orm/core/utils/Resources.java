package com.lwj.orm.core.utils;

import lombok.Data;

import java.io.InputStream;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
public class Resources {

    public static InputStream getResourcesAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}

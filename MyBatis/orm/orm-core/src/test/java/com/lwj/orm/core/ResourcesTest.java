package com.lwj.orm.core;

import com.lwj.orm.core.utils.Resources;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.InputStream;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Slf4j
public class ResourcesTest {
    @Test
    public void getResourcesAsStream() {
        InputStream inputStream = Resources.getResourcesAsStream("sqlMapConfig.xml");
        log.info("inputStream : {}", inputStream);
    }
}
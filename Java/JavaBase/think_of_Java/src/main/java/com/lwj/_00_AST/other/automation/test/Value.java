package com.lwj._00_AST.other.automation.test;

import java.lang.annotation.*;

/**
 * Date: 2024/3/17
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

    String value();
}
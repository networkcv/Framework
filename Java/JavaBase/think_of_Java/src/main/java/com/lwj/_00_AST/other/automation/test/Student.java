package com.lwj._00_AST.other.automation.test;

import com.lwj._00_AST.other.automation.test.other_package.StudentCard;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Date: 2024/3/15
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Data
@SuperBuilder
public class Student extends Person {
    /**
     * 姓名
     */
    @Value("姓名不能为空")
    private String name;
    /**
     * 学生卡
     */
    private List<? super StudentCard> studentCard;
    /**
     * 年龄
     */
    private int age;

    /**
     * 老师
     */
    private List<Teacher> teachers;

    /**
     * 地址
     */
    private List<Address> address;


//    private List<G> t;

//    private List<? extends G> t2;

    @Data
    @Builder
    public static class Address {
        /**
         * 地址id
         */
        private int addressId;
        /**
         * 地址名称
         */
        private String name;
    }

}




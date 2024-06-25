package com.lwj.bytecode;

/**
 * Date: 2024/6/25
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public enum SwitchEnum {
    A(11),
    B(22),
    C(33),
    D(44),
    ;

    private final int value;

    SwitchEnum(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}

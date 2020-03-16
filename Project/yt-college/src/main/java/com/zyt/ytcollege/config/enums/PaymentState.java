package com.zyt.ytcollege.config.enums;

/**
 * create by lwj on 2020/3/16
 */
public enum PaymentState {
    NO_PAY(0), PAY(1), REFUND(2);
    public final int state;

    PaymentState(int state) {
        this.state = state;
    }
}

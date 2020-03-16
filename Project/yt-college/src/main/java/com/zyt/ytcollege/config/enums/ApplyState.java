package com.zyt.ytcollege.config.enums;

/**
 * create by lwj on 2020/3/16
 */
public enum ApplyState {
    NEED_DISPOSE(0), DISPOSED(1);
    public final int state;

    private ApplyState(int state) {
        this.state = state;
    }
}

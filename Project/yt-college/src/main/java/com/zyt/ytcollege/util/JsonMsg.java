package com.zyt.ytcollege.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * create by lwj on 2020/3/14
 */
@Data
@NoArgsConstructor
public class JsonMsg implements Serializable {
    private static final long serialVersionUID = -8504088610147522833L;
    private Integer state;
    private String msg;
    private Object data;

    public JsonMsg(Integer state) {
        this.state = state;
    }

    public JsonMsg(Integer state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public JsonMsg(Integer state, String msg, Object data) {
        super();
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

}

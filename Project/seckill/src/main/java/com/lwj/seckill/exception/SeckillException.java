package com.lwj.seckill.exception;

/**
 * create by lwj on 2019/11/27
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

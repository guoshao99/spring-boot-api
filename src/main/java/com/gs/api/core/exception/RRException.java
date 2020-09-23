package com.gs.api.core.exception;

/**
 * @Description TODO 自定义异常，捕获异常，并返回json
 * @Author GuoShao
 * @Date 2020/9/23 19:13
 * @Version 1.0
 **/
public class RRException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RRException(String msg) {
        super(msg);
    }

}

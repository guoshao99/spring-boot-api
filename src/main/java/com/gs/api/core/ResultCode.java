package com.gs.api.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(200,"操作成功"),//成功
    FAILURE(0,"操作失败"),

    //web
    WEB_400(400,"错误请求"),
    WEB_401(401,"访问未授权"),
    WEB_403(401,"访问未授权"),
    WEB_404(404,"资源未找到"),
    WEB_500(500,"服务器内部错误"),
    WEB_UNKOWN(999,"未知错误"),

    //参数类型
    ARG_TYPE_MISMATCH(1000,"参数类型错误"),
    ARG_BIND_EXCEPTION(1001,"参数绑定错误"),
    ARG_VIOLATION(1002,"参数不符合要求"),
    ARG_MISSING(1003,"参数未找到"),

    //签名类型
    SIGN_NO_APPID(10001, "appId不能为空"),
    SIGN_NO_TIMESTAMP(10002, "timestamp不能为空"),
    SIGN_NO_SIGN(10003, "sign不能为空"),
    SIGN_NO_NONCE(10004, "nonce不能为空"),
    SIGN_TIMESTAMP_INVALID(10005, "timestamp无效"),
    SIGN_DUPLICATION(10006, "重复的请求"),
    SIGN_VERIFY_FAIL(10007, "sign签名校验失败"),;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}

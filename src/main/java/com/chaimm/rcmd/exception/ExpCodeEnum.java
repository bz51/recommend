package com.chaimm.rcmd.exception;


import java.io.Serializable;


/**
 * @Author 大闲人柴毛毛
 * @Date 2017/10/27 下午10:37
 * 全局的异常状态码 和 异常描述
 *
 */
public enum ExpCodeEnum implements Serializable {

    /** 通用异常 */
    UNKNOW_ERROR("000", "未知异常"),
    ERROR_404("001", "没有该接口"),
    PARAM_NULL("002", "参数为空"),
    NO_REPEAT("003", "请勿重复提交"),
    SESSION_NULL("004", "请求头中SessionId不存在"),
    HTTP_REQ_METHOD_ERROR("005", "HTTP请求方法不正确"),
    JSONERROR("006", "JSON解析异常"),
    USER_NO_EXIST("006", "wxid对应的用户不存在"), ARTICLE_NO_EXIST("007", "文章不存在"), DAYS_INVALID("008", "days必须大于0");


    private String code;
    private String message;

    private ExpCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    ExpCodeEnum(){}

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

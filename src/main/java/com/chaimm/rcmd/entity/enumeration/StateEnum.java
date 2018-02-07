package com.chaimm.rcmd.entity.enumeration;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午8:35
 * @description
 */
public enum StateEnum implements BaseEnum {
    OFF(0, "已删除"),
    ON(1, "显示中"),
    ;

    private int code;
    private String msg;

    StateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMsg() {
        return null;
    }
}

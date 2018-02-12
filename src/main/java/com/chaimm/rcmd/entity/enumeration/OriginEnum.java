package com.chaimm.rcmd.entity.enumeration;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/6 下午5:02
 * @description 内容来源
 */
public enum OriginEnum implements BaseEnum {
    CSDN(1, "CSDN"),
    INFOQ(2, "INFOQ"),
    JUEJIN(3, "掘金"),
    ;

    private int code;
    private String msg;

    OriginEnum(int code, String msg) {
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

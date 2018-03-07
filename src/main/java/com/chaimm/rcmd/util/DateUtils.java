package com.chaimm.rcmd.util;

import java.util.TimeZone;

/**
 * @author 大闲人柴毛毛
 * @date 2018/3/7 下午1:46
 * @description
 */
public class DateUtils {
    /**
     * 获取今天零点零分零秒的毫秒数
     * @return
     */
    public static Long getTodayMillis() {

        // 当前时间毫秒数
        long current = System.currentTimeMillis();

        // 今天零点零分零秒的毫秒数
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();

        return Long.valueOf(zero);
    }
}

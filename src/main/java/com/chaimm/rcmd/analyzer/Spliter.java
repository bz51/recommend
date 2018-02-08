package com.chaimm.rcmd.analyzer;

import com.chaimm.rcmd.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/8 下午8:26
 *
 * @description 分词器
 * 使用自己搭建的分词服务，采用HTTP协议交互
 */
@Component
public class Spliter {

    @Value("${spliter.url}")
    private String url;

    @Value("${spliter.param}")
    private String param;

    public List<String> splitWord(String sentence) {
        String resultJsonStr = HttpUtil.sendGet(url, param + sentence);
//        JSON
        // TODO 等分词服务器搭建完后继续写……
        return null;
    }
}

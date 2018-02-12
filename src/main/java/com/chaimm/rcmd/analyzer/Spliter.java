package com.chaimm.rcmd.analyzer;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/8 下午8:26
 *
 * @description 分词器
 * 使用自己搭建的分词服务，采用HTTP协议交互
 */
public class Spliter {


    /**
     * 分词
     * @param sentenceList 待分词句子
     * @return
     */
    public static List<String> splitWord(List<String> sentenceList) {

        List<String> wordList = Lists.newArrayList();
        JiebaSegmenter segmenter = new JiebaSegmenter();

        if (!CollectionUtils.isEmpty(sentenceList)) {
            for (String sentence : sentenceList) {
                List<SegToken> segTokenList = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
                if (!CollectionUtils.isEmpty(segTokenList)) {
                    for (SegToken segToken : segTokenList) {
                        wordList.add(segToken.word);
                    }
                }
            }
        }

        return wordList;
    }

    public static void main(String[] args) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"语言 & 开发"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }
}

package com.chaimm.rcmd.classifier;

import com.chaimm.rcmd.analyzer.Spliter;
import com.chaimm.rcmd.category.CategoryFactory;
import com.chaimm.rcmd.entity.Category;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午11:07
 * @description
 */
@Component
public class Classifier {


    /**
     *
     * @param title
     * @param tagList
     * @return
     */
    public Set<Category> classify(String title, List<String> tagList){

        // 分词
        List<String> wordList = splitWord(title, tagList);

        // 分类
        Set<Category> categorySet = category(wordList);

        return categorySet;
    }


    /**
     * 分类
     * @param wordList 待分类的词
     * @return
     */
    private Set<Category> category(List<String> wordList) {
        Set<Category> categorySet = Sets.newHashSet();

        if (CollectionUtils.isEmpty(wordList)) {
            return categorySet;
        }

        // 将标签 挨个匹配各个类别
        for (String word : wordList) {
            for (Category category : CategoryFactory.CATEGORY_SET_MAP.keySet()) {
                Set<String> categoryWordSet = CategoryFactory.CATEGORY_SET_MAP.get(category);
                if (categoryWordSet.contains(word)) {
                    categorySet.add(category);
                }
            }
        }

        // 未匹配到分类，则为"其他"
        if (CollectionUtils.isEmpty(categorySet)) {
            categorySet.add(CategoryFactory.OTHER_CATEGORY);
        }

        return categorySet;

    }


    /**
     * 分词
     * @param title 文章标题
     * @param tagList 文章标签列表
     * @return
     */
    private List<String> splitWord(String title, List<String> tagList) {

        tagList.add(title);

        List<String> wordList = Spliter.splitWord(tagList);

        // 分词完成后将tagList清空，减少Redis存储
        tagList.clear();

        return wordList;
    }
}

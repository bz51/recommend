package com.chaimm.rcmd.classifier;

import com.chaimm.rcmd.entity.Category;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
    public List<Category> classify(String title, List<String> tagList){
        // TODO 分类器尚未实现
        return Arrays.asList(new Category("demo", "测试类别", null));
    }
}

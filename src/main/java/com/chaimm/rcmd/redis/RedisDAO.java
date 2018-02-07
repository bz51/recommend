package com.chaimm.rcmd.redis;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 下午2:27
 * @description
 */
@Component
public class RedisDAO {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /** 文章类别集合 */
//    private Map<String, Set<Article>> categoryMap = new HashMap<>();


    /** 所有文章title在Redis中的namespace */
    @Value("${redis.namespace.all-titles}")
    private String allTitlesNamespace;

    /**
     * 添加新文章
     * @param article
     */
    public void addArticle(Article article) {

        List<Category> categoryList = article.getCategoryList();

        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        HashOperations opsForHash = redisTemplate.opsForHash();

        if (!CollectionUtils.isEmpty(categoryList)) {
            // 1. 将文章添加进相应类别下
            for (Category category : categoryList) {
                opsForZSet.add(category.getId(), article, article.getWeight());
            }

            // 2. 将文章标题添加进 所有文章的集合中(供文章重复性检测)(只是用Hash的Key，存储title，value为null，这样判断标题是否存在时时间复杂度低)
            opsForHash.put(allTitlesNamespace, article.getTitle(), null);
        }
    }

    /**
     * 该标题是否存在
     * @param title
     * @return
     */
    public boolean hasArticle(String title) {
        HashOperations opsForHash = redisTemplate.opsForHash();
        return opsForHash.hasKey(allTitlesNamespace, title);
    }

    /**
     * 更新文章（评论数、阅读量、权重、阅读增量、评论增量等）
     * // TODO 单篇文章更新的话时间复杂度太高！考虑使用批量更新，为每个分类维护两个ZSet，一个使用，一个更新，更新完成后交换
     * @param article
     */
    public void updateArticle(Article article) {
        ZSetOperations ops = redisTemplate.opsForZSet();

        List<Category> categoryList = article.getCategoryList();
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (Category category : categoryList) {
                // 1. 删除ZSet中该篇文章
                ops.remove(category.getId(), article);
                // 2. 向ZSet添加该篇文章
//                ops.add(category.getId(), article)
            }
        }

    }

}
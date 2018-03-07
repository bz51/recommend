package com.chaimm.rcmd.redis;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.entity.User;
import com.google.common.collect.Lists;
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
    private RedisTemplate redisTemplate;

    /** 文章类别集合 */
//    private Map<String, Set<Article>> categoryMap = new HashMap<>();


    /** 所有文章title在Redis中的namespace */
    @Value("${redis.namespace.all-article}")
    private String allArticleNamespace;

    /** 用户信息的命名空间 */
    @Value("${redis.namespace.users}")
    private String userNamespace;

    /**
     * 添加新文章
     * @param article
     */
    public void addArticle(Article article) {

        Set<Category> categorySet = article.getCategorySet();

        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        HashOperations opsForHash = redisTemplate.opsForHash();

        if (!CollectionUtils.isEmpty(categorySet)) {
            // 1. 将文章标题添加进相应类别下
            for (Category category : categorySet) {
                opsForZSet.add(category.getId(), article.getTitle(), article.getWeight());
            }

            // 2. 将文章添加进 所有文章的集合中(供文章重复性检测)
            opsForHash.put(allArticleNamespace, article.getTitle(), article);
        }
    }

    /**
     * 该标题是否存在
     * @param title
     * @return
     */
    public boolean hasArticle(String title) {
        HashOperations opsForHash = redisTemplate.opsForHash();
        return opsForHash.hasKey(allArticleNamespace, title);
    }

    /**
     * 更新文章（评论数、阅读量、权重、阅读增量、评论增量等）
     * // TODO 单篇文章更新的话时间复杂度太高！考虑使用批量更新，为每个分类维护两个ZSet，一个使用，一个更新，更新完成后交换
     * @param article
     */
    public void updateArticle(Article article) {
        ZSetOperations ops = redisTemplate.opsForZSet();

        Set<Category> categorySet = article.getCategorySet();
        if (!CollectionUtils.isEmpty(categorySet)) {
            for (Category category : categorySet) {
                // 1. 删除ZSet中该篇文章
                ops.remove(category.getId(), article);
                // 2. 向ZSet添加该篇文章
//                ops.add(category.getId(), article)
            }
        }
    }

    public void readArticles() {
        Set set = redisTemplate.opsForZSet().range("demo", 0, -1);
        System.out.println("文章数量："+set.size());
        List<Article> articleList = Lists.newArrayList();
        for (Object obj : set) {
            Article article = (Article) obj;
            articleList.add(article);
        }
        System.out.println();
    }


    /**
     * 创建用户
     * @param user
     */
    public void addUser(User user) {
        HashOperations ops = redisTemplate.opsForHash();
        ops.put(userNamespace, user.getWxid(), user);
    }

    /**
     * 获取指定用户
     * @param wxid
     * @return
     */
    public User getUser(String wxid) {
        HashOperations ops = redisTemplate.opsForHash();
        return (User) ops.get(userNamespace, wxid);
    }


    /**
     * 根据标题获取文章
     * @return
     */
    public Article getArticleByTitle(String title) {

        HashOperations ops = redisTemplate.opsForHash();
        return (Article) ops.get(allArticleNamespace, title);
    }


    /**
     * 获取指定类别指定数量的文章标题
     * @param categoryId
     * @param num
     * @return
     */
    public Set<String> getTitleByCategory(String categoryId, int start, int num) {

        ZSetOperations ops = redisTemplate.opsForZSet();
        return ops.range(categoryId, 0, start+num-1);

    }


    /**
     * 获取指定类别中文章的总数
     * @param categoryId
     * @return
     */
    public int getArticleCountByCategory(String categoryId) {

        ZSetOperations ops = redisTemplate.opsForZSet();
        return ops.size(categoryId).intValue();
    }
}

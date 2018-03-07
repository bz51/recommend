package com.chaimm.rcmd.recommd;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.User;
import com.chaimm.rcmd.redis.RedisDAO;
import com.chaimm.rcmd.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/9 下午2:54
 *
 * @description 文章推荐器
 */
@Component
public class Recommder {

    @Autowired
    private RedisDAO redisDAO;

    /** 每天最少推荐的文章数量 */
    @Value("${rcmd-min-article-num}")
    private int rcmdMin;

    /**
     * 为用户推荐今日文章
     * @param wxid
     * @return
     */
    public List<String> recommend(String wxid) {

        // 获取用户详情
        User user = redisDAO.getUser(wxid);

        // 获取今天的推荐文章
        List<String> titleList = getTodayArticle(user);
        if (!CollectionUtils.isEmpty(titleList)) {
            return titleList;
        }

        // 计算今天的推荐文章
        titleList = calTodayArticle(user);
        return titleList;
    }


    /**
     * 计算今天的推荐文章
     * @param user
     * @return
     */
    private List<String> calTodayArticle(User user) {

        // 计算用户感兴趣类别文章数量
        Map<String, Integer> categoryNumMap = calCategoryNum(user);

        // 获取文章
        List<String> titleList = getArticleByCategoryNum(categoryNumMap);

        // 将今日推荐文章加入user对象
        addIntoRedisUser(user, titleList);

        return titleList;
    }



    /**
     * 将今日推荐文章加入user对象
     * @param user
     * @param titleList
     */
    private void addIntoRedisUser(User user, List<String> titleList) {

        // 获取今日零点零时零分零秒的毫秒数
        Long today = DateUtils.getTodayMillis();

        // 更新user
        user.getRecmdTitleMap().put(today, titleList);

        // 更新redis中的user
        redisDAO.addUser(user);
    }


    /**
     * 获取文章
     * @param categoryNumMap
     * @return
     */
    private List<String> getArticleByCategoryNum(Map<String, Integer> categoryNumMap) {

        List<String> titleList = Lists.newArrayList();
        for (String categoryId : categoryNumMap.keySet()) {

            // 获取该类下num篇文章标题
            int num = categoryNumMap.get(categoryId).intValue();
            Set<String> titleSet = redisDAO.getTitleByCategory(categoryId, num);

            if (!CollectionUtils.isEmpty(titleSet)) {
                // 将titleSet——>titleList
                for (String title : titleSet) {
                    titleList.add(title);
                }
            }
        }

        return titleList;
    }


    /**
     * 计算用户感兴趣类别下的文章数量
     * @param user
     * @return
     */
    private Map<String, Integer> calCategoryNum(User user) {

        // 获取用户感兴趣的类别
        Set<String> categorySet = user.getCategoryMap().keySet();

        // 计算每个类别的平均文章数(整数)
        int numPerCategory = rcmdMin / categorySet.size();
        if (numPerCategory == 0) {
            numPerCategory = 1;
        }
        // 平均分配后的余数
        int remainNum = rcmdMin % categorySet.size();

        // 遍历感兴趣的类别
        Map<String, Integer> categoryNumMap = Maps.newHashMap();
        for (String categoryId : categorySet) {
            categoryNumMap.put(categoryId, Integer.valueOf(numPerCategory));
        }

        // 将余数平均分配给所有感兴趣的类别
        if (rcmdMin > categorySet.size()) {
            String[] arr = {""};
            String[] categoryNumMapKeyArr = categorySet.toArray(arr);
            for (int i=0; i<remainNum; i++) {
                int num = categoryNumMap.get(categoryNumMapKeyArr[i]).intValue() + 1;
                categoryNumMap.put(categoryNumMapKeyArr[i], num);
            }
        }

        return categoryNumMap;
    }


    /**
     * 获取今天的推荐文章
     * @param user
     * @return
     */
    private List<String> getTodayArticle(User user) {

        List<String> titleList = Lists.newArrayList();

        // 若整个RecmdTitleMap为空
        if (user.getRecmdTitleMap() == null || user.getRecmdTitleMap().size() <= 0) {
            return titleList;
        }

        // 获取今天零点零分零秒的毫秒数
        Long today = DateUtils.getTodayMillis();

        // 获取今天的推荐文章标题
        titleList = user.getRecmdTitleMap().get(today);

        return titleList;
    }


    /**
     * 根据标题获取对应的文章
     * @param rcmdTitleList
     * @return
     */
    private List<Article> getArticleByTitle(List<String> rcmdTitleList) {
        List<Article> articleList = Lists.newArrayList();

        if (CollectionUtils.isEmpty(rcmdTitleList)) {
            return articleList;
        }

        for (String title : rcmdTitleList) {
            Article article = redisDAO.getArticleByTitle(title);
            if (article != null) {
                articleList.add(article);
            }
        }

        return articleList;
    }

    /**
     * 根据标题获取文章 摘要（不包含文章内容）
     * @param rcmdTitleList
     * @return
     */
    public List<Article> getArticleAbsByTitle(List<String> rcmdTitleList) {

        // 获取文章全部信息
        List<Article> articleList = this.getArticleByTitle(rcmdTitleList);

        // 剔除文章内容
        articleList = filterArticleContent(articleList);

        return articleList;
    }


    /**
     * 剔除文章中的内容
     * @param articleList
     * @return
     */
    private List<Article> filterArticleContent(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                article.setContent(null);
            }
        }

        return articleList;
    }


}

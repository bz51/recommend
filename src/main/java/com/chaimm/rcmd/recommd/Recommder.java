package com.chaimm.rcmd.recommd;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.User;
import com.chaimm.rcmd.redis.RedisDAO;
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
    public List<Article> recommend(String wxid) {

        // 获取用户详情
        User user = redisDAO.getUser(wxid);

        // 获取今天的推荐文章
        List<Article> articleList = getTodayArticle(user);
        if (!CollectionUtils.isEmpty(articleList)) {
            return articleList;
        }

        // 计算今天的推荐文章
        articleList = calTodayArticle(user);
        return articleList;
    }


    /**
     * 计算今天的推荐文章
     * @param user
     * @return
     */
    private List<Article> calTodayArticle(User user) {

        // 计算用户感兴趣类别文章数量
        Map<String, Integer> categoryNumMap = calCategoryNum(user);

        // 获取文章
        List<Article> articleList = getArticleByCategoryNum(categoryNumMap);

        // 将今日推荐文章加入user对象
        addIntoRedisUser(user, articleList);

        return articleList;
    }



    /**
     * 将今日推荐文章加入user对象
     * @param user
     * @param articleList
     */
    private void addIntoRedisUser(User user, List<Article> articleList) {

        // 将List<Article>——>List<String>
        List<String> titleList = Lists.newArrayList();
        for (Article article : articleList) {
            titleList.add(article.getTitle());
        }

        // 获取今日零点零时零分零秒的毫秒数
        long today = getTodayMillis();

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
    private List<Article> getArticleByCategoryNum(Map<String, Integer> categoryNumMap) {

        List<Article> articleList = Lists.newArrayList();
        for (String categoryId : categoryNumMap.keySet()) {
            int num = categoryNumMap.get(categoryId).intValue();
            Set<Article> articleSet = redisDAO.getArticleByCategory(categoryId, num);

            if (!CollectionUtils.isEmpty(articleSet)) {
                for (Article article : articleSet) {
                    articleList.add(article);
                }
            }
        }

        return articleList;
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
            String[] categoryNumMapKeyArr = categoryNumMap.keySet().toArray(arr);
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
    private List<Article> getTodayArticle(User user) {

        List<Article> articleList = Lists.newArrayList();

        // 若整个RecmdTitleMap为空
        if (user.getRecmdTitleMap() == null || user.getRecmdTitleMap().size() <= 0) {
            return articleList;
        }

        // 获取今天零点零分零秒的毫秒数
        Long today = getTodayMillis();

        // 获取今天的推荐文章标题
        List<String> rcmdTitleList = user.getRecmdTitleMap().get(today);

        // 获取标题对应的文章
        articleList = getArticleByTitle(rcmdTitleList);

        return articleList;
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
     * 获取今天零点零分零秒的毫秒数
     * @return
     */
    private Long getTodayMillis() {

        // 当前时间毫秒数
        long current = System.currentTimeMillis();

        // 今天零点零分零秒的毫秒数
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();

        return Long.valueOf(zero);
    }
}

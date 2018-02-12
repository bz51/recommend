package com.chaimm.rcmd.analyzer;

import com.chaimm.rcmd.classifier.Classifier;
import com.chaimm.rcmd.dao.ArticleDAO;
import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.redis.RedisDAO;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午10:27
 *
 * @description 数据解析器的顶层父类
 */
public abstract class Analyzer {

    @Autowired
    protected ArticleDAO articleDAO;

    @Autowired
    protected RedisDAO redisDAO;

    @Autowired
    protected Classifier classifier;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 数据解析器
     * @param articleList 文章列表（此时文章中只有URL和title，但不排除有些解析器已经包含了文章的很多信息）
     */
    public void analysis(List<Article> articleList) {

        // 过滤掉已经存在的文章（根据标题过滤）
        articleList = filterExistArticle(articleList);

        // 获取文章详细信息
        articleList = batchAnalysisArticleDetail(articleList);

        // 计算权重
        articleList = calWeight(articleList);

        // 分类
        articleList = batchClassify(articleList);

        // 插入DB
//        insertDB(articleList);

        // 插入Redis
        insertRedis(articleList);
    }


    /**
     * 批量解析文章详情
     * @param articleList
     * @return
     */
    protected List<Article> batchAnalysisArticleDetail(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                try {
                    // 获取文章详情页Document
                    Document document = Jsoup.connect(article.getUrl()).get();
                    // 解析文章内容
                    analysisArticleDetail(document, article);
                } catch (IOException e) {
                    // TODO 考虑异常如何处理
                    logger.error("###############获取文章详情失败################");
                    e.printStackTrace();
                }
            }
        }
        return articleList;
    }



    /**
     * 文章批量分类
     * @param articleList
     * @return
     */
    protected List<Article> batchClassify(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                Set<Category> categorySet = classifier.classify(article.getTitle(), article.getTagList());
                article.setCategorySet(categorySet);
            }
        }
        return articleList;
    }

    /**
     * 过滤掉已经存在的文章
     * PS：根据标题过滤
     * @param articleList 文章列表
     * @return
     */
    protected List<Article> filterExistArticle(List<Article> articleList) {
        List<Article> articleListFiltered = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                // 若是新文章，则添加
                if (isNewArticle(article)) {
                    articleListFiltered.add(article);
                } else {
                    logger.warn("重复文章："+article.getTitle());
                }
            }
        }


        return articleListFiltered;
    }

    /**
     * 判断该文章是否是新文章
     * @param article
     * @return
     */
    protected boolean isNewArticle(Article article) {
        return !redisDAO.hasArticle(article.getTitle());
    }


    /**
     * 将Document中信息解析成Article
     * @param document
     * @param article
     */
    protected abstract Article analysisArticleDetail(Document document, Article article);


    /**
     * 文章权重计算器
     * @param articleList 文章列表
     * @return
     */
    protected List<Article> calWeight(List<Article> articleList) {
        // TODO 权重计算器尚未完成
        return articleList;
    }


    /**
     * 文章插入Redis
     * @param articleList
     */
    protected void insertRedis(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                redisDAO.addArticle(article);
                logger.info("《"+article.getTitle()+"》-"+article.getCategorySet().toString()+"-入库成功！");
            }
        }
    }


    /**
     * 文章插入DB
     * @param articleList
     */
    protected void insertDB(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                // 插入文章
                articleDAO.createArticle(article);
                // 插入文章-类别
                articleDAO.createCategory(article);
            }
        }
    }

}

package com.chaimm.rcmd.analyzer;


import com.alibaba.fastjson.JSONArray;
import com.chaimm.rcmd.dao.ArticleDAO;
import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.redis.RedisDAO;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午10:32
 *
 * @description CSDN平台的数据解析器
 */
@Component
public class CsdnAnalyzer extends Analyzer {

    @Override
    protected List<Article> batchAnalysisArticleDetail(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                try {
                    // 获取文章详情页Document
                    Document document = Jsoup.connect(article.getUrl()).get();
                    // 解析文章内容
                    article = analysisArticleDetail(document, article);
                } catch (IOException e) {
                    // TODO 考虑异常如何处理
                    e.printStackTrace();
                }
            }
        }
        return articleList;
    }

    /**
     * 解析文章的内容
     * @param document
     * @param article
     * @return
     */
    private Article analysisArticleDetail(Document document, Article article) {
        String content = document.getElementById("article_content").html();
        article.setContent(content);
        return article;
    }

    @Override
    protected void insertRedis(List<Article> articleList) {
        if (!CollectionUtils.isEmpty(articleList)) {
            for (Article article : articleList) {
                redisDAO.addArticle(article);
            }
        }
    }

    @Override
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

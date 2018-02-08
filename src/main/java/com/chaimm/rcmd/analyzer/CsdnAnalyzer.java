package com.chaimm.rcmd.analyzer;


import com.alibaba.fastjson.JSONArray;
import com.chaimm.rcmd.dao.ArticleDAO;
import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.redis.RedisDAO;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     * 解析文章的内容
     * @param document
     * @param article
     * @return
     */
    private Article analysisArticleDetail(Document document, Article article) {
        // 判断新老版本
        if (isOld(document)) {
            return analysisArticleDetailOld(document, article);
        }
        return analysisArticleDetailNew(document, article);

    }

    /**
     * 判断当前文章详情页 是否是 旧版
     * @param document
     * @return
     */
    private boolean isOld(Document document) {
        Elements articleElements = document.getElementsByTag("article");
        if (articleElements.size() <= 0) {
            return true;
        }
        return false;
    }


    /**
     * 解析新的文章详情页
     * @param document
     * @param article
     * @return
     */
    private Article analysisArticleDetailNew(Document document, Article article) {

        // 获取正文
        String content = document.getElementById("article_content").html();
        article.setContent(content);

        // 获取评论数
        int comments = document.getElementsByClass("comment_li_box").size();
        article.setComments(comments);

        // 获取发布时间
        String postTimeStr = document.getElementsByClass("time").get(0).text();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        try {
            Timestamp postTime = new Timestamp(sdf.parse(postTimeStr).getTime());
            article.setPostTime(postTime);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("文章发布日期格式不正确！"+article.getUrl());
        }

        // 设置爬取时间
        article.setCrawlTime(new Timestamp(System.currentTimeMillis()));

        // 获取标签
        Elements tagElements = document.getElementsByClass("article_tags");
        if (tagElements.size() > 0) {
            Elements aElements = tagElements.get(0).getElementsByTag("a");
            List<String> tagList = article.getTagList();
            if (CollectionUtils.isEmpty(tagList)) {
                tagList = Lists.newArrayList();
            }

            for (Element a : aElements) {
                tagList.add(a.text());
            }
        }

        return article;
    }


    /**
     * 解析 旧版 文章详情页
     * @param document
     * @param article
     * @return
     */
    private Article analysisArticleDetailOld(Document document, Article article) {

        // 获取正文
        String content = document.getElementById("article_content").html();
        article.setContent(content);

        // 获取评论数
        String commentsTxt = document.getElementsByClass("link_comments").get(0).text();
        commentsTxt = commentsTxt.substring(3);
        commentsTxt = commentsTxt.substring(0, commentsTxt.length()-1);
        int comments = Integer.parseInt(commentsTxt);
        article.setComments(comments);

        // 获取发布时间
        String postTimeStr = document.getElementsByClass("link_postdate").get(0).text();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Timestamp postTime = new Timestamp(sdf.parse(postTimeStr).getTime());
            article.setPostTime(postTime);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("文章发布日期格式不正确！"+article.getUrl());
        }

        // 设置爬取时间
        article.setCrawlTime(new Timestamp(System.currentTimeMillis()));

        // 获取标签
        Elements tagElements = document.getElementsByClass("link_categories");
        if (tagElements.size() > 0) {
            Elements aElements = tagElements.get(0).getElementsByTag("a");
            List<String> tagList = article.getTagList();
            if (CollectionUtils.isEmpty(tagList)) {
                tagList = Lists.newArrayList();
            }

            for (Element a : aElements) {
                tagList.add(a.text());
            }
        }

        return article;
    }

}

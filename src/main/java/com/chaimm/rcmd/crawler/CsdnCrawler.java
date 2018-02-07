package com.chaimm.rcmd.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chaimm.rcmd.analyzer.CsdnAnalyzer;
import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.util.HttpUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午9:37
 *
 * @description CSDN平台的爬虫
 */
@Component
public class CsdnCrawler extends Crawler {

    /** CSDN平台的数据解析器，负责将爬到的数据解析成Article对象，最终持久化 */
    @Autowired
    private CsdnAnalyzer analyzer;

    @Value("${csdn.thread-pool.core-pool-size}")
    private int csdnThreadPoolCorePoolSize;

    @Value("${csdn.thread-pool.max-pool-size}")
    private int csdnThreadPoolMaxPoolSize;

    @Value("${csdn.thread-pool.keep-alive-time}")
    private int csdnThreadPoolKeepAliveTime;

    @Value("${csdn.repeat-num-per-category}")
    private int repeatNumPerCategory;

    /**
     * 并发规则：为CSDN的每个类别创建一条线程爬取
     */
    @Override
    public void crawl() {
        CsdnCrawlerTask task1 = new CsdnCrawlerTask("news");
        CsdnCrawlerTask task2 = new CsdnCrawlerTask("ai");
        CsdnCrawlerTask task3 = new CsdnCrawlerTask("cloud");
        CsdnCrawlerTask task4 = new CsdnCrawlerTask("blockchain");
        CsdnCrawlerTask task5 = new CsdnCrawlerTask("db");
        CsdnCrawlerTask task6 = new CsdnCrawlerTask("career");
        CsdnCrawlerTask task7 = new CsdnCrawlerTask("game");
        CsdnCrawlerTask task8 = new CsdnCrawlerTask("engineering");
        CsdnCrawlerTask task9 = new CsdnCrawlerTask("web");
        CsdnCrawlerTask task10 = new CsdnCrawlerTask("mobile");
        CsdnCrawlerTask task11 = new CsdnCrawlerTask("iot");
        CsdnCrawlerTask task12 = new CsdnCrawlerTask("ops");

        executor.submit(task1);
        executor.submit(task2);
        executor.submit(task3);
        executor.submit(task4);
        executor.submit(task5);
        executor.submit(task6);
        executor.submit(task7);
        executor.submit(task8);
        executor.submit(task9);
        executor.submit(task10);
        executor.submit(task11);
        executor.submit(task12);
    }

    @Override
    protected void setStartDelayTime() {
        // 启动后立即爬取
        super.startDelayTime = 0;
    }

    @Override
    protected void setPeriod() {
        // 每 24h 爬取一次新文章
        super.period = 24;
    }

    @Override
    protected void setExecutor() {
        this.executor = new ThreadPoolExecutor(
                csdnThreadPoolCorePoolSize,
                csdnThreadPoolMaxPoolSize,
                csdnThreadPoolKeepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }


    /**
     * CSDN平台爬取任务
     * 也就是一条线程爬取数据的过程
     * CSDN平台采用N条线程同时爬取，每条线程爬取一个类别
     */
    private class CsdnCrawlerTask implements Runnable {

        private static final String URL_Prefix = "https://www.csdn.net/api/articles?type=more&category=";
        private static final String URL_Suffix = "&shown_offset=0";

        /** CSDN文章类别 */
        private String type;

        public CsdnCrawlerTask(String type) {
            this.type = type;
        }

        @Override
        public void run() {

            // 每个类别爬取repeatNumPerCategory次后结束
            for (int i=0; i<repeatNumPerCategory; i++) {
                // 发送请求
                // TODO 尚未增加健壮性判断
                String result = HttpUtil.httpsRequest(generateURL(), "GET", "");
                JSONObject resultObject = JSONObject.parseObject(result);
                JSONArray articleJsonArray = resultObject.getJSONArray("articles");

                // 获取文章URL列表(能获取的都获取了)
                List<Article> articleList = analysisURL(articleJsonArray);

                // 交给Csdn数据解析器处理
                analyzer.analysis(articleList);
            }
        }

        /**
         * 解析文章URL(这里可以多解析一些信息)
         * @param articleJsonArray
         * @return
         */
        private List<Article> analysisURL(JSONArray articleJsonArray) {
            List<Article> articleList = Lists.newArrayList();

            if (!CollectionUtils.isEmpty(articleJsonArray)) {
                for (int i=0; i<articleJsonArray.size(); i++) {
                    JSONObject articleJson = articleJsonArray.getJSONObject(i);

                    Article article = new Article();
                    article.setUrl(articleJson.getString("url"));
                    article.setIncreaseViews(articleJson.getInteger("views").intValue() - article.getViews());
                    article.setViews(articleJson.getInteger("views").intValue());
                    article.setIncreaseComments(articleJson.getInteger("comments").intValue() - article.getComments());
                    article.setComments(articleJson.getInteger("comments").intValue());
                    article.setTitle(articleJson.getString("title"));
                    article.setAuthor(articleJson.getString("user_name"));
                    // TODO 尚未解析文章发布时间
                    // 获取标签列表(作为分类器分类的参考依据，本字段不持久化)
                    article.setTagList(analysisTags(articleJson));

                    articleList.add(article);
                }
            }

            return articleList;
        }

        private List<String> analysisTags(JSONObject articleJson) {
            List<String> tagList = Lists.newArrayList();

            String category = articleJson.getString("category");
            String tag = articleJson.getString("tag");
            String strategy = articleJson.getString("strategy");

            if (!StringUtils.isEmpty(tag)) {
                String[] tags = tag.split(",");
                tagList.addAll(Arrays.asList(tags));
            }

            if (!StringUtils.isEmpty(category)) {
                tagList.add(category);
            }

            if (!StringUtils.isEmpty(strategy)) {
                tagList.add(strategy);
            }

            return tagList;
        }

        private String generateURL() {
            return URL_Prefix + type + URL_Suffix;
        }
    }

}

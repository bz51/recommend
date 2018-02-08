package com.chaimm.rcmd.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chaimm.rcmd.analyzer.CsdnAnalyzer;
import com.chaimm.rcmd.entity.Article;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
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

    /** 每个爬虫私有的线程池 */
    private ThreadPoolExecutor executor;

    /** 本平台爬虫的启动时间(h) */
    @Value("${csdn.start-delay-time}")
    private long startDelayTime;

    /** 本平台爬虫的间隔时间执行(h) (从上一次定时任务执行完成后开始计时) */
    @Value("${csdn.period}")
    private long period;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 并发规则：为CSDN的每个类别创建一条线程爬取
     */
    @Override
    public void crawl() {
        // 初始化线程池
        this.initExecutor();

        CsdnCrawlerTask task1 = new CsdnCrawlerTask(1,5);

        executor.submit(task1);
//        executor.submit(task2);
//        executor.submit(task3);
//        executor.submit(task4);
//        executor.submit(task5);
//        executor.submit(task6);
//        executor.submit(task7);
//        executor.submit(task8);
//        executor.submit(task9);
//        executor.submit(task10);
//        executor.submit(task11);
//        executor.submit(task12);
    }


    /**
     * 初始化线程池
     */
    public void initExecutor() {
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

        private static final String URL_Prefix = "http://oldblog.csdn.net/hotarticle.html?page=";

        /** CSDN文章类别 */
        private int startPage;
        private int endPage;


        public CsdnCrawlerTask(int startPage, int endPage) {
            this.startPage = startPage;
            this.endPage = endPage;
        }

        @Override
        public void run() {
            try {
                for (int curPage=startPage; curPage<=endPage; curPage++) {
                    logger.info("=========CSDN-Hot-第"+curPage+"页-开始爬取==========");

                    // 发送请求
                    // TODO 健壮性判断方法有待考虑
                    Document document = Jsoup.connect(URL_Prefix+curPage).get();

                    // 获取文章URL列表(能获取的都获取了)
                    List<Article> articleList = analysisDocument(document);

                    // 交给Csdn数据解析器处理
                    analyzer.analysis(articleList);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("CSDN-Hot-"+startPage+"-"+endPage+"爬取失败！");
            }
        }

        /**
         * 解析文章列表页
         * @param document
         * @return
         */
        private List<Article> analysisDocument(Document document) {
            Elements elements = document.getElementsByClass("blog_list");
            List<Article> articleList = Lists.newArrayList();

            if (!CollectionUtils.isEmpty(elements)) {
                for (Element element : elements) {
                    Article article = new Article();

                    String username = element.getElementsByClass("nickname").get(0).text();
                    article.setAuthor(username);

                    int views = Integer.parseInt(element.getElementsByTag("em").get(0).text());
                    article.setViews(views);

                    Elements allA = element.getElementsByTag("a");
                    // PS：标题所在的<a>为每条文章列表的第三个<a>标签
                    Element aElement = allA.get(2);
                    String title = aElement.text();
                    String url = aElement.attr("href");
                    article.setTitle(title);
                    article.setUrl(url);

                    // PS：类别所在的<a>为每条文章列表的第四个<a>标签(部分文章没有分类)
                    if (allA.size()>3) {
                        Element categoryElement = allA.get(3);
                        String category = categoryElement.text();
                        List<String> tagList = Lists.newArrayList();
                        tagList.add(category);
                        article.setTagList(tagList);
                    }

                    articleList.add(article);
                }
            }
            return articleList;
        }

        /**
         * 解析文章URL(这里可以多解析一些信息)
         * @param articleJsonArray
         * @return
         */
        @Deprecated
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

    }



    /**
     * 供Starter获取本平台爬虫的启动时延
     * @return
     */
    @Override
    public long getStartDelayTime() {
        return startDelayTime;
    }

    /**
     * 供Starter获取本平台爬虫的执行间隔
     * @return
     */
    @Override
    public long getPeriod() {
        return period;
    }

    @Override
    public String getCrawlerName() {
        return "CSDN";
    }

    public int getCsdnThreadPoolCorePoolSize() {
        return csdnThreadPoolCorePoolSize;
    }

    public void setCsdnThreadPoolCorePoolSize(int csdnThreadPoolCorePoolSize) {
        this.csdnThreadPoolCorePoolSize = csdnThreadPoolCorePoolSize;
    }

    public int getCsdnThreadPoolMaxPoolSize() {
        return csdnThreadPoolMaxPoolSize;
    }

    public void setCsdnThreadPoolMaxPoolSize(int csdnThreadPoolMaxPoolSize) {
        this.csdnThreadPoolMaxPoolSize = csdnThreadPoolMaxPoolSize;
    }

    public int getCsdnThreadPoolKeepAliveTime() {
        return csdnThreadPoolKeepAliveTime;
    }

    public void setCsdnThreadPoolKeepAliveTime(int csdnThreadPoolKeepAliveTime) {
        this.csdnThreadPoolKeepAliveTime = csdnThreadPoolKeepAliveTime;
    }

    public void setStartDelayTime(long startDelayTime) {
        this.startDelayTime = startDelayTime;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}

package com.chaimm.rcmd.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chaimm.rcmd.analyzer.CsdnAnalyzer;
import com.chaimm.rcmd.analyzer.InfoqAnalyzer;
import com.chaimm.rcmd.entity.Article;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * @date 2018/2/12 上午11:07
 * @description
 */
@Component
public class InfoqCrawler extends Crawler{


    /** INFOQ平台的数据解析器，负责将爬到的数据解析成Article对象，最终持久化 */
    @Autowired
    @Qualifier("InfoqAnalyzer")
    private InfoqAnalyzer analyzer;

    @Value("${infoq.thread-pool.core-pool-size}")
    private int threadPoolCorePoolSize;

    @Value("${infoq.thread-pool.max-pool-size}")
    private int threadPoolMaxPoolSize;

    @Value("${infoq.thread-pool.keep-alive-time}")
    private int threadPoolKeepAliveTime;

    /** 每个爬虫私有的线程池 */
    private ThreadPoolExecutor executor;

    /** 本平台爬虫的启动时间(h) */
    @Value("${infoq.start-delay-time}")
    private long startDelayTime;

    /** 本平台爬虫的间隔时间执行(h) (从上一次定时任务执行完成后开始计时) */
    @Value("${infoq.period}")
    private long period;




    @Override
    public void crawl() {
        this.initExecutor();

//        CrawlerTask crawlerTask1 = new CrawlerTask(1,10);
        CrawlerTask crawlerTask2 = new CrawlerTask(11,20);
        CrawlerTask crawlerTask3 = new CrawlerTask(21,30);
        CrawlerTask crawlerTask4 = new CrawlerTask(31,40);
        CrawlerTask crawlerTask5 = new CrawlerTask(41,50);
        CrawlerTask crawlerTask6 = new CrawlerTask(51,60);
        CrawlerTask crawlerTask7 = new CrawlerTask(61,70);
        CrawlerTask crawlerTask8 = new CrawlerTask(71,80);
        CrawlerTask crawlerTask9 = new CrawlerTask(81,90);
        CrawlerTask crawlerTask10 = new CrawlerTask(91,100);
        CrawlerTask crawlerTask11 = new CrawlerTask(101,110);

//        executor.submit(crawlerTask1);
        executor.submit(crawlerTask2);
        executor.submit(crawlerTask3);
        executor.submit(crawlerTask4);
        executor.submit(crawlerTask5);
        executor.submit(crawlerTask6);
        executor.submit(crawlerTask7);
        executor.submit(crawlerTask8);
        executor.submit(crawlerTask9);
        executor.submit(crawlerTask10);
        executor.submit(crawlerTask11);
    }

    /**
     * 初始化线程池
     */
    @Override
    protected void initExecutor() {
        this.executor = new ThreadPoolExecutor(
                threadPoolCorePoolSize,
                threadPoolMaxPoolSize,
                threadPoolKeepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }


    /**
     * Infoq平台爬取任务
     * 也就是一条线程爬取数据的过程
     */
    private class CrawlerTask implements Runnable {

        private static final String URL_Prefix = "http://www.infoq.com/cn/articles/";

        /** 当前任务的起始-终止页码文章类别 */
        private int startPage;
        private int endPage;


        public CrawlerTask(int startPage, int endPage) {
            this.startPage = startPage;
            this.endPage = endPage;
        }

        @Override
        public void run() {
            try {
                for (int curPage=startPage; curPage<=endPage; curPage++) {
                    logger.info("=========Infoq-第"+curPage+"页-开始爬取==========");

                    // 发送请求
                    // TODO 健壮性判断方法有待考虑
                    Document document = Jsoup.connect(URL_Prefix+ (curPage-1)*12).get();

                    // 获取文章URL列表(能获取的都获取了)
                    List<Article> articleList = analysisDocument(document);

                    // 交给Infoq数据解析器处理
                    analyzer.analysis(articleList);
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Infoq-"+startPage+"-"+endPage+"爬取失败！");
            }
        }

        /**
         * 解析文章列表页
         * @param document
         * @return
         */
        private List<Article> analysisDocument(Document document) {
            List<Article> articleList = Lists.newArrayList();

            Elements type1Elements = document.getElementsByClass("news_type1");
            Elements type2Elements = document.getElementsByClass("news_type2");
            Elements elements = new Elements();
            elements.addAll(type1Elements);
            elements.addAll(type2Elements);

            if (!CollectionUtils.isEmpty(elements)) {
                for (Element element : elements) {
                    Article article = analysisURL(element);
                    articleList.add(article);
                }
            }

            return articleList;
        }

        /**
         * 解析文章URL(这里可以多解析一些信息)
         * @param element
         * @return
         */
        private Article analysisURL(Element element) {
            Article article = new Article();

            // 获取标题 和 URL
            Element h2 = element.getElementsByTag("h2").get(0);
            String title = h2.getElementsByTag("a").get(0).text();
            String url = h2.getElementsByTag("a").get(0).attr("href");
            article.setTitle(title);
            article.setUrl("http://www.infoq.com"+url);

            // 获取作者
            String author = element.getElementsByClass("authors-list").get(0)
                    .getElementsByTag("span").get(0)
                    .getElementsByTag("a").get(0).text();
            article.setAuthor(author);

            // 获取图片
            String picUrl = element.getElementsByTag("img").get(0).attr("src");
            article.setPicUrl(picUrl);

            return article;
        }

    }


    @Override
    public long getStartDelayTime() {
        return this.startDelayTime;
    }

    @Override
    public long getPeriod() {
        return this.period;
    }

    @Override
    public String getCrawlerName() {
        return "INFOQ";
    }


}

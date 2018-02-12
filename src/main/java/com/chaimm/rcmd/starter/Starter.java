package com.chaimm.rcmd.starter;

import com.chaimm.rcmd.category.CategoryFactory;
import com.chaimm.rcmd.crawler.Crawler;
import com.chaimm.rcmd.crawler.CsdnCrawler;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.redis.RedisDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午9:16
 *
 * @description 爬虫启动器，负责定时启动各个平台的爬虫
 * 本类实现CommandLineRunner接口，在Spring初始化完成后调用
 */
@Component
public class Starter implements CommandLineRunner{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 定时任务线程池 */
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    /** 所有平台的爬虫（CSDN爬虫、Infoq爬虫……） */
    @Autowired
    private List<Crawler> crawlerList;

    // TODO 临时使用
    @Autowired
    private RedisDAO redisDAO;


    /**
     * 初始化
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {

        // 启动所有爬虫
        startCrawler();

//        readArticles();
    }


    /**
     * 启动各个平台的爬虫
     */
    private void startCrawler() {
        if (!CollectionUtils.isEmpty(crawlerList)) {
            for (Crawler crawler : crawlerList) {
                scheduledExecutor.scheduleAtFixedRate(crawler, crawler.getStartDelayTime(), crawler.getPeriod(), TimeUnit.HOURS);
                logger.info(crawler.getCrawlerName() + "爬虫启动完毕！");
            }
        }
    }
}

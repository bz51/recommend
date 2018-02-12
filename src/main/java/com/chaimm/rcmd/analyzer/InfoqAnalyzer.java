package com.chaimm.rcmd.analyzer;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.enumeration.OriginEnum;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/12 上午11:19
 * @description
 */
@Component("InfoqAnalyzer")
public class InfoqAnalyzer extends Analyzer {


    @Override
    protected Article analysisArticleDetail(Document document, Article article) {

        // 获取文章内容
        String content = document.getElementsByClass("text_info_article").get(0).html();
        article.setContent(content);

        // 获取时间
        // TODO 还没想好怎么抓 发布时间

        // 设置文章来源
        article.setOrigin(OriginEnum.INFOQ);

        // 设置标签
        String tag = document.getElementsByClass("related__title").get(0)
                                .getElementsByTag("a").get(0)
                                .text();
        List<String> tagList = article.getTagList();
        tagList.add(tag);

        return article;
    }
}

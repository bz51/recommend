package com.chaimm.rcmd.analyzer;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.enumeration.OriginEnum;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

        // 获取时间
        // TODO 还没想好怎么抓 发布时间

        // 设置文章来源
        article.setOrigin(OriginEnum.INFOQ);

        // 设置标签
        Elements tagEles = document.getElementsByClass("related__title");
        if (!tagEles.isEmpty()) {
            String tag = document.getElementsByClass("related__title").get(0)
                    .getElementsByTag("a").get(0)
                    .text();
            List<String> tagList = article.getTagList();
            tagList.add(tag);
        }

        // 获取文章内容
        String content = getContent(document);
        article.setContent(content);

        return article;
    }


    /**
     * 获取文章内容
     * @param document
     * @return
     */
    private String getContent(Document document) {
        Element element = document.getElementsByClass("text_info_article").get(0);

        // 去掉related之后的元素
        element.select(".related").remove();
        element.select("script").remove();
        element.select("#cont_item_primary_topic").remove();
        element.select(".related_sponsors").remove();
        element.select(".clear").remove();
        element.select("#comment_here").remove();
        element.select(".comments").remove();
        element.select(".all_comments").remove();
        element.select("#overlay_comments").remove();
        element.select("#replyPopup").remove();
        element.select("#editCommentPopup").remove();
        element.select("#messagePopup").remove();
        element.select("#responseContent").remove();
        element.select("#contentRatingWidget").remove();
        element.select("#noOfComments").remove();

        return element.html();
    }
}

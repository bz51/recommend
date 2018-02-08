package com.chaimm.rcmd.entity;

import com.chaimm.rcmd.entity.enumeration.OriginEnum;
import com.chaimm.rcmd.entity.enumeration.StateEnum;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/6 下午4:40
 * @description
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /** 来源 */
    private OriginEnum origin;

    private String title;

    private String content;

    /** 原文链接 */
    private String url;

    /** 评论数 */
    private int comments;
    /** 评论增量 */
    private int increaseComments;

    /** 点击量 */
    private int views;
    /** 点击量增量 */
    private int increaseViews;

    /** 作者 */
    private String author;

    /** 抓取时间 */
    private Timestamp crawlTime;

    /** 文章写作时间 */
    private Timestamp postTime;

    /** 文章所属类别 */
    private List<Category> categoryList;

    /** 文章标签列表，仅供分类器参考，不持久化 */
    @Transient
    private List<String> tagList;

    /** 权重 */
    private float weight;

    /** 文章状态 */
    private StateEnum state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OriginEnum getOrigin() {
        return origin;
    }

    public void setOrigin(OriginEnum origin) {
        this.origin = origin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getIncreaseComments() {
        return increaseComments;
    }

    public void setIncreaseComments(int increaseComments) {
        this.increaseComments = increaseComments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getIncreaseViews() {
        return increaseViews;
    }

    public void setIncreaseViews(int increaseViews) {
        this.increaseViews = increaseViews;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Timestamp crawlTime) {
        this.crawlTime = crawlTime;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", origin=" + origin +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", comments=" + comments +
                ", increaseComments=" + increaseComments +
                ", views=" + views +
                ", increaseViews=" + increaseViews +
                ", author='" + author + '\'' +
                ", crawlTime=" + crawlTime +
                ", postTime=" + postTime +
                ", categoryList=" + categoryList +
                ", tagList=" + tagList +
                ", weight=" + weight +
                ", state=" + state +
                '}';
    }
}

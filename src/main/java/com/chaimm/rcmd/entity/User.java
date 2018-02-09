package com.chaimm.rcmd.entity;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/9 下午1:54
 * @description
 */
public class User implements Serializable {

    /** 微信ID */
    private String wxid;

    private String username;

    private Timestamp registerTime;

    /** 用户订阅的文章类别 key-类别ID，value-用户对该类别的感兴趣程度 */
    private Map<String, Object> categorySet;

    /** 日期-文章列表的Map(只存文章标题) */
    private Map<Timestamp, List<String>> articleList;

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Map<String, Object> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Map<String, Object> categorySet) {
        this.categorySet = categorySet;
    }

    public Map<Timestamp, List<String>> getArticleList() {
        return articleList;
    }

    public void setArticleList(Map<Timestamp, List<String>> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "User{" +
                "wxid='" + wxid + '\'' +
                ", username='" + username + '\'' +
                ", registerTime=" + registerTime +
                ", categorySet=" + categorySet +
                ", articleList=" + articleList +
                '}';
    }
}

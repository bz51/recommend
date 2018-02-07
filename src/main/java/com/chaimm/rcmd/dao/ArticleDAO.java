package com.chaimm.rcmd.dao;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午8:43
 * @description
 */
@Mapper
public interface ArticleDAO {

    void createArticle(Article article);

    void createCategory(Article article);

    void updateCommentsAndViews(@Param("views") int views,
                                @Param("comments") int comments,
                                @Param("articleId") String articleId);
}

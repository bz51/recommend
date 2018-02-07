package com.chaimm.rcmd.category;

import com.chaimm.rcmd.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午11:17
 *
 * @description 文章类别工厂（负责管理所有的类别）
 */
public class CategoryFactory {

    public static List<Category> allCategoryList = Arrays.asList(
        new Category("ai", "人工智能", null),
        new Category("blockchain", "区块链", null),
        new Category("architect", "架构", null),
        new Category("db", "数据库", null),
        new Category("front", "前端", null),
        new Category("ops", "运维", null),
        new Category("android", "Android", null),
        new Category("ios", "IOS", null),
        new Category("security", "安全", null),
        new Category("news", "新闻咨询", null),
        new Category("game", "游戏", null),
        new Category("cloud", "云计算/大数据", null),
        new Category("work", "求职", null),
        new Category("life", "职场/生活", null),
        new Category("base", "计算机基础", null),
        new Category("test", "测试", null),
        new Category("product", "产品与运营", null),
        new Category("java", "Java", null),
        new Category("python", "Python", null),
        new Category("cc++", "C/C++", null),
        new Category("language", "编程语言", null),
        new Category("other", "其他", null)
    );
}

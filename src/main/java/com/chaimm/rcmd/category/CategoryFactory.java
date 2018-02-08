package com.chaimm.rcmd.category;

import com.chaimm.rcmd.entity.Category;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午11:17
 *
 * @description 文章类别工厂（负责管理所有的类别）
 */
public class CategoryFactory {

    public static final List<Category> allCategoryList = Arrays.asList(
        new Category("ai", "人工智能", null),
        new Category("blockchain", "区块链", null),
        new Category("architect", "架构", null),
        new Category("db", "数据库", null),
        new Category("front", "前端", null),
        new Category("ops", "运维", null),
        new Category("android", "Android", null),
        new Category("ios", "IOS", null),
        new Category("mobile", "移动开发", null),
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

    /** 人工智能 */
    public static final Set<String> AI_CATEGORY_SET = Sets.newHashSet(
        "人工智能","机器学习","神经网络","自主计算","分类","聚类","卷积","卷积神经网络","决策树",
            "深度学习","自然语言处理","自然语言","监督学习","无监督学习","无监督学习",""
    );

    /** 区块链 */
    public static final Set<String> BLOCKCHAIN_CATEGORY_SET = Sets.newHashSet(
        "区块链"
    );

    /** 架构 */
    public static final Set<String> ARCHITECT_CATEGORY_SET = Sets.newHashSet(
        "架构"
    );

    /** 数据库 */
    public static final Set<String> DB_CATEGORY_SET = Sets.newHashSet(
        "数据库","mysql","oracle"
    );

    /** 前端 */
    public static final Set<String> FRONT_CATEGORY_SET = Sets.newHashSet(
        "前端",
        "echats"
    );

    /** 运维 */
    public static final Set<String> OPS_CATEGORY_SET = Sets.newHashSet(
        "运维"
    );

    /** Android */
    public static final Set<String> ANDROID_CATEGORY_SET = Sets.newHashSet(
        "android","安卓"
    );

    /** IOS */
    public static final Set<String> IOS_CATEGORY_SET = Sets.newHashSet(
        "ios"
    );

    /** 移动开发 */
    public static final Set<String> MOBILE_CATEGORY_SET = Sets.newHashSet(
        "ios","android","安卓","移动开发"
    );

    /** 安全 */
    public static final Set<String> SECURITY_CATEGORY_SET = Sets.newHashSet(
        "安全"
    );

    /** 新闻 */
    public static final Set<String> NEWS_CATEGORY_SET = Sets.newHashSet(
        "咨询","新闻"
    );

    /** 游戏 */
    public static final Set<String> GAME_CATEGORY_SET = Sets.newHashSet(
        "游戏"
    );

    /** 云计算/大数据 */
    public static final Set<String> CLOUD_CATEGORY_SET = Sets.newHashSet(
        "云计算","大数据"
    );

    /** 工作/求职 */
    public static final Set<String> WORK_CATEGORY_SET = Sets.newHashSet(
        "面试","面筋","面经","职场"
    );

    /** 生活 */
    public static final Set<String> LIFE_CATEGORY_SET = Sets.newHashSet(
        "生活"
    );

    /** 计算机基础 */
    public static final Set<String> BASE_CATEGORY_SET = Sets.newHashSet(
        "算法","数据结构","计算机网络","网络"
    );

    /** 测试 */
    public static final Set<String> TEST_CATEGORY_SET = Sets.newHashSet(
        "测试"
    );

    /** 产品 */
    public static final Set<String> PRODUCT_CATEGORY_SET = Sets.newHashSet(
        "产品","运营"
    );

    /** python */
    public static final Set<String> PYTHON_CATEGORY_SET = Sets.newHashSet(
        "python"
    );

    /** Java */
    public static final Set<String> JAVA_CATEGORY_SET = Sets.newHashSet(
        "java"
    );

    /** c/c++ */
    public static final Set<String> CC_CATEGORY_SET = Sets.newHashSet(
        "c","c++"
    );

    /** 语言 */
    public static final Set<String> LANGUAGE_CATEGORY_SET = Sets.newHashSet(
        "编程语言","java","python","c","c++","js","javascript"
    );

    /** 其他 */
    public static final Set<String> OTHER_CATEGORY_SET = Sets.newHashSet(

    );

    public static final List<Set<String>> CATEGORY_SET_LIST = Lists.newArrayList(
            AI_CATEGORY_SET,BLOCKCHAIN_CATEGORY_SET,ARCHITECT_CATEGORY_SET,DB_CATEGORY_SET,FRONT_CATEGORY_SET,
            OPS_CATEGORY_SET,ANDROID_CATEGORY_SET,IOS_CATEGORY_SET,MOBILE_CATEGORY_SET,SECURITY_CATEGORY_SET,
            NEWS_CATEGORY_SET,GAME_CATEGORY_SET,CLOUD_CATEGORY_SET,WORK_CATEGORY_SET,LIFE_CATEGORY_SET,
            BASE_CATEGORY_SET,TEST_CATEGORY_SET,PRODUCT_CATEGORY_SET,PYTHON_CATEGORY_SET,JAVA_CATEGORY_SET,
            CC_CATEGORY_SET,LANGUAGE_CATEGORY_SET,OTHER_CATEGORY_SET
    );

}

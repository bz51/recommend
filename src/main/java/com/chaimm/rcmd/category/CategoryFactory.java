package com.chaimm.rcmd.category;

import com.chaimm.rcmd.entity.Category;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/7 上午11:17
 *
 * @description 文章类别工厂（负责管理所有的类别）
 */
public class CategoryFactory {

    private static final Category AI_CATEGORY = new Category("ai", "人工智能", null);
    private static final Category BLOCKCHAIN_CATEGORY = new Category("blockchain", "区块链", null);
    private static final Category ARCHITECT_CATEGORY = new Category("architect", "架构", null);
    private static final Category DB_CATEGORY = new Category("db", "数据库", null);
    private static final Category FRONT_CATEGORY = new Category("front", "前端", null);
    private static final Category OPS_CATEGORY = new Category("ops", "运维", null);
    private static final Category ANDROID_CATEGORY = new Category("android", "Android", null);
    private static final Category IOS_CATEGORY = new Category("ios", "IOS", null);
    private static final Category MOBILE_CATEGORY = new Category("mobile", "移动开发", null);
    private static final Category SECURITY_CATEGORY = new Category("security", "安全", null);
    private static final Category NEWS_CATEGORY = new Category("news", "新闻咨询", null);
    private static final Category GAME_CATEGORY = new Category("game", "游戏", null);
    private static final Category CLOUD_CATEGORY = new Category("cloud", "云计算/大数据", null);
    private static final Category WORK_CATEGORY = new Category("work", "求职", null);
    private static final Category LIFE_CATEGORY = new Category("life", "职场/生活", null);
    private static final Category BASE_CATEGORY = new Category("base", "计算机基础", null);
    private static final Category TEST_CATEGORY = new Category("test", "测试", null);
    private static final Category PRODUCT_CATEGORY = new Category("product", "产品与运营", null);
    private static final Category JAVA_CATEGORY = new Category("java", "Java", null);
    private static final Category PYTHON_CATEGORY = new Category("python", "Python", null);
    private static final Category CC_CATEGORY = new Category("cc++", "C/C++", null);
    private static final Category LANGUAGE_CATEGORY = new Category("language", "编程语言", null);
    public static final Category OTHER_CATEGORY = new Category("other", "其他", null);

    public static final List<Category> allCategoryList = Arrays.asList(
        AI_CATEGORY,BLOCKCHAIN_CATEGORY,ARCHITECT_CATEGORY,DB_CATEGORY,FRONT_CATEGORY,OPS_CATEGORY,
            ANDROID_CATEGORY,IOS_CATEGORY,MOBILE_CATEGORY,SECURITY_CATEGORY,NEWS_CATEGORY,GAME_CATEGORY,
            CLOUD_CATEGORY,WORK_CATEGORY,LIFE_CATEGORY,BASE_CATEGORY,TEST_CATEGORY,PRODUCT_CATEGORY,
            JAVA_CATEGORY,PYTHON_CATEGORY,CC_CATEGORY,LANGUAGE_CATEGORY,OTHER_CATEGORY
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

    public static final Map<Category, Set<String>> CATEGORY_SET_MAP = Maps.newHashMap();

    static {
        CATEGORY_SET_MAP.put(AI_CATEGORY,AI_CATEGORY_SET);
        CATEGORY_SET_MAP.put(BLOCKCHAIN_CATEGORY,BLOCKCHAIN_CATEGORY_SET);
        CATEGORY_SET_MAP.put(ARCHITECT_CATEGORY,ARCHITECT_CATEGORY_SET);
        CATEGORY_SET_MAP.put(DB_CATEGORY,DB_CATEGORY_SET);
        CATEGORY_SET_MAP.put(FRONT_CATEGORY,FRONT_CATEGORY_SET);
        CATEGORY_SET_MAP.put(OPS_CATEGORY,OPS_CATEGORY_SET);
        CATEGORY_SET_MAP.put(ANDROID_CATEGORY,ANDROID_CATEGORY_SET);
        CATEGORY_SET_MAP.put(IOS_CATEGORY,IOS_CATEGORY_SET);
        CATEGORY_SET_MAP.put(MOBILE_CATEGORY,MOBILE_CATEGORY_SET);
        CATEGORY_SET_MAP.put(SECURITY_CATEGORY,SECURITY_CATEGORY_SET);
        CATEGORY_SET_MAP.put(NEWS_CATEGORY,NEWS_CATEGORY_SET);
        CATEGORY_SET_MAP.put(GAME_CATEGORY,GAME_CATEGORY_SET);
        CATEGORY_SET_MAP.put(CLOUD_CATEGORY,CLOUD_CATEGORY_SET);
        CATEGORY_SET_MAP.put(WORK_CATEGORY,WORK_CATEGORY_SET);
        CATEGORY_SET_MAP.put(LIFE_CATEGORY,LIFE_CATEGORY_SET);
        CATEGORY_SET_MAP.put(CC_CATEGORY,CC_CATEGORY_SET);
        CATEGORY_SET_MAP.put(BASE_CATEGORY,BASE_CATEGORY_SET);
        CATEGORY_SET_MAP.put(TEST_CATEGORY,TEST_CATEGORY_SET);
        CATEGORY_SET_MAP.put(PRODUCT_CATEGORY,PRODUCT_CATEGORY_SET);
        CATEGORY_SET_MAP.put(PYTHON_CATEGORY,PYTHON_CATEGORY_SET);
        CATEGORY_SET_MAP.put(JAVA_CATEGORY,JAVA_CATEGORY_SET);
        CATEGORY_SET_MAP.put(LANGUAGE_CATEGORY,LANGUAGE_CATEGORY_SET);
        CATEGORY_SET_MAP.put(OTHER_CATEGORY,OTHER_CATEGORY_SET);
    }
}

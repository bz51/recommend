package com.chaimm.rcmd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chaimm.rcmd.category.CategoryFactory;
import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Category;
import com.chaimm.rcmd.entity.Result;
import com.chaimm.rcmd.entity.User;
import com.chaimm.rcmd.exception.CommonBizException;
import com.chaimm.rcmd.exception.ExpCodeEnum;
import com.chaimm.rcmd.recommd.Recommder;
import com.chaimm.rcmd.redis.RedisDAO;
import com.chaimm.rcmd.util.HttpClientUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/9 下午2:11
 * @description
 */

@RestController
public class Controller {

    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private Recommder recommder;

    @Value("#{WX.APPID}")
    private String APPID;
    @Value("#{WX.SECRET}")
    private String SECRET;
    @Value("#{WX.WxLoginUrl}")
    private String WxLoginUrl;


    /**
     * 获取所有类别
     * @return
     */
    @GetMapping("getAllCategories")
    public Result<List<Category>> getAllCategories() {
        return Result.newSuccessResult(CategoryFactory.allCategoryList);
    }


    /**
     * 创建用户 并 为让用户选择类别
     * @param wxid
     * @param categoryIds
     * @return
     */
    @GetMapping("createUserAndSelectCategory")
    public Result<List<String>> createUserAndSelectCategory(String wxid, String categoryIds) {

        // 创建用户
        createUser(wxid);

        // 为用户添加感兴趣的类别
        return createCategoryForUser(wxid, categoryIds);
    }


    /**
     * 创建用户
     * @param wxid 微信ID
     * @return
     */
    @GetMapping("/addUser")
    public Result createUser(String wxid) {

        if (StringUtils.isEmpty(wxid)) {
            return Result.newFailureResult(new CommonBizException(ExpCodeEnum.PARAM_NULL));
        }

        User user = new User();
        user.setWxid(wxid);
        redisDAO.addUser(user);

        return Result.newSuccessResult();
    }


    /**
     * 为用户添加感兴趣的类别
     * @param wxid
     * @param categoryIds 例如：ai,java,cc
     * @return
     */
    @GetMapping("selectCategory")
    public Result createCategoryForUser(String wxid, String categoryIds) {

        // 0. 参数校验
        checkParam(wxid, categoryIds);

        // 1. 为用户设置感兴趣的文章列表
        setCategoryForUser(wxid, categoryIds);

        // 2. 为该用户推荐今日文章
        List<String> titleList = recommder.recommend(wxid);

        return Result.newSuccessResult(titleList);
    }



    /**
     * 根据标题获取文章详情
     * // TODO 本接口未来可以记录用户是否阅读该篇文章，用于推荐分析
     * @param title
     * @param wxid 用于记录用户是否阅读的该篇文章
     * @return
     */
    @GetMapping("/getArticleDetailByTitle")
    public Result<Article> getArticleDetailByTitle(String title, String wxid) {

        if (StringUtils.isEmpty(title)) {
            throw new CommonBizException(ExpCodeEnum.PARAM_NULL);
        }

        // 获取Article
        Article article = redisDAO.getArticleByTitle(title);
        if (article==null) {
            throw new CommonBizException(ExpCodeEnum.ARTICLE_NO_EXIST);
        }

        return Result.newSuccessResult(article);
    }


    /**
     * 获取指定天数的文章
     * @param wxid
     * @param days
     * @return
     */
    @GetMapping("getTitlesByDay")
    public Result<Map<Long,List<String>>> getTitlesByDay(String wxid, Integer days) {

        checkParam(wxid, days);

        // 获取历史推荐
        Map<Long,List<String>> recmdTitleAllMap = redisDAO.getUser(wxid).getRecmdTitleMap();

        // 只取前day天的推荐
        Map<Long,List<String>> recmdTitlePreDaysMap = Maps.newTreeMap();
        int count = 0;
        for (Long key : recmdTitleAllMap.keySet()) {
            if (count < days) {
                recmdTitlePreDaysMap.put(key, recmdTitleAllMap.get(key));
            } else {
                break;
            }
        }

        return Result.newSuccessResult(recmdTitlePreDaysMap);

    }


    /**
     * 获取微信openID
     * @param code
     * @return
     */
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> wxLogin(String code){
        Map<String,String> params = Maps.newHashMap();
        String resultJsonStr = new HttpClientUtil().doPost(WxLoginUrl+"?appid="+APPID+"&secret="+SECRET+"&js_code="+code+"&grant_type=authorization_code",params,"utf-8");
        JSONObject resultJson = JSON.parseObject(resultJsonStr);

        // 请求失败
        if (StringUtils.isNotEmpty(resultJson.getString("errcode"))) {
            throw new CommonBizException(ExpCodeEnum.WX_ERROR);
        }

        // 请求成功：获取openid
        String openId = resultJson.getString("openid");

        return Result.newSuccessResult(openId);
    }



    /**
     * 参数校验
     * @param wxid
     * @param categoryIds
     */
    private void checkParam(String wxid, String categoryIds) {

        if (StringUtils.isEmpty(wxid)
                || StringUtils.isEmpty(categoryIds)) {
            throw new CommonBizException(ExpCodeEnum.PARAM_NULL);
        }

        // wxid是否存在
        User user = redisDAO.getUser(wxid);
        if (user == null) {
            throw new CommonBizException(ExpCodeEnum.USER_NO_EXIST);
        }
    }

    private void checkParam(String wxid, Integer days) {

        if (StringUtils.isEmpty(wxid)) {
            throw new CommonBizException(ExpCodeEnum.PARAM_NULL);
        }

        if (days==null || days<=0) {
            throw new CommonBizException(ExpCodeEnum.DAYS_INVALID);
        }

        // wxid是否存在
        User user = redisDAO.getUser(wxid);
        if (user == null) {
            throw new CommonBizException(ExpCodeEnum.USER_NO_EXIST);
        }
    }


    /**
     * 为用户设置感兴趣的文章类别
     * @param wxid
     * @param categoryIds 例如：ai,java,cc
     */
    private void setCategoryForUser(String wxid, String categoryIds) {
        // 构建categoryMap
        Map<String,Object> categoryMap = Maps.newHashMap();
        String[] categoryIdArr = categoryIds.split(",");
        if (categoryIdArr.length > 0) {
            for (String categoryId : categoryIdArr) {
                categoryMap.put(categoryId,null);
            }
        }

        // 从redis获取该用户信息
        User user = redisDAO.getUser(wxid);

        // 将categoryMap放进去
        user.setCategoryMap(categoryMap);

        // 将user放回redis
        redisDAO.addUser(user);
    }
}

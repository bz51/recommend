package com.chaimm.rcmd.controller;

import com.chaimm.rcmd.entity.Article;
import com.chaimm.rcmd.entity.Result;
import com.chaimm.rcmd.entity.User;
import com.chaimm.rcmd.exception.CommonBizException;
import com.chaimm.rcmd.exception.ExpCodeEnum;
import com.chaimm.rcmd.recommd.Recommder;
import com.chaimm.rcmd.redis.RedisDAO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        List<Article> articleList = recommder.recommend(wxid);

        return Result.newSuccessResult(articleList);
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

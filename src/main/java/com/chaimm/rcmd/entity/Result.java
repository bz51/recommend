package com.chaimm.rcmd.entity;

import com.chaimm.rcmd.exception.CommonBizException;
import com.chaimm.rcmd.exception.ExpCodeEnum;

import java.io.Serializable;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/9 下午2:12
 * @description
 */
public class Result<T> implements Serializable {

    /** 执行结果 */
    private boolean isSuccess;

    /** 错误码 */
    private String errorCode;

    /** 错误原因 */
    private String message;

    /** 返回数据 */
    private T data;

    /**
     * 返回成功的结果
     * @param data 需返回的结果
     * @param <T>
     * @return
     */
    public static <T> Result<T> newSuccessResult(T data){
        Result<T> result = new Result<>();
        result.isSuccess = true;
        result.data = data;
        return result;
    }

    /**
     * 返回成功的结果
     * @param <T>
     * @return
     */
    public static <T> Result<T> newSuccessResult(){
        Result<T> result = new Result<>();
        result.isSuccess = true;
        return result;
    }

    /**
     * 返回失败的结果
     * PS：返回"未知异常"
     * @param <T>
     * @return
     */
    public static <T> Result<T> newFailureResult(){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorCode = ExpCodeEnum.UNKNOW_ERROR.getCode();
        result.message = ExpCodeEnum.UNKNOW_ERROR.getMessage();
        return result;
    }

    /**
     * 返回失败的结果
     * @param commonBizException 异常
     * @param <T>
     * @return
     */
    public static <T> Result<T> newFailureResult(CommonBizException commonBizException){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorCode = commonBizException.getCodeEnum().getCode();
        result.message = commonBizException.getCodeEnum().getMessage();
        return result;
    }

    /**
     * 返回失败的结果
     * @param commonBizException 异常
     * @param data 需返回的数据
     * @param <T>
     * @return
     */
    public static <T> Result<T> newFailureResult(CommonBizException commonBizException, T data){
        Result<T> result = new Result<>();
        result.isSuccess = false;
        result.errorCode = commonBizException.getCodeEnum().getCode();
        result.message = commonBizException.getCodeEnum().getMessage();
        result.data = data;
        return result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "isSuccess=" + isSuccess +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

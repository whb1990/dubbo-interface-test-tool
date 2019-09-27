package com.whb.dubbo.dto;

import com.alibaba.dubbo.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应结果实体类
 *
 * @param <T>
 */
@Data
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 异常码
     */
    private static final int DEFAULT_EXCEPTION_CODE = -1;
    /**
     * 成功码
     */
    private static final int DEFAULT_SUCCESS_CODE = 1;
    /**
     * 错误码
     */
    private static final int DEFAULT_ERROR_CODE = 0;

    /**
     * 响应码
     */
    private int code;
    /**
     * 执行结果标识
     */
    private boolean success;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 备注
     */
    private String remark;
    /**
     * 返回的数据内容
     */
    private T data;
    /**
     * 异常
     */
    private Throwable exception;

    /**
     * 处理成功
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> handleSuccess(String msg, T data) {
        return handleResult(DEFAULT_SUCCESS_CODE, true, msg, "success", data, null);
    }

    /**
     * 处理失败
     *
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> handleError(String msg, T data) {
        return handleResult(DEFAULT_ERROR_CODE, false, msg, "occur an error", data, null);
    }

    /**
     * 处理异常
     *
     * @param msg
     * @param data
     * @param e
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> handleException(String msg, T data, Throwable e) {
        return handleResult(DEFAULT_EXCEPTION_CODE, false, null == msg ? e.getMessage() : msg, "occur an exception", data, e);
    }

    /**
     * 处理结果
     *
     * @param code    响应码
     * @param success 是否成功
     * @param msg     消息
     * @param remark  备注
     * @param data    数据
     * @param e       异常
     * @param <T>
     * @return
     */
    private static <T> ResultDTO<T> handleResult(int code, boolean success, String msg, String remark, T data, Throwable e) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setCode(code);
        result.setSuccess(success);
        result.setMsg(msg);
        result.setRemark(remark);
        result.setData(data);
        result.setException(e);
        return result;
    }

    /**
     * 生成异常结果
     *
     * @param e
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> createExceptionResult(Throwable e, Class<T> clazz) {
        return createExceptionResult("", e, clazz);
    }

    public static <T> ResultDTO<T> createExceptionResult(String msg, Throwable e, Class<T> clazz) {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setCode(DEFAULT_EXCEPTION_CODE);
        ret.setSuccess(false);
        ret.setMsg(StringUtils.isEmpty(msg) ? e.getMessage() : msg);
        ret.setRemark("occur an exception");
        ret.setData(null);
        ret.setException(e);
        return ret;
    }

    /**
     * 生成错误结果
     *
     * @param msg
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> createErrorResult(String msg, Class<T> clazz) {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setCode(DEFAULT_ERROR_CODE);
        ret.setMsg(msg);
        ret.setSuccess(false);
        ret.setRemark("occur an error");
        ret.setData(null);
        ret.setException(null);
        return ret;
    }

    public static <T> ResultDTO<T> createSuccessResult(String msg, Class<T> clazz) {
        return createSuccessResult(msg, null, clazz);
    }

    /**
     * 生成成功结果
     *
     * @param msg
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> createSuccessResult(String msg, T data, Class<T> clazz) {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setCode(DEFAULT_SUCCESS_CODE);
        ret.setSuccess(true);
        ret.setMsg(msg);
        ret.setRemark("success");
        ret.setData(data);
        ret.setException(null);
        return ret;
    }
}

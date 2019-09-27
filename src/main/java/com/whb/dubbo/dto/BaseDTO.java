package com.whb.dubbo.dto;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基础数据传输对象
 */
public class BaseDTO {
    /**
     * 用于生成请求ID
     */
    private static final AtomicLong counter = new AtomicLong();
    /**
     * 请求ID
     */
    private final String requestId;

    public BaseDTO() {
        this.requestId = String.valueOf(counter.getAndAdd(1));
    }

    public String getRequestId() {
        return requestId;
    }
}

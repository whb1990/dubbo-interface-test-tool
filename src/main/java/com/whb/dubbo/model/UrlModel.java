package com.whb.dubbo.model;

import com.alibaba.dubbo.common.URL;
import lombok.Getter;

/**
 * 服务地址对象
 */
@Getter
public class UrlModel {
    /**
     * 缓存的key
     */
    private final String key;
    /**
     * 服务地址
     */
    private final URL url;

    public UrlModel(String key, URL url) {
        this.key = key;
        this.url = url;
    }
}

package com.whb.dubbo.dto;

import lombok.Data;

/**
 * URL数据传输对象
 */
@Data
public class UrlModelDTO {
    /**
     * 缓存的key
     */
    private String key;
    /**
     * 主机
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

}

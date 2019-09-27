package com.whb.dubbo.dto;

import lombok.Data;

/**
 * 连接数据传输对象
 */
@Data
public class ConnectDTO extends BaseDTO {

    /**
     * ip and port.
     */
    private String conn;
    /**
     * 接口名称
     */
    private String serviceName;
    /**
     * 服务提供方的key
     */
    private String providerKey;
    /**
     * 方法缓存的key
     */
    private String methodKey;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数
     */
    private String json;
    /**
     * 等待响应结果的超时时间
     */
    private int timeout;
    /**
     * 接口版本号
     */
    private String version;
    /**
     * 接口所属分组
     */
    private String group;
}

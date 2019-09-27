package com.whb.dubbo.model;

import lombok.Data;

/**
 * 注册中心对象
 */
@Data
public class RegistryModel {
    /**
     * 注册中心的key
     */
    private String registryKey;
    /**
     * 描述
     */
    private String registryDesc;

}

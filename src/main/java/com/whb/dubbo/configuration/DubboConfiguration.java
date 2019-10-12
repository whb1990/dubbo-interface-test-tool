package com.whb.dubbo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: whb
 * @date: 2019/10/12 15:47
 * @description: Dubbo配置信息
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dubbo.dependency")
public class DubboConfiguration {

    private String pom;

    private String lib;

    private Integer timeout;

}

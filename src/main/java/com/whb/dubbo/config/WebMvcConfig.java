package com.whb.dubbo.config;

import com.whb.dubbo.interceptor.WebMvcInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author: whb
 * @date: 2019/9/27 19:50
 * @description: 配置拦截器
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebMvcInterceptor()).addPathPatterns("/**");
    }

}

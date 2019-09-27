package com.whb.dubbo.dto;

import com.whb.dubbo.model.MethodModel;
import lombok.Data;

/**
 * 方法数据传输对象
 */
@Data
public class MethodModelDTO {

    /**
     * 方法隶属的接口名
     */
    private String interfaceName;
    /**
     * 方法缓存的key
     */
    private String methodKey;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 前端页面显示的方法名
     */
    private String methodText;

    public MethodModelDTO() {

    }

    public MethodModelDTO(MethodModel model) {
        this.methodKey = model.getKey();
        this.methodName = model.getMethod().getName();
        this.methodText = model.getMethodText();
    }
}

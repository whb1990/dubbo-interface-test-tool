package com.whb.dubbo.model;

import com.alibaba.dubbo.common.utils.StringUtils;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 方法对象
 */
@Getter
public class MethodModel {
    /**
     * 方法
     */
    private final Method method;
    /**
     * 缓存key
     */
    private final String key;

    public MethodModel(String key, Method method) {
        this.key = key;
        this.method = method;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append("(");

        for (Parameter param : method.getParameters()) {
            sb.append(param.getType().getName());
            sb.append(" ");
            sb.append(param.getName());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");

        return sb.toString();
    }

    /**
     * 用于前端页面展示
     * @return
     */
    public String getMethodText() {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append("(");

        for (Parameter param : method.getParameters()) {
            sb.append(getShortType(param.getType().getName()));
            sb.append(" ");
            sb.append(param.getName());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");

        return sb.toString();
    }

    private String getShortType(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        int index = name.lastIndexOf(".");
        if (index > 0 && index < name.length()) {
            name = name.substring(index + 1);
        }
        return name;
    }

}

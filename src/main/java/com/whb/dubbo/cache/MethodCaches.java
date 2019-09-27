package com.whb.dubbo.cache;

import com.whb.dubbo.dto.MethodModelDTO;
import com.whb.dubbo.model.MethodModel;
import com.whb.dubbo.util.MD5Util;
import com.whb.dubbo.util.StringUtil;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存接口方法
 */
public class MethodCaches {

    private final static Map<String, MethodModel> map = new ConcurrentHashMap<>();

    /**
     * 获取方法列表同时进行缓存
     *
     * @param interfaceName 接口名
     * @param methods       方法集合
     * @return
     */
    public static List<MethodModelDTO> cache(final String interfaceName, Method[] methods) {

        List<MethodModelDTO> ret = new ArrayList<>();

        Arrays.stream(methods).forEach(m -> {
            //生成缓存key
            String key = generateMethodKey(m, interfaceName);
            //封装方法对象
            MethodModel model = new MethodModel(key, m);

            ret.add(new MethodModelDTO(model));
            //缓存
            map.putIfAbsent(key, model);
        });
        return ret;
    }

    /**
     * 生成缓存的key
     *
     * @param method
     * @param interfaceName
     * @return
     */
    private static String generateMethodKey(Method method, String interfaceName) {
        return StringUtil.format("{}#{}", interfaceName, MD5Util.encrypt(method.toGenericString()));
    }

    /**
     * 从缓存中取method
     *
     * @param key
     * @return
     */
    public static MethodModel get(@NotNull String key) {
        return map.get(key);
    }
}

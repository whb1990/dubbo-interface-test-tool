package com.whb.dubbo.cache;

import com.alibaba.dubbo.common.URL;
import com.whb.dubbo.model.UrlModel;
import com.whb.dubbo.util.StringUtil;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存接口库服务地址
 */
public class UrlCaches {

    private final static Map<String, UrlModel> map = new ConcurrentHashMap<>();

    /**
     * 缓存接口服务地址
     *
     * @param interfaceName
     * @param urls
     * @return
     */
    public static List<UrlModel> cache(String interfaceName, List<URL> urls) {

        List<UrlModel> ret = new ArrayList<>();

        for (int i = 0; i < urls.size(); i++) {

            URL url = urls.get(i);
            String key = generateUrlKey(interfaceName, url.getHost(), url.getPort());
            UrlModel model = new UrlModel(key, url);
            ret.add(model);

            map.put(model.getKey(), model);
        }
        return ret;
    }

    /**
     * 生成缓存key
     *
     * @param interfaceName
     * @param host
     * @param port
     * @return
     */
    private static String generateUrlKey(String interfaceName, String host, int port) {
        return StringUtil.format("{}#{}#{}#", interfaceName, host, port);
    }

    /**
     * 从缓存获取指定接口服务地址
     *
     * @param key
     * @return
     */
    public static UrlModel get(@NotNull String key) {
        return map.get(key);
    }
}

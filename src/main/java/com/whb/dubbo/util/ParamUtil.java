package com.whb.dubbo.util;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.PojoUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.model.PointModel;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 方法参数解析工具类
 */
public class ParamUtil {
    /**
     * 从请求地址获取参数
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> getAttachmentFromUrl(URL url) throws Exception {
        String interfaceName = url.getParameter(Constants.INTERFACE_KEY, "");
        if (StringUtils.isEmpty(interfaceName)) {
            throw new RRException("找不到接口名称！");
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.PATH_KEY, interfaceName);
        map.put(Constants.VERSION_KEY, url.getParameter(Constants.VERSION_KEY));
        map.put(Constants.GROUP_KEY, url.getParameter(Constants.GROUP_KEY));
        /**
         * 不需要设置这些参数
         *
         map.put(Constants.SIDE_KEY, Constants.CONSUMER_SIDE);
         map.put(Constants.DUBBO_VERSION_KEY, Version.getVersion());
         map.put(Constants.TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
         map.put(Constants.PID_KEY, String.valueOf(ConfigUtils.getPid()));
         map.put(Constants.METHODS_KEY, methodNames);
         map.put(Constants.INTERFACE_KEY, interfaceName);
         map.put(Constants.VERSION_KEY, "1.0"); // 不能设置这个，不然服务端找不到invoker
         */
        return map;
    }

    /**
     * 解析参数
     *
     * @param jsonStr
     * @param invokeMethod
     * @return
     */
    public static Object[] parseJson(String jsonStr, Method invokeMethod) {
        jsonStr = jsonStr.trim();

        String json;
        if (invokeMethod.getParameters().length > 0) {
            if (StringUtils.isEmpty(jsonStr)) {
                throw new RRException("参数不能为空");
            }
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                json = jsonStr;
            } else {
                json = "[" + jsonStr + "]";
            }
        } else {
            json = jsonStr;
        }

        List<Object> list = JSON.parseArray(json, Object.class);
        Object[] array = PojoUtils.realize(list.toArray(), invokeMethod.getParameterTypes(), invokeMethod.getGenericParameterTypes());

        return array;
    }

    /**
     * 从连接中解析ip和端口
     *
     * @param conn
     * @return
     */
    public static PointModel parsePointModel(@NotNull String conn) {
        String[] pairs = conn.replace("：", ":").split(":");
        String host = pairs[0];
        String port = pairs[1];

        return new PointModel(host, Integer.valueOf(port));
    }
}

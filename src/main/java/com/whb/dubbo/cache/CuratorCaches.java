package com.whb.dubbo.cache;

import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.handler.CuratorHandler;
import com.whb.dubbo.model.PointModel;
import com.whb.dubbo.util.ParamUtil;
import com.whb.dubbo.util.StringUtil;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存zk注册中心
 */
public class CuratorCaches {

    private final static Map<String, CuratorHandler> map = new ConcurrentHashMap<>();

    public static CuratorHandler getHandler(@NotNull String conn) throws NoSuchFieldException, IllegalAccessException {
        //先从缓存中取
        CuratorHandler client = map.get(conn);
        //如果为空
        if (null == client) {
            try {
                // 切割ip和端口
                PointModel model = ParamUtil.parsePointModel(conn);
                //封装zk连接对象
                client = new CuratorHandler("zookeeper", model.getIp(), model.getPort());
                // 连接到zk
                client.doConnect();
                // 异步连接，需要等待一下
                Thread.sleep(1000);
                //连接是否可用
                if (client.isAvailable()) {
                    // 缓存起来以便重复使用
                    map.putIfAbsent(conn, client);
                } else {
                    //关闭连接
                    client.close();
                }
            } catch (Exception e) {
                throw new RRException(StringUtil.format("无法连接到：{}, 原因：{}", conn, e.getMessage()));
            }
        }
        return client;
    }
}

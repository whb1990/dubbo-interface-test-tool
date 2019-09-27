package com.whb.dubbo.handler;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistry;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient;
import com.alibaba.dubbo.remoting.zookeeper.curator.CuratorZookeeperTransporter;
import com.whb.dubbo.cache.MethodCaches;
import com.whb.dubbo.cache.UrlCaches;
import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.MethodModelDTO;
import com.whb.dubbo.exception.RRException;
import com.whb.dubbo.model.ServiceModel;
import com.whb.dubbo.model.UrlModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuratorHandler {
    /**
     * 协议
     */
    private final String protocol;
    /**
     * 主机
     */
    private final String host;
    /**
     * 端口
     */
    private final int port;
    /**
     * zk客户端
     */
    private ZookeeperClient zkClient;
    /**
     * zk注册中心
     */
    private ZookeeperRegistry registry;
    /**
     * 根节点
     */
    private String root = "/dubbo";

    public CuratorHandler(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public void doConnect() throws NoSuchFieldException, IllegalAccessException {

        CuratorZookeeperTransporter zookeeperTransporter = new CuratorZookeeperTransporter();
        URL url = new URL(protocol, host, port);

        registry = new ZookeeperRegistry(url, zookeeperTransporter);

        Field field = registry.getClass().getDeclaredField("zkClient");
        field.setAccessible(true);
        zkClient = (ZookeeperClient) field.get(registry);

    }

    public List<ServiceModel> getInterfaces() {
        List<ServiceModel> ret = new ArrayList<>();
        List<String> list = zkClient.getChildren(root);
        for (int i = 0; i < list.size(); i++) {
            ServiceModel model = new ServiceModel();
            model.setServiceName(list.get(i));
            ret.add(model);
        }

        return ret;
    }

    public List<UrlModel> getProviders(ConnectDTO dto) {

        if (null == dto) {
            throw new RRException("dto can't be null.");
        }
        if (StringUtils.isEmpty(dto.getServiceName())) {
            throw new RRException("service name can't be null.");
        }

        Map<String, String> map = new HashMap<>();
        map.put(Constants.INTERFACE_KEY, dto.getServiceName());

        if (StringUtils.isNotEmpty(dto.getVersion())) {
            map.put(Constants.VERSION_KEY, dto.getVersion());
        }
        if (StringUtils.isNotEmpty(dto.getGroup())) {
            map.put(Constants.GROUP_KEY, dto.getGroup());
        }

        URL url = new URL(protocol, host, port, map);
        List<URL> list = registry.lookup(url);

        return UrlCaches.cache(dto.getServiceName(), list);
    }

    public List<MethodModelDTO> getMethods(String interfaceName) throws ClassNotFoundException {

        Class<?> clazz = Class.forName(interfaceName);
        Method[] methods = clazz.getMethods();

        return MethodCaches.cache(interfaceName, methods);

    }

    public void close() {
        registry.destroy();
    }

    public boolean isAvailable() {
        return registry.isAvailable();
    }
}

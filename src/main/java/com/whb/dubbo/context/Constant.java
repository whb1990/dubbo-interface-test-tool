package com.whb.dubbo.context;

/**
 * 常量定义
 */
public class Constant {

    /**
     * 下载任务的Key
     */
    public static final String DUBBO_DOWNLOAD_JAR_TASK = "dubbo:download:jar:task";
    /**
     * 任务状态--运行中
     */
    public static final int RUNNING_FlAG = 1;
    /**
     * 任务状态--已完成
     */
    public static final int COMPLETE_FLAG = 2;
    /**
     * 下载任务实时消息Key
     */
    public static final String DUBBO_DOWNLOAD_JAR_MESSAGE = "dubbo:download:jar:msg:{}";

    /**
     * 缓存前缀
     */
    public static final String DUBBO_CACHE_PREFIX = "dubbo:cache";
    /**
     * 实例Key
     */
    public static final String DUBBO_CASE_KEY = "dubbo:case";
    /**
     * zk注册地址Key
     */
    public static final String DUBBO_REGISTRY_KEY = "dubbo:registry:list";
}

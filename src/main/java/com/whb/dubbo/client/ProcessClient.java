package com.whb.dubbo.client;

import com.whb.dubbo.cache.RedisResolver;
import com.whb.dubbo.context.Constant;
import com.whb.dubbo.context.TaskContainer;
import com.whb.dubbo.dto.PomDTO;
import com.whb.dubbo.handler.StreamHandler;
import com.whb.dubbo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 进度客户端
 */
@Slf4j
public class ProcessClient extends Thread {
    /**
     * jar包路径
     */
    private final String libPath;
    private final RedisResolver redisResolver;
    /**
     * pom对象
     */
    private final PomDTO dto;
    /**
     * pom路径
     */
    private final String pomXml;
    /**
     * 等待时长
     */
    private long timeout = 20;
    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public ProcessClient(PomDTO dto, RedisResolver redisResolver, String pomXml, String libPath) {
        this.dto = dto;
        this.redisResolver = redisResolver;
        this.pomXml = pomXml;
        this.libPath = libPath;
    }

    @Override
    public void run() {
        log.info("开始下载jar包...");

        // 设置运行标识为运行中
        this.putFlag();

        // 根据操作系统执行相应命令
        String command = makeCommand(pomXml);

        log.info("开始执行maven命令： {}", command);
        Process ps = null;
        try {
            ps = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            log.error(StringUtil.format("无法执行maven命令 {}", command), e);
            return;
        }

        // 再开线程执行
        TaskContainer.getTaskContainer().execute(new StreamHandler(ps, redisResolver, dto.getRequestId(), libPath));

        // 等待20分钟
        try {
            ps.waitFor(timeout, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("waiting too long...", e);
        }

        // 移除运行标识
        this.removeFlag();

        // 执行完毕
        this.done = true;

    }

    /**
     * 将任务标记设置为运行，任务的最长生存期为1小时。
     */
    private void putFlag() {
        log.info("set the key to mark as the running flag and the longest lifetime of task was one hour");
        redisResolver.set(Constant.DOE_DOWNLOAD_JAR_TASK, Constant.RUNNING_FlAG, 1, TimeUnit.HOURS);
    }

    /**
     * 移除运行标识
     */
    private void removeFlag() {
        log.info("remove the running flag.");
        redisResolver.del(Constant.DOE_DOWNLOAD_JAR_TASK);
    }

    /**
     * 执行命令
     *
     * @param pomXml
     * @return
     */
    private String makeCommand(String pomXml) {
        //Linux系统
        if (isOSLinux()) {
            return StringUtil.format("/bin/bash -c  mvn dependency:copy-dependencies -DoutputDirectory={} -DincludeScope=compile -f {}", libPath, pomXml);
        } else if (isOSMac()) {
            //Mac系统
            return StringUtil.format("mvn dependency:copy-dependencies -DoutputDirectory={} -DincludeScope=compile -f {}", libPath, pomXml);
        } else {
            //Windows系统
            return StringUtil.format("cmd /c  mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile -f {}", pomXml);
        }
    }

    /**
     * 判断是否Linux系统
     *
     * @return
     */
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否Mac系统
     *
     * @return
     */
    public static boolean isOSMac() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("mac")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDone() {
        return done;
    }

    /**
     * 是否运行中
     *
     * @return
     */
    public boolean isRunning() {
        if (redisResolver.hasKey(Constant.DOE_DOWNLOAD_JAR_TASK)) {
            log.warn("jar包下载任务正在执行中，请稍后重试.");
            return true;
        }
        return false;
    }
}

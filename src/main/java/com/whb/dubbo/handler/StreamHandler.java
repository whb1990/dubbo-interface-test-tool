package com.whb.dubbo.handler;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.whb.dubbo.cache.RedisResolver;
import com.whb.dubbo.context.Constant;
import com.whb.dubbo.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class StreamHandler implements Runnable {

    private final String libPath;
    private final RedisResolver redisResolver;
    private final String requestId;
    private final Process ps;

    public StreamHandler(Process ps, RedisResolver redisResolver, String requestId, String libPath) {
        this.ps = ps;
        this.redisResolver = redisResolver;
        this.requestId = requestId;
        this.libPath = libPath;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        log.info("begin to put the message into redis.");

        // 获取标准输出
        BufferedReader readStdout = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        // 获取错误输出
        BufferedReader readStderr = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
        try {

            // auto expire
            String key = StringUtil.format(Constant.DUBBO_DOWNLOAD_JAR_MESSAGE, requestId);
            redisResolver.rPush(key, "");
            redisResolver.expire(key, 15 * 60);

            String successLine;
            String errorLine = null;
            while (null != (successLine = readStdout.readLine()) || (errorLine = readStderr.readLine()) != null) {

                if (StringUtils.isNotEmpty(successLine)) {
                    putToRedis(requestId, successLine);
                }
                if (StringUtils.isNotEmpty(errorLine)) {
                    putToRedis(requestId, errorLine);
                }
            }

            log.info("finish download the jars to the path {}.", libPath);

        } catch (Exception e) {

            log.error("occur something wrong when download the jars.", e);

        } finally {

            try {

                readStdout.close();
                readStderr.close();

            } catch (Exception e) {
                log.error("occur something wrong when close resources", e);
            }
        }
    }

    private void putToRedis(String requestId, String message) {

        log.info("{}|{}", requestId, message);

        String key = StringUtil.format(Constant.DUBBO_DOWNLOAD_JAR_MESSAGE, requestId);

        redisResolver.rPush(key, message);

    }
}

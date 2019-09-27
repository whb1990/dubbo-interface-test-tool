package com.whb.dubbo.context;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务容器
 */
@Slf4j
public class TaskContainer {

    /**
     * 获取当前的cpu核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池最大容量
     */
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT;
    /**
     * 线程池核心容量
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT;
    /**
     * 线程池
     */
    private final ThreadPoolExecutor poolExecutor;
    /**
     * 线程池是否关闭
     */
    protected volatile boolean isShutdown;
    /**
     * 任务计数器
     */
    protected CountDownLatch watch;

    private TaskContainer() {
        // 创建任务池
        poolExecutor = new ThreadPoolExecutor(2, MAXIMUM_POOL_SIZE, 1,
                TimeUnit.HOURS, new ArrayBlockingQueue<>(CORE_POOL_SIZE), (r, executor) -> {
            try {
                // 核心改造点，由blocking queue的offer改成put阻塞方法
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.error("任务进入队列出错：", e);
            }
        });
    }

    public static TaskContainer getTaskContainer() {
        return TaskContainerHolder.instance;
    }

    /**
     * 执行任务
     *
     * @param task
     */
    public void execute(Runnable task) {
        poolExecutor.execute(task);
    }

    /**
     * 静态内部类实现的单例
     */
    static class TaskContainerHolder {
        static final TaskContainer instance = new TaskContainer();
    }
}

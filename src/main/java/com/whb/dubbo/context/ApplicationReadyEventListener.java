package com.whb.dubbo.context;

import com.whb.dubbo.service.PomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.MalformedURLException;

/**
 * 监听服务启动开启下载Jar包任务
 */
@Slf4j
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("ApplicationReadyEventListener.onApplicationEvent()");
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();

        PomService pomService = applicationContext.getBean("pomService", PomService.class);
        try {
            log.info("开始自动下载Jar包...");
            pomService.loadJars("");
            log.info("Jar包下载完毕。");
        } catch (NoSuchMethodException | MalformedURLException e) {
            log.error("下载Jar包失败...", e);
        }
    }
}

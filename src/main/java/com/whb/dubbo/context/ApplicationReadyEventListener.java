package com.whb.dubbo.context;

import com.whb.dubbo.configuration.DubboConfiguration;
import com.whb.dubbo.service.PomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;

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
        String workspace = System.getProperty("user.dir");
        String parentDir = workspace.substring(0, workspace.indexOf(":")) + ":" + System.getProperty("file.separator");
        log.info("用户当前工作目录：{}，用户当前工作目录的根目录：{}", workspace, parentDir);

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        DubboConfiguration dubboConfiguration = applicationContext.getBean(DubboConfiguration.class);
        log.info("pomXml:{},libPath:{}", dubboConfiguration.getPom(), dubboConfiguration.getLib());
        //lib路径
        File libFile = new File(parentDir + dubboConfiguration.getLib());
        if (!libFile.exists()) {
            //不存在则递归创建目录
            libFile.mkdirs();
        }
        //pom文件路径
        File pomFile = new File(parentDir + dubboConfiguration.getPom());
        if (!pomFile.exists()) {
            if (!pomFile.getParentFile().exists()) {
                //递归创建目录
                pomFile.getParentFile().mkdirs();
            }
            //将工作目录下的pom.xml文件拷贝到pom路径下
            try {
                FileChannel in = new FileInputStream(workspace + System.getProperty("file.separator") + "pom.xml").getChannel();
                FileChannel out = new FileOutputStream(pomFile).getChannel();
                in.transferTo(0, in.size(), out);
            } catch (FileNotFoundException e) {
                log.error("拷贝文件失败，error:{}", e);
            } catch (IOException e) {
                log.error("拷贝文件失败，error:{}", e);
            }
        }
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

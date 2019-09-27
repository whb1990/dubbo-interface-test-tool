package com.whb.dubbo;

import com.whb.dubbo.context.ApplicationReadyEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DubboInterfaceTestApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(DubboInterfaceTestApplication.class);
        //启动的时候加载Jar包
        springApplication.addListeners(new ApplicationReadyEventListener());
        springApplication.run(args);
        System.out.println("=============Dubbo接口测试服务启动完毕========================");
    }
}


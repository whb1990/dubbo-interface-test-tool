package com.whb.dubbo.crontroller;

import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.service.PomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * @author Joey
 * @date 2018/10/30 16:28
 */
@Slf4j
@RestController
@RequestMapping("/dubbo/sys")
public class SysConfController {

    @Value("${dubbo.watchdog.url}")
    private String url;

    @Resource
    private PomService pomService;

    /**
     * 重新加载Jar
     *
     * @param response
     * @return
     */
    @RequestMapping("/doReload")
    public ResultDTO<String> doReload(HttpServletResponse response) {
        log.info("SysConfController.doReload");
        try {
            return pomService.loadJars("");
        } catch (NoSuchMethodException | MalformedURLException e) {
            return ResultDTO.handleException(null, null, e);
        }
    }

    /**
     * 清空Jar
     *
     * @param response
     * @return
     */
    @RequestMapping("/doRepublish")
    public ResultDTO<String> doRepublish(HttpServletResponse response) {
        log.info("SysConfController.doRepublish");
        return pomService.deleteJars("");
    }
}

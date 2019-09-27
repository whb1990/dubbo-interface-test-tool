package com.whb.dubbo.interceptor;

import com.whb.dubbo.util.IPUtils;
import com.whb.dubbo.util.StringUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: whb
 * @date: 2019/9/27 19:46
 * @description: Web拦截器
 */
public class WebMvcInterceptor implements HandlerInterceptor {

    private long time;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String requestIp = IPUtils.getIpAddr(req);
        time = System.currentTimeMillis();
        Map<String, String[]> map = req.getParameterMap();
        HandlerMethod method = (HandlerMethod) o;
        String name = method.getMethod().getDeclaringClass().getName() + "." + method.getMethod().getName();
        System.out.println("======调用方Ip：" + requestIp + "===" + name + "===Params：=========");
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            System.out.println("=========key：" + entry.getKey() + ",  value" + StringUtil.jsonJoin(entry.getValue()));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String requestIp = IPUtils.getIpAddr(httpServletRequest);
        long nowTime = System.currentTimeMillis();
        HandlerMethod method = (HandlerMethod) o;
        String name = method.getMethod().getDeclaringClass().getName() + "." + method.getMethod().getName();
        System.out.println("======调用方Ip：" + requestIp + "===" + name + "已调用完成============共花了" + ((nowTime - time) / 1000f) + "秒===========");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

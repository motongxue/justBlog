package com.nbclass.framework.interceptor;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.util.IpUtil;
import com.nbclass.service.PageViewService;
import com.nbclass.service.RedisService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Configuration
public class PageViewInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private PageViewService pageViewService;
    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getIpAddr(request);
        String ipKey= CacheKeyPrefix.SYS_PAGE_VIEW.getPrefix()+"_"+ip.replace(".","_");
        Object object = redisService.get(ipKey);
        if(object==null){
            pageViewService.updateSystemPageViewNum();
            redisService.set(ipKey,true,3600, TimeUnit.SECONDS);
        }
        return true;
    }
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}

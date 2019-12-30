package com.nbclass.framework.interceptor;

import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.util.IpUtil;
import com.nbclass.service.ConfigService;
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
    private ConfigService configService;
    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtil.getIpAddr(request);
        String ipKey= ConfigKey.SYSTEM_PAGE_VIEW.getValue()+"_"+ip.replace(".","");
        Integer currentUser = redisService.get(ipKey);
        if(currentUser==null){
            String value = configService.selectAll().get(ConfigKey.SYSTEM_PAGE_VIEW.getValue());
            configService.updateByKey(ConfigKey.SYSTEM_PAGE_VIEW.getValue(), String.valueOf((Integer.valueOf(value)+1)));
            redisService.set(ipKey,1,3600, TimeUnit.SECONDS);
        }
        return true;
    }
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

}

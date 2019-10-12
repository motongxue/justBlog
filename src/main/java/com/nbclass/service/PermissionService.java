package com.nbclass.service;

import com.nbclass.exception.AuthenticationException;
import com.nbclass.jwt.JwtUtil;
import com.nbclass.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * web端模块
 * @version V1.0
 * @date 2018年7月11日
 * @author superzheng
 */
@Component("perms")
public class PermissionService {
    @Autowired
    private JwtUtil jwtUtil;
    private static final String at = "access_token";
    public boolean isLogin(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        HttpServletRequest request = attrs.getRequest();
        String access_token = CookieUtil.getCookieByName(request, at);
        if(StringUtils.isBlank(access_token)){
            access_token=request.getHeader(at);
        }
        if (StringUtils.isBlank(access_token)){
            throw new AuthenticationException("缺少access_token!");
        }

        return jwtUtil.verifyToken(access_token)!=null;
    }

   /* public boolean isAdmin(){

    }*/
}

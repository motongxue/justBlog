package com.nbclass.service;

import com.nbclass.jwt.JwtUser;
import com.nbclass.jwt.JwtUtil;
import com.nbclass.util.CookieUtil;
import com.nbclass.util.CoreConst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 权限模块，thymeleaf调用
 *
 * @author nbclass
 * @version V1.0
 * @date 2019-10-12
 */
@Component("perms")
public class PermissionService {
    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    private static final String at = "access_token";

    @Autowired
    private JwtUtil jwtUtil;

    public boolean isLogin(){
        return verify()!=null;
    }

    private JwtUser verify(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        HttpServletRequest request = attrs.getRequest();
        String access_token = Optional.ofNullable(CookieUtil.getCookieByName(request, at)).orElse(request.getHeader(at));
        try{
            if(StringUtils.isNotBlank(access_token)){
                return jwtUtil.verifyToken(access_token);
            }
        }catch (Exception e){
            logger.error("jwt验证失败", e);
        }
        return null;
    }
}

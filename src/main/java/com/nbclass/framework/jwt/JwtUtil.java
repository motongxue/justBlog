package com.nbclass.framework.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.util.CookieUtil;
import com.nbclass.framework.util.CopyUtil;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.model.BlogUser;
import com.nbclass.framework.util.DateUtil;
import com.nbclass.service.RedisService;
import com.nbclass.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * JWT处理类
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtProp jwtProp;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    /**
     * 生成token
     * @param user
     * @return
     */
    public String genToken(BlogUser user){

        try {
            //使用HMAC256进行加密
            Algorithm algorithm = Algorithm.HMAC256(jwtProp.getSecret());
            Date date = new Date();
            //创建jwt
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(jwtProp.getIssuer()) //发行人
                    .withIssuedAt(date)  //发行时间
                    .withExpiresAt(DateUtil.addDays(date, jwtProp.getExpire())) //过期时间点
                    .withClaim("userId", user.getUserId())
                    .withClaim("username", user.getUsername());
            //传入参数
            //签名加密
            return builder.sign(algorithm);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

   /**
     * 解密jwt
     * @param token
     * @return
     * @throws RuntimeException
     */
    public JwtUser verifyToken(String token) throws RuntimeException{
        Algorithm algorithm = null;
        try {
            //使用HMAC256进行加密
            algorithm = Algorithm.HMAC256(jwtProp.getSecret());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        //解密
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(jwtProp.getIssuer()).build();
        DecodedJWT jwt =  verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUserId(map.get("userId").asString());
        jwtUser.setUsername(map.get("username").asString());
        jwtUser.setIssuedAt(new Date(map.get("iat").asInt()*1000L));
        jwtUser.setExpiration(new Date(map.get("exp").asInt()*1000L));
        return jwtUser;
    }

    public String getUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String access_token = Optional.ofNullable(CookieUtil.getCookieByName(request, CoreConst.ACCESS_TOKEN)).orElse(request.getHeader(CoreConst.ACCESS_TOKEN));
        return verifyToken(access_token).getUserId();
    }

    public UserInfo getUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String access_token = Optional.ofNullable(CookieUtil.getCookieByName(request, CoreConst.ACCESS_TOKEN)).orElse(request.getHeader(CoreConst.ACCESS_TOKEN));
        String userId = verifyToken(access_token).getUserId();
        if(StringUtils.isEmpty(userId)){
            return null;
        }
        String cacheKey = CacheKeyPrefix.SYS_USER.getPrefix()+userId;
        UserInfo userInfo = redisService.get(cacheKey);
        if(userInfo==null){
            userInfo = CopyUtil.getCopy(userService.selectByUserId(userId),UserInfo.class);
            redisService.set(cacheKey,userInfo);
        }
        return userInfo;
    }





}
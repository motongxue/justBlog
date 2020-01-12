package com.nbclass.service.impl;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.enums.TemplateType;
import com.nbclass.framework.exception.MailException;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.jwt.JwtUtil;
import com.nbclass.framework.jwt.UserInfo;
import com.nbclass.framework.util.*;
import com.nbclass.mapper.UserMapper;
import com.nbclass.model.BlogUser;
import com.nbclass.service.*;
import com.nbclass.vo.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private ConfigService configService;

    @Resource
    private EmailService emailService;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public ResponseVo save(BlogUser user) {
        if(user.getId()==null){
            if(userMapper.selectByUsername(user.getUsername())!=null ){
                return ResponseUtil.error("用户已存在!");
            }
            user.setCreateTime(new Date());
            user.withUserId(UUIDUtil.getUniqueIdByUUId())
                .withSalt(UUIDUtil.uuid())
                .withNickname(user.getUsername())
                .withStatus(CoreConst.STATUS_VALID)
                .withCreateTime(new Date())
            ;
            PasswordHelper.encryptPassword(user);
            userMapper.insertSelective(user);
            return ResponseUtil.success("添加成功");
        }else{
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            redisService.del(CacheKeyPrefix.SYS_USER.getPrefix()+user.getUserId());
            return ResponseUtil.success("保存成功", CopyUtil.getCopy(user,UserInfo.class));
        }
    }

    @Override
    public ResponseVo login(BlogUser login) {
        BlogUser user = userMapper.selectByUsername(login.getUsername());
        if(user==null || !PasswordHelper.verify(login.getPassword(),user)){
            return ResponseUtil.error("用户名或者密码错误");
        }
        redisService.set(CacheKeyPrefix.SYS_USER.getPrefix()+user.getUserId(), CopyUtil.getCopy(user, UserInfo.class));
        String token = jwtUtil.genToken(user);
        return ResponseUtil.success("登录成功",token);
    }

    @Override
    public BlogUser selectByUserId(String userId) {
        return userMapper.selectByUserId(userId);
    }

    @Override
    public ResponseVo changePassword(BlogUser user, String newPassword) {
        BlogUser blogUser = userMapper.selectByPrimaryKey(user);
        user.setSalt(blogUser.getSalt());
        PasswordHelper.encryptPassword(user);
        if(!blogUser.getPassword().equals(user.getPassword())){
            return ResponseUtil.error("密码不正确");
        }
        user.setPassword(newPassword);
        PasswordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKeySelective(user);
        return ResponseUtil.success("修改密码成功");
    }

    @Override
    public ResponseVo forget(String username) {

        BlogUser user = userMapper.selectByUsername(username);
        if(user==null){
            return ResponseUtil.error("用户不存在");
        }
        Map<String, String> configMap = configService.selectAll();
        String resetType = configMap.get(ConfigKey.RESET_PWD_TYPE.getValue());
        Map<String,String> map = new HashMap<>();
        map.put("resetType",resetType);
        if(resetType.equals("2")){
            if(StringUtils.isBlank(user.getEmail())){
                return ResponseUtil.error("用户邮箱不存在");
            }
            if(ValidatorUtil.isEmail(user.getEmail())){
                String code = UUIDUtil.generateIntCode(6);
                redisService.set(CacheKeyPrefix.RESET_.getPrefix()+username, code, 15, TimeUnit.MINUTES);
                try{
                    emailService.sendVerificationCode(user.getEmail(),user.getUsername(),code,"找回密码");
                }catch (MailException e){
                    return ResponseUtil.error(e.getMessage());
                }
                return ResponseUtil.success("验证码已发至邮箱",map);
            }else{
                return ResponseUtil.error("该用户邮箱格式不正确");
            }
        }
        return ResponseUtil.success(map);
    }

    @Override
    public ResponseVo reset(String username, String password, String code) {
        Map<String, String> configMap = configService.selectAll();
        String resetType = configMap.get(ConfigKey.RESET_PWD_TYPE.getValue());
        if (resetType.equals("1")) {
            if (!code.equals(configMap.get(ConfigKey.SECURITY_CODE.getValue()))) {
                return ResponseUtil.error("安全码不正确");
            }
        } else {
            String cacheCode = redisService.get(CacheKeyPrefix.RESET_.getPrefix() + username);
            if (StringUtils.isBlank(cacheCode)) {
                return ResponseUtil.error("验证码已失效");
            }
            if (!code.equals(cacheCode)) {
                return ResponseUtil.error("验证码不正确");
            }
            redisService.del(CacheKeyPrefix.RESET_.getPrefix() + username);
        }
        BlogUser user = userMapper.selectByUsername(username);
        user.setPassword(password);
        PasswordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKeySelective(user);
        return ResponseUtil.success("重置密码成功");

    }
}

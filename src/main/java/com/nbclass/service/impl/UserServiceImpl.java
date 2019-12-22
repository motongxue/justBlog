package com.nbclass.service.impl;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.jwt.JwtUtil;
import com.nbclass.framework.jwt.UserInfo;
import com.nbclass.framework.util.*;
import com.nbclass.mapper.UserMapper;
import com.nbclass.model.BlogUser;
import com.nbclass.service.RedisService;
import com.nbclass.service.UserService;
import com.nbclass.vo.ResponseVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private  UserMapper userMapper;

    @Resource
    private RedisService redisService;

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
        redisService.set(CacheKeyPrefix.SYS_USER+user.getUserId(), CopyUtil.getCopy(user, UserInfo.class));
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

}

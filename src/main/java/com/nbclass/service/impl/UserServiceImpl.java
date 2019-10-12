package com.nbclass.service.impl;

import com.nbclass.jwt.JwtUtil;
import com.nbclass.mapper.UserMapper;
import com.nbclass.model.User;
import com.nbclass.service.UserService;
import com.nbclass.util.*;
import com.nbclass.vo.ResponseVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private  UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public ResponseVo register(User user) {
        if(userMapper.selectByUsername(user.getUsername())!=null){
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
        String token = jwtUtil.genToken(user);
        return ResponseUtil.success("success", token);
    }
}

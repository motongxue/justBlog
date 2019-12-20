package com.nbclass.service;

import com.nbclass.model.BlogUser;
import com.nbclass.vo.ResponseVo;

public interface UserService {

    ResponseVo save(BlogUser user);

    ResponseVo login(BlogUser user);

    BlogUser selectByUserId(String userId);
}

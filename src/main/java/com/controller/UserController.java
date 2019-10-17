package com.controller;

import com.nbclass.model.User;
import com.nbclass.service.UserService;
import com.nbclass.util.ResponseUtil;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * blog rest接口控制器
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("add")

    public ResponseVo add(User user){
        return userService.add(user);
    }

}

package com.nbclass.controller.admin;

import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.model.BlogUser;
import com.nbclass.service.UserService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    @AccessToken
    public ResponseVo add(BlogUser user){
        return userService.save(user);
    }

    @PostMapping("/detail/{userId}")
    @AccessToken
    public BlogUser detail(@PathVariable("userId")String userId){
        return userService.selectByUserId(userId);
    }

}

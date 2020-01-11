package com.nbclass.controller.admin;

import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.model.BlogUser;
import com.nbclass.service.UserService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录注册接口控制器
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Controller
public class LoginController {

    private static final String pathSuffix="admin/";
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return  pathSuffix + "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseVo login(BlogUser user){
        return userService.login(user);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseVo logout(){
        return ResponseUtil.success("退出成功");
    }

    @PostMapping("/forget")
    @ResponseBody
    public ResponseVo forget(@RequestParam String username){
        return userService.forget(username);
    }

    @PostMapping("/reset")
    @ResponseBody
    public ResponseVo reset(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String code){
        return userService.reset(username,password,code);
    }

}

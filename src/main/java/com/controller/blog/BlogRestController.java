package com.controller.blog;

import com.google.code.kaptcha.Constants;
import com.nbclass.model.User;
import com.nbclass.service.UserService;
import com.nbclass.util.PasswordHelper;
import com.nbclass.util.ResponseUtil;
import com.nbclass.util.UUIDUtil;
import com.nbclass.vo.ResponseVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * blog rest接口控制器
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@RestController
public class BlogRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseVo register(User user){
        return userService.register(user);
    }
}

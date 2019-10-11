package com.controller.blog;

import com.nbclass.service.UserService;
import com.nbclass.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前端页面控制器
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Controller
public class BlogWebController {

    private static final String THEME_PREFIX = "theme/";

    @Autowired
    private UserService userService;
    @GetMapping("/test")
    @ResponseBody
    public String test(Model model) {
        return GsonUtil.toJson(userService.selectAll());
    }
    /**
     * 首页入口
     */
    @GetMapping("/")
    public String index(Model model) {
        return THEME_PREFIX + "baby" + "/index";
    }
}

package com.controller;

import com.nbclass.annotation.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 前端页面控制器
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Controller
public class BlogPageController {

    private static final String THEME_PREFIX = "blog/";
    /**
     * 首页入口
     */
    @GetMapping("/")
    public String index() {
        return THEME_PREFIX + "zblog" + "/index";
    }
    @GetMapping("/login")
    public String login() {
        return THEME_PREFIX + "zblog" + "/login";
    }
}

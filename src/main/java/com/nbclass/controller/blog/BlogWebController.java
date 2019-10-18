package com.nbclass.controller.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 前端页面控制器
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
@Controller
public class BlogWebController {

    private static final String THEME_PREFIX = "blog/";
    /**
     * 首页入口
     */
    @GetMapping("/")
    public String index() {
        return THEME_PREFIX + "zblog" + "/index";
    }

}

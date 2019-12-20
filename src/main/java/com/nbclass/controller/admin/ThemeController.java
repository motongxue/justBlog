package com.nbclass.controller.admin;

import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.service.ThemeService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/theme")
public class ThemeController {
    @Autowired
    private ThemeService themeService;

    @PostMapping("/list")
    @AccessToken
    public ResponseVo loadTags(){
        return ResponseUtil.success(themeService.selectAll());
    }

}

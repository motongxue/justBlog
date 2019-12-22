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

    @PostMapping("/use")
    @AccessToken
    public ResponseVo use(String themeId){
        themeService.useTheme(themeId);
        return ResponseUtil.success(String.format("主题[%s]启用成功",themeId));
    }

    @PostMapping("/save")
    @AccessToken
    public ResponseVo save(String themeId, String settingJson){
        themeService.updateSettings(themeId, settingJson);
        return ResponseUtil.success(String.format("主题设置保存成功"));
    }

}

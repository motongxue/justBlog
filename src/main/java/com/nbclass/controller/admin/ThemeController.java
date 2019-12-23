package com.nbclass.controller.admin;

import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.theme.ZbFile;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.ResponseUtil;
import com.nbclass.service.ThemeService;
import com.nbclass.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/admin/theme")
public class ThemeController {
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ZbProperties properties;

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

    @PostMapping("/getFiles")
    @AccessToken
    public ResponseVo getFiles(String themeId){
        List<ZbFile> files = FileUtil.listFileTree(Paths.get(properties.getWorkDir() + "/theme/" + themeId));
        return ResponseUtil.success(files);
    }

    @PostMapping("/getFileContent")
    @AccessToken
    public ResponseVo getFileContent(String path){
        return ResponseUtil.success(themeService.getFileContent(path));
    }

}

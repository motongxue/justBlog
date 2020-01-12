package com.nbclass.controller.admin;

import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.exception.ResourceNotFoundException;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.jwt.JwtUtil;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.framework.util.PropertiesUtil;
import com.nbclass.model.BlogArticle;
import com.nbclass.service.ArticleService;
import com.nbclass.service.ConfigService;
import com.nbclass.service.ThemeService;
import com.nbclass.vo.ConfigEmailVo;
import com.nbclass.vo.ConfigStorageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.Map;

/**
 * blog rest接口控制器
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Controller
@RequestMapping("/admin")
public class AdminPageController{

    private static final String pathSuffix="admin/";

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ThemeService themeService;

    @GetMapping(value = {"", "/"})
    @AccessToken
    public String index(Model model) {
        model.addAttribute("userInfo", jwtUtil.getUserInfo());
        model.addAttribute("systemIsSet",configService.selectAll().get(ConfigKey.SYSTEM_IS_SET.getValue()));
        return  pathSuffix + "index";
    }

    @GetMapping("/welcome")
    @AccessToken
    public String welcome(Model model) {
        model.addAttribute("siteInfo",articleService.siteInfoStatistics());
        return  pathSuffix + "welcome";
    }

    @GetMapping("/categories")
    @AccessToken
    public String categories(){
        return  pathSuffix + "categories";
    }

    @GetMapping("/articles")
    @AccessToken
    public String articles(){
        return  pathSuffix + "articles";
    }

    @GetMapping("/article/add")
    @AccessToken
    public String articleAdd(Model model){
        model.addAttribute("editorType",configService.selectAll().get(ConfigKey.EDITOR_TYPE.getValue()));
        return  pathSuffix + "article-add";
    }

    @GetMapping("/article/edit/{id}")
    @AccessToken
    public String articleEdit(Model model, @PathVariable("id") Integer id){
        BlogArticle article = articleService.selectById(id);
        if (article == null) {
            throw new ResourceNotFoundException();
        }
        model.addAttribute("article", article);
        return  pathSuffix + "article-edit";
    }

    @GetMapping("/tags")
    @AccessToken
    public String tags(){
        return  pathSuffix + "tags";
    }

    @GetMapping("/comments")
    @AccessToken
    public String comments(Model model){
        model.addAttribute("emailServiceEnable",configService.selectEmailConfig().getSetFlag());
        return  pathSuffix + "comments";
    }

    @GetMapping("/themes")
    @AccessToken
    public String themes(Model model){
        model.addAttribute("curThemeId",themeService.selectCurrent().getId());
        model.addAttribute("themes",themeService.selectAll());
        return  pathSuffix + "themes";
    }

    @GetMapping("/theme/{themeId}/setting")
    @AccessToken
    public String themeSetting(Model model,@PathVariable("themeId")String themeId){
        ZbTheme theme = themeService.selectByThemeId(themeId);
        if(theme.getSetFlag()==null||theme.getSetFlag()==0){
            throw new ZbException("该主题不支持设置！");
        }
        model.addAttribute("theme", theme);
        return  pathSuffix + "theme-setting";
    }

    @GetMapping("/theme/{themeId}/edit")
    @AccessToken
    public String themeEdit(Model model,@PathVariable("themeId")String themeId){
        ZbTheme theme = themeService.selectByThemeId(themeId);
        model.addAttribute("theme", theme);
        return  pathSuffix + "theme-edit";
    }


    @GetMapping("/links")
    @AccessToken
    public String links(){
        return  pathSuffix + "links";
    }

    @GetMapping("/notifies")
    @AccessToken
    public String notifies(){
        return  pathSuffix + "notifies";
    }

    @GetMapping("/sliders")
    @AccessToken
    public String sliders(){
        return  pathSuffix + "sliders";
    }

    @GetMapping("/files")
    @AccessToken
    public String files(){
        return  pathSuffix + "files";
    }


    @GetMapping(value = "/config")
    @AccessToken
    public String config(Model model){
        Map<String, String> configMap = configService.selectAll();
        String json = configMap.get(ConfigKey.CONFIG_STORAGE.getValue());
        ConfigStorageVo configStorageVo = GsonUtil.fromJson(json, ConfigStorageVo.class);
        String configJson = configMap.get(ConfigKey.CONFIG_EMAIL.getValue());
        ConfigEmailVo configEmailVo = GsonUtil.fromJson(configJson, ConfigEmailVo.class);
        String workDir = CoreConst.USER_HOME + File.separator + PropertiesUtil.getString(CoreConst.WORK_DIR_KEY);
        model.addAttribute("workDir", workDir.endsWith(File.separator)?workDir:workDir+File.separator);
        model.addAttribute("config",configMap);
        model.addAttribute("storageConfig",configStorageVo);
        model.addAttribute("emailConfig",configEmailVo);
        return pathSuffix + "config";
    }

}

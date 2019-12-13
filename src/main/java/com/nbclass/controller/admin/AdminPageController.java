package com.nbclass.controller.admin;

import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.annotation.AccessToken;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.CategoryService;
import com.nbclass.service.ConfigService;
import com.nbclass.vo.CloudStorageConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * blog rest接口控制器
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private static final String pathSuffix="admin/";

    @Autowired
    private ConfigService configService;

    @GetMapping(value = {"", "/"})
    @AccessToken
    public String index() {
        return  pathSuffix + "index";
    }

    @GetMapping("/welcome")
    @AccessToken
    public String welcome() {
        return  pathSuffix + "welcome";
    }

    @GetMapping("/site/info")
    @AccessToken
    public String websiteInfo(Model model){
        model.addAttribute("siteInfo",configService.selectAll());
        return  pathSuffix + "siteinfo";
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
    public String articleAdd(){
        return  pathSuffix + "article-add";
    }

    @GetMapping("/links")
    @AccessToken
    public String links(){
        return  pathSuffix + "links";
    }

    @GetMapping(value = "/config")
    @AccessToken
    public String config(Model model){
        String json = configService.selectAll().get(ConfigKey.CLOUD_STORAGE_CONFIG.getValue());
        CloudStorageConfigVo cloudStorageConfig = GsonUtil.fromJson(json, CloudStorageConfigVo.class);
        model.addAttribute("cloudStorageConfig",cloudStorageConfig);
        return pathSuffix + "config";
    }

}

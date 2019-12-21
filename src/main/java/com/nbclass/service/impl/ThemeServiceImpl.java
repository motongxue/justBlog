package com.nbclass.service.impl;

import com.google.gson.reflect.TypeToken;
import com.nbclass.enums.ConfigKey;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeSetting;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.ConfigService;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.*;

@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisService redisService;

    @Override
    public void useTheme(String themeId) {
        CoreConst.currentTheme=themeId;
        this.selectAll().forEach(item->{
            if(item.getId().equals(themeId)){
                Map<String,Object> map = new HashMap<>();
                ZbThemeSetting themeSetting = redisService.get(CacheKeyPrefix.THEME.getPrefix()+themeId);
                if(themeSetting!=null){
                    themeSetting.getForm().forEach(formItem->map.put(formItem.getName(),formItem));
                }
                item.setForm(map);
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), item);
                initThymeleafVars();
            }
        });
    }

    @Override
    public ZbTheme selectCurrent() {
        return redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
    }

    @Override
    public void initThymeleafVars() {
        if (thymeleafViewResolver != null) {
            ZbTheme currentTheme = selectCurrent();
            Map<String, Object> vars = new HashMap<>();
            String cdn = configService.selectAll().get(ConfigKey.SITE_CDN.getValue());
            String staticPath = String.format("%s/theme/%s/static",cdn!=null ? cdn : "",currentTheme.getId());
            vars.put("static", staticPath);
            vars.put("currentTheme", currentTheme);
            CoreConst.currentTheme=currentTheme.getId();
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }

    @Override
    public List<ZbTheme> selectAll() {
        String json = redisService.get(CacheKeyPrefix.THEMES.getPrefix());
        Map<String, ZbTheme> map = GsonUtil.fromJson(json,new TypeToken<Map<String, ZbTheme>>(){}.getType());
        List<ZbTheme> list = new ArrayList<>();
        map.forEach((k,v)-> list.add(v));
        return list;
    }

    @Override
    public void updateSettings(String settingsJson) {
        //TODO:修改主题设置时，需要检查当前主题CURRENT_THEME，和缓存中的主题THEME_XXX,赋值form属性;

        //重新初始化模板常量
        initThymeleafVars();
    }

}

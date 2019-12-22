package com.nbclass.service.impl;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.service.ConfigService;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

@Service
public class ThymeleafServiceImpl implements ThymeleafService {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisService redisService;

    @Override
    public void init() {
        if (thymeleafViewResolver != null) {
            ZbTheme currentTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
            Map<String, Object> vars = new HashMap<>();
            String cdn = configService.selectAll().get(ConfigKey.SITE_CDN.getValue());
            String staticPath = String.format("%s/theme/%s/static",cdn!=null ? cdn : "",currentTheme.getId());
            vars.put("static", staticPath);
            vars.put("currentTheme", currentTheme);
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }

    @Override
    public void initStaticPath() {
        if (thymeleafViewResolver != null) {
            ZbTheme currentTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
            String cdn = configService.selectAll().get(ConfigKey.SITE_CDN.getValue());
            String staticPath = String.format("%s/theme/%s/static",cdn!=null ? cdn : "",currentTheme.getId());
            thymeleafViewResolver.addStaticVariable("static",staticPath);
        }
    }

    @Override
    public void initCurrentTheme(ZbTheme theme) {
        thymeleafViewResolver.addStaticVariable("currentTheme",theme);
    }
}

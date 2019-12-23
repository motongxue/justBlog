package com.nbclass.service.impl;

import com.google.gson.reflect.TypeToken;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeForm;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThymeleafService thymeleafService;
    @Autowired
    private RedisService redisService;

    @Override
    public void useTheme(String themeId) {
        CoreConst.currentTheme=themeId;
        ZbTheme theme = this.selectByThemeId(themeId);
        redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), theme);
        thymeleafService.init();
    }

    @Override
    public ZbTheme selectCurrent() {
        return redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
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
    public ZbTheme selectByThemeId(String themeId) {
        return redisService.get(CacheKeyPrefix.THEME.getPrefix()+themeId);
    }

    @Override
    public void updateSettings(String themeId, String settingJson) {
        boolean isCurrent = false;
        if(this.selectCurrent().getId().equals(themeId)){
            isCurrent=true;
        }
        ZbTheme theme = this.selectByThemeId(themeId);
        if(theme!=null){
            List<ZbThemeForm> list = GsonUtil.fromJson(settingJson,new TypeToken<List<ZbThemeForm>>(){}.getType());
            list.forEach(item->{
                theme.getSettings().forEach(setting -> setting.getForm().forEach(formItem->{
                    if(item.getName().equals(formItem.getName())){
                        formItem.setValue(item.getValue());
                    }
                }));
                Map<String, String> settingMap = theme.getSetting();
                for(String key : settingMap.keySet()){
                    if(key.equals(item.getName())){
                        settingMap.put(key,item.getValue());
                    }
                }
            });
            redisService.set(CacheKeyPrefix.THEME.getPrefix()+themeId,theme);
            if(isCurrent){
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(),theme);
                thymeleafService.initCurrentTheme(theme);
            }
        }
    }

    @Override
    public String getFileContent(String absolutePath) {
        Path path = Paths.get(absolutePath);
        try {
             return  new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ZbException("读取内容失败 " + absolutePath, e);
        }
    }

    @Override
    public void saveFileContent(String absolutePath, String content) {
        Path path = Paths.get(absolutePath);
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ZbException("保存文件失败 " + absolutePath, e);
        }
    }

}

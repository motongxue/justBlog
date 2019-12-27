package com.nbclass.framework.listener;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeSetting;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StartedListener
 *
 * @version V1.0
 * @date 2019/10/23
 * @author nbclass
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    Environment environment;
    @Resource
    private RedisService redisService;
    @Resource
    private ThemeService themeService;
    @Resource
    private ThymeleafService thymeleafService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //初始化主题
        this.initThemes();
        //初始化模板引擎
        thymeleafService.init();
        //打印信息
        this.printStartInfo();
    }

    private void printStartInfo() {
        log.info("Blog started success : port : {}",environment.getProperty("server.port"));
    }

    private void initThemes() {
        try {
            log.info("Blog themes start init");
            Path sysSource = themeService.getSysThemePath(null);
            Path userSource = themeService.getUserThemePath(null);
            Map<String, ZbTheme> sysThemeMap = FileUtil.scanThemeFolder(sysSource);
            Map<String, ZbTheme> userThemeMap = FileUtil.scanThemeFolder(userSource);
            if (CollectionUtils.isEmpty(sysThemeMap)) {
                throw new RuntimeException("system theme does not exist");
            }
            //用户自定义主题目录和系统主题目录处理
            if (CollectionUtils.isEmpty(userThemeMap)) {
                FileUtil.copyFolder(sysSource, userSource);
                userThemeMap = sysThemeMap;
            } else {
                Map<String, ZbTheme> finalUserThemeMap = userThemeMap;
                sysThemeMap.forEach((k, v)->{
                    ZbTheme userTheme = finalUserThemeMap.get(k);
                    if(userTheme == null){
                        //系统主题不在用户自定义目录，则copy进用户自定义目录
                        try {
                            FileUtil.copyFolder( themeService.getSysThemePath(k), themeService.getUserThemePath(k));
                            finalUserThemeMap.put(k,v);
                        } catch (IOException e) {
                            throw new RuntimeException("copy theme to user folder error:{}", e);
                        }
                    }
                });
                FileUtil.copyFolder(userSource, sysSource);
            }
            userThemeMap.forEach((k,v)-> themeService.handleThemeSetting(v));
            redisService.set(CacheKeyPrefix.THEMES.getPrefix(),  GsonUtil.toJson(userThemeMap));
            //当前主题处理
            ZbTheme currentTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
            if (null == currentTheme) {
                //第一次启动，初始化当前系统主题
                currentTheme = userThemeMap.entrySet().iterator().next().getValue();
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), currentTheme);
            }
            CoreConst.currentTheme=currentTheme.getId();
        }catch (Exception e){
            throw new RuntimeException("Blog themes init error", e);
        }
    }

}

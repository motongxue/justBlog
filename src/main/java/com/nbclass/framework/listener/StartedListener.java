package com.nbclass.framework.listener;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.util.*;
import com.nbclass.service.ConfigService;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
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
    private ConfigService configService;
    @Resource
    private ThymeleafService thymeleafService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //系统初始化
        this.initSystem();
        //初始化主题
        this.initThemes();
        //初始化模板引擎
        thymeleafService.init();
        //打印信息
        this.printStartInfo();
    }

    private void printStartInfo() {
        String isSet = configService.get(ConfigKey.SYSTEM_IS_SET.getValue());
        String blogUrl;
        if(StringUtils.isNotEmpty(isSet) && isSet.equals(CoreConst.STATUS_VALID_STRING)){
            blogUrl=configService.get(ConfigKey.SITE_HOST.getValue());
            blogUrl = blogUrl.endsWith("/")?blogUrl.substring(0,blogUrl.length()-1):blogUrl;
        }else{
            blogUrl= String.format("http://%s:%s", IpUtil.getMachineIP(), environment.getProperty("server.port"));
        }
        log.info("Blog started success:   {}", blogUrl);
        log.info("Blog admin started success:   {}/admin", blogUrl);
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

    private void initSystem(){
        //系统创建时间
        String createTimeKey = ConfigKey.SYSTEM_CREATE_TIME.getValue();
        String createTimeCache = redisService.get(createTimeKey);
        if(StringUtils.isEmpty(createTimeCache)){
            String createTime = configService.get(createTimeKey);
            if(StringUtils.isEmpty(createTime)){
                createTime = DateUtil.getNewFormatDateString(new Date());
                configService.updateByKey(createTimeKey,createTime);
            }
            redisService.set(createTimeKey,createTime);
        }
        //系统浏览数
        String pageViewKey = ConfigKey.SYSTEM_PAGE_VIEW.getValue();
        String pageViewCache = redisService.get(pageViewKey);
        if(StringUtils.isEmpty(pageViewCache)){
            redisService.set(pageViewKey,configService.get(pageViewKey));
        }
    }

}

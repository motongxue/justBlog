package com.nbclass.framework.listener;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.enums.ConfigKey;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.util.*;
import com.nbclass.model.BlogConfig;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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
    ZbProperties zbProperties;
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
        //初始化Template
        this.initTemplate();
        //初始化模板引擎
        thymeleafService.init();
        //打印信息
        this.printStartInfo();
    }

    private void printStartInfo() {
        Map<String, String> map = configService.selectAll();
        String isSet = map.get(ConfigKey.SYSTEM_IS_SET.getValue());
        String blogUrl;
        if(StringUtils.isNotEmpty(isSet) && isSet.equals(CoreConst.STATUS_VALID_STRING)){
            blogUrl= map.get(ConfigKey.SITE_HOST.getValue());
            blogUrl = blogUrl.endsWith("/")?blogUrl.substring(0,blogUrl.length()-1):blogUrl;
        }else{
            blogUrl= String.format("http://%s:%s", IpUtil.getMachineIP(), environment.getProperty("server.port"));
        }
        log.info("Blog work path at:            {}", zbProperties.getWorkDir());
        log.info("Blog started success:         {}", blogUrl);
        log.info("Blog admin started success:   {}/admin", blogUrl);
    }


    private void initTemplate(){
        try {
            log.info("Blog template start init...");
            Path sysPath = themeService.getSysTemplatePath();
            Path userPath = Paths.get(zbProperties.getWorkTemplateDir());
            try (Stream<Path> pathStream = Files.list(sysPath)) {
                pathStream.forEach(path -> {
                    try {
                        if (Files.isDirectory(path)) {
                            String pathStr = path.toString();
                            if (!(pathStr.endsWith("theme")||pathStr.endsWith("theme/"))) {
                                FileUtil.copyFolder(path, userPath.resolve(sysPath.relativize(path).toString()));
                            }else{
                                //主题文件夹处理
                                try {
                                    Path userSource = themeService.getUserThemePath();
                                    Map<String, ZbTheme> sysThemeMap = FileUtil.scanThemeFolder(path);
                                    Map<String, ZbTheme> userThemeMap = FileUtil.scanThemeFolder(userSource);
                                    if (CollectionUtils.isEmpty(sysThemeMap) && CollectionUtils.isEmpty(userThemeMap)) {
                                        throw new RuntimeException("system theme and user theme not exist");
                                    }
                                    //用户自定义主题目录和系统主题目录处理
                                    if (CollectionUtils.isEmpty(userThemeMap)) {
                                        FileUtil.copyFolder(path, userSource);
                                        userThemeMap = sysThemeMap;
                                    } else {
                                        Map<String, ZbTheme> finalUserThemeMap = userThemeMap;
                                        Objects.requireNonNull(sysThemeMap).forEach((k, v)->{
                                            ZbTheme userTheme = finalUserThemeMap.get(k);
                                            if(userTheme == null){
                                                //系统主题不在用户自定义目录，则copy进用户自定义目录
                                                try {
                                                    FileUtil.copyFolder( themeService.getSysThemePath(k), themeService.getUserThemePath(k));
                                                    finalUserThemeMap.put(k,v);
                                                } catch (IOException e) {
                                                    throw new RuntimeException("Copy system theme ["+k+"] to user folder error:{}", e);
                                                }
                                            }
                                        });
                                    }
                                    Objects.requireNonNull(userThemeMap).forEach((k, v)-> themeService.handleThemeSetting(v));
                                    redisService.set(CacheKeyPrefix.THEMES.getPrefix(),  GsonUtil.toJson(userThemeMap));
                                    //当前主题处理
                                    ZbTheme currentTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
                                    if (null == currentTheme) {
                                        //第一次启动，初始化当前系统主题
                                        currentTheme = userThemeMap.entrySet().iterator().next().getValue();
                                        redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), currentTheme);
                                    }else{
                                        ZbTheme userCurrentTheme = userThemeMap.get(currentTheme.getId());
                                        if(null!=userCurrentTheme){
                                            themeService.handleCurrentTheme(userCurrentTheme, currentTheme);
                                        }
                                    }
                                    CoreConst.currentTheme=currentTheme.getId();
                                }catch (Exception e){
                                    throw new RuntimeException("Blog themes init error", e);
                                }
                            }
                        } else {
                            Files.copy(path, userPath.resolve(sysPath.relativize(path).toString()), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }catch (IOException e) {
                        log.error("Copy system template : {} to user template : {} error  : {}",path.toString(),userPath.toString(),e);
                    }
                });
            }
        } catch (IOException e) {
            log.error("Copy system template folder to user folder error : {}",e);
        }
    }

    private void initSystem(){
        Map<String, String> map = configService.selectAll();
        //系统浏览数
        String pageViewCache = redisService.get( CacheKeyPrefix.SYS_PAGE_VIEW.getPrefix());
        if(StringUtils.isEmpty(pageViewCache)){
            redisService.set( CacheKeyPrefix.SYS_PAGE_VIEW.getPrefix(), map.get(ConfigKey.SYSTEM_PAGE_VIEW.getValue()));
        }
    }

}

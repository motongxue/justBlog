package com.nbclass.framework.listener;

import com.google.gson.reflect.TypeToken;
import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.theme.ZbTheme;
import com.nbclass.framework.theme.ZbThemeForm;
import com.nbclass.framework.theme.ZbThemeSetting;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.GsonUtil;
import com.nbclass.service.RedisService;
import com.nbclass.service.ThemeService;
import com.nbclass.service.ThymeleafService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private ZbProperties zbProperties;
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
            String workThemeDir = zbProperties.getWorkDir() + "theme/";
            String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + CoreConst.THEME_FOLDER;
            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
            boolean isJarEnv = "jar".equalsIgnoreCase(themeUri.getScheme());
            FileSystem fileSystem = isJarEnv? FileSystems.newFileSystem(themeUri, Collections.emptyMap()):null;
            Path sysSource = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER) : Paths.get(themeUri);
            Path userSource = Paths.get(workThemeDir);
            Map<String, ZbTheme> sysThemeMap = FileUtil.scanThemeFolder(sysSource);
            Map<String, ZbTheme> userThemeMap = FileUtil.scanThemeFolder(userSource);
            if (CollectionUtils.isEmpty(sysThemeMap)) {
                throw new RuntimeException("system theme does not exist");
            }
            //用户自定义主题目录和系统主题目录处理
            if (CollectionUtils.isEmpty(userThemeMap)) {
                FileUtil.copyFolder(sysSource, userSource);
                sysThemeMap.forEach((k,v)-> handleThemeSetting(v));
                redisService.set(CacheKeyPrefix.THEMES.getPrefix(), GsonUtil.toJson(sysThemeMap));
            } else {
                sysThemeMap.forEach((k,v)->{
                    ZbTheme userTheme = userThemeMap.get(k);
                    if(userTheme == null){
                        //系统主题不在用户自定义目录，则copy进用户自定义目录
                        try {
                            Path systemThemePath = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER + k) : Paths.get(ResourceUtils.getURL(themeClassPath + k).toURI());
                            FileUtil.copyFolder(systemThemePath, Paths.get(workThemeDir+k));
                            handleThemeSetting(v);
                            userThemeMap.put(k,v);
                        } catch (IOException e) {
                            throw new RuntimeException("copy theme to user folder error:{}", e);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }else{
                        //系统主题在用户自定义目录
                        handleThemeSetting(userTheme);
                    }
                });
                FileUtil.copyFolder(userSource, sysSource);
                redisService.set(CacheKeyPrefix.THEMES.getPrefix(),  GsonUtil.toJson(userThemeMap));
            }
            //当前主题处理
            ZbTheme currentTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
            if (null == currentTheme) {
                //第一次启动，初始化当前系统主题
                currentTheme = sysThemeMap.entrySet().iterator().next().getValue();
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), currentTheme);
            }
            CoreConst.currentTheme=currentTheme.getId();
        }catch (Exception e){
            throw new RuntimeException("Blog themes init error", e);
        }
    }


    private void handleThemeSetting(ZbTheme theme){
        ZbTheme themeCache = redisService.get(CacheKeyPrefix.THEME + theme.getId());
        if (themeCache == null) {
            List<ZbThemeSetting> settingList = theme.getSettings();
            Map<String,String> map= new HashMap<>();
            settingList.forEach(setting-> setting.getForm().forEach(formItem->{
                formItem.setValue(formItem.getDefaultValue());
                map.put(formItem.getName(),formItem.getValue());
            }));
            theme.setSetting(map);
            redisService.set(CacheKeyPrefix.THEME.getPrefix() + theme.getId(), theme);
        }
    }

}

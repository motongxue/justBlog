package com.nbclass.framework.listener;

import com.nbclass.enums.CacheKeyPrefix;
import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.util.CoreConst;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.framework.util.ZbTheme;
import com.nbclass.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private ZbProperties zbProperties;
    @Autowired
    private RedisService redisService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        this.printStartInfo();
        this.initThemes();
    }

    private void printStartInfo() {
        log.info("Blog started success");
    }

    private void initThemes() {
        ZbTheme zbTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
        String sourcePath = Objects.requireNonNull(getClass().getClassLoader().getResource(CoreConst.THEME_FOLDER)).getPath();
        log.debug("get system theme path:{}",sourcePath);
        String destPath = zbProperties.getWorkDir() + "theme/";
        LinkedList<String> list = FileUtil.scanSystemTheme(sourcePath);
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException("system theme does not exist");
        }
        if(null==zbTheme){
            String currentThemeName = list.get(0);
            zbTheme=new ZbTheme();
            zbTheme.setName(currentThemeName);
            //TODO need add more details：settings
            redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), zbTheme);
        }
        LinkedList<String> userThemeList = FileUtil.scanSystemTheme(destPath);
        if(CollectionUtils.isEmpty(userThemeList)){
            FileUtil.copyDictionary(sourcePath, destPath);
        }else{
            for(String systemTheme : list){
                if(!userThemeList.contains(systemTheme)){
                    FileUtil.copyDictionary(sourcePath + systemTheme, destPath);
                    userThemeList.add(systemTheme);
                }
            }
        }
        List<ZbTheme> zbThemeList = new LinkedList<>();
        assert userThemeList != null;
        for(String themeName:userThemeList){
            ZbTheme theme = new ZbTheme();
            theme.setName(themeName);
            zbThemeList.add(theme);
        }
        zbThemeList.sort(new ZbTheme());
        redisService.set(CacheKeyPrefix.THEMES.getPrefix(), zbThemeList);
    }

}

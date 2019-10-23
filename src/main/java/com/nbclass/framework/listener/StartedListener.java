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
import org.springframework.util.ResourceUtils;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
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
        try {
            String workThemeDir = zbProperties.getWorkDir() + "theme/";
            ZbTheme zbTheme = redisService.get(CacheKeyPrefix.CURRENT_THEME.getPrefix());
            String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + CoreConst.THEME_FOLDER;
            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
            boolean isJarEnv = "jar".equalsIgnoreCase(themeUri.getScheme());
            FileSystem fileSystem = isJarEnv? FileSystems.newFileSystem(themeUri, Collections.emptyMap()):null;
            Path source = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER) : Paths.get(themeUri);
            Path dest = Paths.get(workThemeDir);
            LinkedList<String> list = FileUtil.scanSystemTheme(source);
            if (CollectionUtils.isEmpty(list)) {
                throw new RuntimeException("system theme does not exist");
            }
            if (null == zbTheme) {
                String currentThemeName = list.get(0);
                zbTheme = new ZbTheme();
                zbTheme.setName(currentThemeName);
                //TODO need add more details：settings
                redisService.set(CacheKeyPrefix.CURRENT_THEME.getPrefix(), zbTheme);
            }
            LinkedList<String> userThemeList = FileUtil.scanSystemTheme(dest);
            if (CollectionUtils.isEmpty(userThemeList)) {
                FileUtil.copyFolder(source, dest);
            } else {
                for (String systemTheme : list) {
                    if (!userThemeList.contains(systemTheme)) {
                        Path systemThemePath = isJarEnv? fileSystem.getPath("/BOOT-INF/classes/" + CoreConst.THEME_FOLDER + systemTheme) : Paths.get(ResourceUtils.getURL(themeClassPath + systemTheme).toURI());
                        FileUtil.copyFolder(systemThemePath, Paths.get(workThemeDir+systemTheme));
                        userThemeList.add(systemTheme);
                    }
                }
            }
            List<ZbTheme> zbThemeList = new LinkedList<>();
            assert userThemeList != null;
            for (String themeName : userThemeList) {
                ZbTheme theme = new ZbTheme();
                theme.setName(themeName);
                //TODO need add more details：settings
                zbThemeList.add(theme);
            }
            zbThemeList.sort(new ZbTheme());
            redisService.set(CacheKeyPrefix.THEMES.getPrefix(), zbThemeList);
        }catch (Exception e){
            throw new RuntimeException("init theme error", e);
        }
    }

}

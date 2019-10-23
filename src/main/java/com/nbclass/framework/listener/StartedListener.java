package com.nbclass.framework.listener;

import com.nbclass.framework.util.CoreConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * The method executed after the application is started.
 *
 * @author ryanwang
 * @date : 2018/12/5
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {


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
            String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + CoreConst.THEME_FOLDER;
            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();
            log.debug("Theme uri: [{}]", themeUri);
            String path="";
            if ("jar".equalsIgnoreCase(themeUri.getScheme())) {
                path = CoreConst.ROOT_PATH + "/BOOT-INF/classes/" + CoreConst.THEME_FOLDER;
                log.debug("jar template url : {}" ,path);
            } else {
                path = themeUri.getPath();
                log.debug("file template url : {}" ,path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Initialize internal theme to user path error", e);
        }
    }

}

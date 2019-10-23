package com.nbclass.framework.config;

import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMvcConfig
 *
 * @author xingkong1221
 * @since 2017-07-12
 */
@Configuration
@Order(1)
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String FILE_PROTOCOL = "file:///";


    @Autowired
    private RedisService redisService;
    @Autowired
    private  ZbProperties zbProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String workDir = FILE_PROTOCOL + zbProperties.getWorkDir();
        registry.addResourceHandler("/theme/*/static/**")
                .addResourceLocations(workDir + "theme/");
    }

}

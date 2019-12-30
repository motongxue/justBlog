package com.nbclass.framework.config;

import com.nbclass.framework.config.properties.ZbProperties;
import com.nbclass.framework.interceptor.PageViewInterceptor;
import com.nbclass.framework.util.CoreConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.File;


/**
 * WebMvcConfig配置类-资源映射
 *
 * @version V1.0
 * @date 2019/10/24
 * @author nbclass
 */
@Configuration
@Order(1)
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String FILE_PROTOCOL = "file:///";

    @Resource
    private  ZbProperties zbProperties;

    @Resource
    private PageViewInterceptor pageViewInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/theme/*/static/**")
                .addResourceLocations(FILE_PROTOCOL + zbProperties.getWorkThemeDir() + File.separator);
        registry.addResourceHandler("/file/**")
                .addResourceLocations(FILE_PROTOCOL + zbProperties.getWorkFileDir() + File.separator);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageViewInterceptor).addPathPatterns("/**");
    }

}

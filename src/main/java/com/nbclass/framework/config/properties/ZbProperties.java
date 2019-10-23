package com.nbclass.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ZbProperties属性类
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Data
@ConfigurationProperties("zb")
@Component
public class ZbProperties{

    /**
     * Work directory.
     */
    private String workDir;

}

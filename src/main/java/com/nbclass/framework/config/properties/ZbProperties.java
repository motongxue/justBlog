package com.nbclass.framework.config.properties;

import com.nbclass.framework.util.CoreConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * ZbProperties属性类
 *
 * @version V1.0
 * @date 2019/10/11
 * @author nbclass
 */
@Data
@ConfigurationProperties("zb")
@Component("zbProperties")
public class ZbProperties{

    /**
     * Work directory.
     */
    private String workDir;

    public String getWorkDir(){
        return CoreConst.USER_HOME + File.separator + workDir;
    }

    public String getWorkThemeDir(){
        return CoreConst.USER_HOME + File.separator + workDir + File.separator + CoreConst.THEME_;
    }

    public String getWorkThemeDir(String themeId){
        return CoreConst.USER_HOME + File.separator + workDir + File.separator + CoreConst.THEME_ + File.separator + themeId;
    }

    public String getWorkFileDir(){
        return CoreConst.USER_HOME + File.separator + workDir + File.separator + CoreConst.FILE_;
    }



}

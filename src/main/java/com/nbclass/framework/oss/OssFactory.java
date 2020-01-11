package com.nbclass.framework.oss;

import com.nbclass.framework.holder.SpringContextHolder;
import com.nbclass.service.ConfigService;
import com.nbclass.vo.ConfigStorageVo;

/**
 * 文件上传
 */
public final class OssFactory {

    public static OssService init(){
        //获取云存储配置信息
        ConfigService sysConfigService = SpringContextHolder.getBean(ConfigService.class);
        ConfigStorageVo config = sysConfigService.selectStorageConfig();
        if(config.getType() == OssTypeEnum.LOCAL.getValue()){
            return new LocalOssService(config);
        }else if(config.getType() == OssTypeEnum.QINIU.getValue()){
            return new QiniuOssService(config);
        }else if(config.getType() == OssTypeEnum.ALIYUN.getValue()){
            return new AliyunOssService(config);
        }else if(config.getType() == OssTypeEnum.QCLOUD.getValue()){
            return new QcloudOssService(config);
        }
        return null;
    }

    public static OssService init(Integer type){
        //获取云存储配置信息
        ConfigService sysConfigService = SpringContextHolder.getBean(ConfigService.class);
        ConfigStorageVo config = sysConfigService.selectStorageConfig();
        if(type.equals(OssTypeEnum.LOCAL.getValue())){
            return new LocalOssService(config);
        }else if(type.equals(OssTypeEnum.QINIU.getValue())){
            return new QiniuOssService(config);
        }else if(type.equals(OssTypeEnum.ALIYUN.getValue())){
            return new AliyunOssService(config);
        }else if(type.equals(OssTypeEnum.QCLOUD.getValue())){
            return new QcloudOssService(config);
        }
        return null;
    }

}

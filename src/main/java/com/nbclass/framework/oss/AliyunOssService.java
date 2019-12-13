package com.nbclass.framework.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.nbclass.framework.exception.ZbException;
import com.nbclass.vo.CloudStorageConfigVo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储
 */
public class AliyunOssService extends OssService {
    private OSSClient client;

    AliyunOssService(CloudStorageConfigVo config){
        this.config = config;
        //初始化
        init();
    }

    private void init(){
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
    }

    @Override
    public String upload(byte[] data, String path, boolean isPublic) {
        return upload(new ByteArrayInputStream(data), path, isPublic);
    }

    @Override
    public String upload(InputStream inputStream, String path, boolean isPublic) {
        try {
            if(isPublic){
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
                client.putObject(config.getAliyunBucketName(), path, inputStream,objectMetadata);
            }else{
                client.putObject(config.getAliyunBucketName(), path, inputStream);
            }
        } catch (Exception e){
            throw new ZbException("上传文件失败，请检查配置信息", e);
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix), isPublic);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix), isPublic);
    }
}

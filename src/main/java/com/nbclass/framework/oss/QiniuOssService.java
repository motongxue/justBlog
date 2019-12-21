package com.nbclass.framework.oss;

import com.nbclass.framework.exception.ZbException;
import com.nbclass.vo.CloudStorageConfigVo;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 */
public class QiniuOssService extends OssService {
    private UploadManager uploadManager;
    private String token;

    QiniuOssService(CloudStorageConfigVo config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path, boolean isPublic) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new ZbException("上传文件失败，请核对七牛配置信息", e);
        }

        return config.getQiniuDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path, boolean isPublic) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path, isPublic);
        } catch (IOException e) {
            throw new ZbException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix), isPublic);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix), isPublic);
    }
}

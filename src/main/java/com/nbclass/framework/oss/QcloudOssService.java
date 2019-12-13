package com.nbclass.framework.oss;


import com.nbclass.framework.exception.ZbException;
import com.nbclass.vo.CloudStorageConfigVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 腾讯云存储
 */
public class QcloudOssService extends OssService {
    private COSClient client;

    QcloudOssService(CloudStorageConfigVo config){
        this.config = config;
        init();
    }

    private void init(){
        COSCredentials credentials = new BasicCOSCredentials(config.getQcloudSecretId(),
                config.getQcloudSecretKey());
        Region region = new Region(config.getQcloudRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        client = new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(byte[] data, String path, boolean isPublic) {
        return upload(new ByteArrayInputStream(data), path, isPublic);
    }

    @Override
    public String upload(InputStream inputStream, String path, boolean isPublic) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getQcloudBucketName(), path, inputStream,objectMetadata);
            if(isPublic){
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        } catch (CosClientException e) {
            throw new ZbException("文件上传失败，" + e.getMessage());
        }
        client.shutdown();
        return config.getQcloudDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix), isPublic);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix), isPublic);
    }
}

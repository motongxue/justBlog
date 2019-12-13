package com.nbclass.framework.oss;

import com.nbclass.framework.exception.ZbException;
import com.nbclass.framework.util.FileUtil;
import com.nbclass.vo.CloudStorageConfigVo;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云存储
 */
public class LocalOssService extends OssService {

    LocalOssService(CloudStorageConfigVo config){
        this.config = config;
    }
    @Override
    public String upload(byte[] data, String path, boolean isPublic) {
        return upload(new ByteArrayInputStream(data), path, isPublic);
    }

    @Override
    public String upload(InputStream is, String path, boolean isPublic) {
        try {
            FileOutputStream os = new FileOutputStream(path);
            byte[] bb = new byte[1024];
            int ch;
            while ((ch = is.read(bb)) > -1) {
                os.write(bb, 0, ch);
            }
            os.close();
            is.close();
        } catch (Exception e){
            throw new ZbException("上传本地文件失败", e);
        }
        String localDomain = config.getLocalDomain().endsWith("/")?config.getLocalDomain():config.getLocalDomain()+"/";
        return localDomain+path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix, boolean isPublic) {
        return upload(data, getPath(suffix), isPublic);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix, boolean isPublic) {
        return upload(inputStream, getPath(suffix), isPublic);
    }

    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String filePathPrefix = config.getLocalPrefix().endsWith("/")?config.getLocalPrefix():config.getLocalPrefix()+"/";
        if(!FileUtil.exists(filePathPrefix)){
            try {
                FileUtil.createDir(filePathPrefix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePathPrefix + uuid + suffix;
    }
}
